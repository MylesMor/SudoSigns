package dev.mylesmor.sudosigns.listeners;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.menus.SignEditor;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.util.Consumer;


/**
 * The ChatListener class for taking user input in chat.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        SudoUser user = SudoSigns.users.get(p.getUniqueId());
        if (user != null) {
            if (user.isTextInput() && user.isEditing()) {
                if (user.getInputType() == PlayerInput.CONSOLE_COMMAND || user.getInputType() == PlayerInput.PLAYER_COMMAND) {
                    e.setCancelled(true);
                    user.getEditor().getCommandsMenu().addCommand(e.getMessage().substring(1), user.getInputType());
                    user.removeTextInput();
                }
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        SudoUser user = SudoSigns.users.get(p.getUniqueId());
        if (user != null) {
            if (user.isTextInput() && user.isEditing()) {
                SignEditor editor = user.getEditor();
                if (e.getMessage().equalsIgnoreCase("cancel")) {
                    handle(e, true, "Cancelled!", editor, user, null, editor::goToMain);
                    return;
                }
                switch (user.getInputType()) {
                    case PLAYER_COMMAND: case CONSOLE_COMMAND:
                        user.removeTextInput();
                        handle(e, true, "No command found! Cancelling...", editor, user, null, editor::goToCommands);
                        break;
                    case RENAME:
                        handle(e, true, null, editor, user, edit -> editor.renameSign(ChatColor.stripColor(e.getMessage())), editor::goToMain);
                        break;
                    case PERMISSION:
                        handle(e, true, null, editor, user, edit -> editor.getPermMenu().addPermission(true, ChatColor.stripColor(e.getMessage())), editor::goToPermissions);
                        break;
                }

            }
        }
    }

    private void handle(AsyncPlayerChatEvent e, boolean cancel, String message, SignEditor editor, SudoUser user, Consumer<SignEditor> eventConsumer, Runnable finalAction) {
        if (cancel) e.setCancelled(true);
        if (message != null) Util.sudoSignsMessage(e.getPlayer(), ChatColor.RED, message, null);
        if (eventConsumer != null) eventConsumer.accept(editor);
        user.removeTextInput();
        Bukkit.getScheduler().runTask(SudoSigns.sudoSignsPlugin, finalAction);
    }
}

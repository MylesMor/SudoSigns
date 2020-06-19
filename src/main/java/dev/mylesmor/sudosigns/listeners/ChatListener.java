package dev.mylesmor.sudosigns.listeners;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SudoUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;


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
                    user.getEditor().addCommand(e.getMessage().substring(1), user.getInputType());
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
                if (e.getMessage().equalsIgnoreCase("cancel")) {
                    e.setCancelled(true);
                    user.removeTextInput();
                    p.sendMessage(SudoSigns.prefix + ChatColor.RED + " Cancelled!");
                    Bukkit.getScheduler().runTask(SudoSigns.sudoSignsPlugin, () -> {
                        user.getEditor().goToMain();
                    });
                }
                if (user.getInputType() == PlayerInput.CONSOLE_COMMAND || user.getInputType() == PlayerInput.PLAYER_COMMAND) {
                    p.sendMessage(SudoSigns.prefix + ChatColor.RED + " No command found! Cancelling...");
                    user.removeTextInput();
                    Bukkit.getScheduler().runTask(SudoSigns.sudoSignsPlugin, () -> {
                        user.getEditor().goToCommands();
                    });
                } else if (user.getInputType() == PlayerInput.RENAME) {
                    e.setCancelled(true);
                    user.getEditor().renameSign(ChatColor.stripColor(e.getMessage()));
                    Bukkit.getScheduler().runTask(SudoSigns.sudoSignsPlugin, () -> {
                        user.getEditor().goToMain();
                    });
                } else if (user.getInputType() == PlayerInput.PERMISSION) {
                    e.setCancelled(true);
                    user.getEditor().addPermission(true, ChatColor.stripColor(e.getMessage()));
                    Bukkit.getScheduler().runTask(SudoSigns.sudoSignsPlugin, () -> {
                        user.getEditor().goToPermissions();
                    });
                }
            }
        }
    }
}

package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import static dev.mylesmor.sudosigns.SudoSigns.*;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        if (users.containsKey(p.getUniqueId())) {
            SudoUser user = users.get(p.getUniqueId());
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
        if (users.containsKey(p.getUniqueId())) {
            SudoUser user = users.get(p.getUniqueId());
            if (user.isTextInput() && user.isEditing()) {
                if (e.getMessage().equalsIgnoreCase("cancel")) {
                    e.setCancelled(true);
                    user.removeTextInput();
                    p.sendMessage(prefix + ChatColor.RED + " Cancelled!");
                    Bukkit.getScheduler().runTask(sudoSignsPlugin, () -> {
                        user.getEditor().goToMain();
                    });
                }
                if (user.getInputType() == PlayerInput.CONSOLE_COMMAND || user.getInputType() == PlayerInput.PLAYER_COMMAND) {
                    p.sendMessage(prefix + ChatColor.RED + " No command found! Cancelling...");
                    user.removeTextInput();
                    Bukkit.getScheduler().runTask(sudoSignsPlugin, () -> {
                        user.getEditor().goToCommands();
                    });
                } else if (user.getInputType() == PlayerInput.RENAME) {
                    e.setCancelled(true);
                    user.getEditor().renameSign(ChatColor.stripColor(e.getMessage()));
                    Bukkit.getScheduler().runTask(sudoSignsPlugin, () -> {
                        user.getEditor().goToMain();
                    });
                } else if (user.getInputType() == PlayerInput.PERMISSION) {
                    e.setCancelled(true);
                    user.getEditor().addPermission(true, ChatColor.stripColor(e.getMessage()));
                    Bukkit.getScheduler().runTask(sudoSignsPlugin, () -> {
                        user.getEditor().goToPermissions();
                    });
                }
            }
        }
    }
}

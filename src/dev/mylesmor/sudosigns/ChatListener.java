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
        if (textInput.containsKey(p) && editors.containsKey(p)) {
            if (textInput.get(p) == PlayerInput.CONSOLE_COMMAND || textInput.get(p) == PlayerInput.PLAYER_COMMAND) {
                e.setCancelled(true);
                editors.get(p).addCommand(e.getMessage().substring(1), textInput.get(p));
            }
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (textInput.containsKey(p) && editors.containsKey(p)) {
            if (textInput.get(p) == PlayerInput.CONSOLE_COMMAND || textInput.get(p) == PlayerInput.PLAYER_COMMAND) {
                p.sendMessage(prefix + ChatColor.RED + " No command found! Cancelling...");
                textInput.remove(p);
                Bukkit.getScheduler().runTask(sudoSignsPlugin, () -> {
                    editors.get(p).goToCommands();
                });
            } else if (textInput.get(p) == PlayerInput.RENAME) {
                e.setCancelled(true);
                editors.get(p).renameSign(ChatColor.stripColor(e.getMessage()));
            }
        }
    }
}

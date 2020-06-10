package dev.mylesmor.sudosigns;

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
            e.setCancelled(true);
            editors.get(p).addCommand(e.getMessage().substring(1), textInput.get(p));
        }
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (textInput.containsKey(p) && editors.containsKey(p)) {
            p.sendMessage(prefix + "No command found! Cancelling...");
            textInput.remove(p);
            editors.get(p).goToCommands();

        }
    }
}

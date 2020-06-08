package dev.mylesmor.sudosigns;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import static dev.mylesmor.sudosigns.SudoSigns.*;

public class SignListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (playersToCreate.containsKey(p)) {
            e.setCancelled(true);
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                signs.get(playersToCreate.get(p)).createSign(sign);
                SignEditor editor = new SignEditor(p, signs.get(playersToCreate.get(p)));
                editors.put(p, editor);
                playersToCreate.remove(p);
            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
                signs.remove(playersToCreate.get(p));
                playersToCreate.remove(p);
            }
        } else if (playersToClick.containsKey(p)) {
            e.setCancelled(true);
            if (e.getClickedBlock().getState() instanceof Sign) {
                signs.get(playersToClick.get(p));
                SignEditor editor = new SignEditor(p, signs.get(playersToClick.get(p)));
                editors.put(p, editor);
            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
                playersToClick.remove(p);
            }
        }
    }
}

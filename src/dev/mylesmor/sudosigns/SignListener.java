package dev.mylesmor.sudosigns;

import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Map;

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
                Sign sign = (Sign) e.getClickedBlock().getState();
                boolean found = false;
                if (playersToClick.get(p).equalsIgnoreCase("EDIT")) {
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            SignEditor editor = new SignEditor(p, signs.get(entry.getKey()));
                            editors.put(p, editor);
                            found = true;
                        }
                    }
                    if (!found) {
                        p.sendMessage(prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
                    }
                } else if (playersToClick.get(p).equalsIgnoreCase("DELETE")) {
                    String name = null;
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            name = entry.getKey();
                            found = true;
                        }
                    }
                    if (!found) {
                        p.sendMessage(prefix + ChatColor.RED + " This is not a SudoSign!");
                    } else {
                        signs.remove(name);
                        p.sendMessage(prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");
                    }
                } else if (playersToClick.get(p).equalsIgnoreCase("RUN")) {
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            entry.getValue().executeCommands(p);
                            found = true;
                        }
                    }
                    if (!found) {
                        p.sendMessage(prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
                    }
                }

            } else {
                p.sendMessage(prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
                playersToClick.remove(p);
            }
        } else {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        entry.getValue().executeCommands(p);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (e.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlock().getState();
            for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    e.setCancelled(true);
                    //SudoSigns.destroySign(p, entry.getValue());
                    if (p.hasPermission(selectPerm)) {
                        String message = "[\"\",{\"text\":\"[SUDOSIGNS] \",\"color\":\"yellow\"},{\"text\":\"Selected: \",\"color\":\"gray\"},{\"text\":\"" + entry.getKey() + "\",\"bold\":true,\"color\":\"gold\"},{\"text\":\".     \",\"color\":\"gray\"},{\"text\":\"[RUN] \",\"bold\":true,\"color\":\"green\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss run " + entry.getKey() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Run the sign's commands\",\"color\":\"green\"}]}},{\"text\":\"[EDIT] \",\"bold\":true,\"color\":\"light_purple\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss edit " + entry.getKey() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Edit the sign\",\"color\":\"light_purple\"}]}},{\"text\":\"[VIEW] \",\"bold\":true,\"color\":\"blue\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss view " + entry.getKey() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"View the sign's details\",\"color\":\"blue\"}]}},{\"text\":\"[DELETE]\",\"bold\":true,\"color\":\"red\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/ss confirmdelete " + entry.getKey() + "\"},\"hoverEvent\":{\"action\":\"show_text\",\"value\":[\"\",{\"text\":\"Delete the sign\",\"color\":\"red\"}]}}]";
                        p.spigot().sendMessage(ComponentSerializer.parse(message));
                    } else {
                        p.sendMessage(prefix + ChatColor.RED + " You don't have permission to destroy this sign!");
                    }
                }
            }
        }
    }

}


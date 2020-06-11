package dev.mylesmor.sudosigns;

import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

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
                signs.get(playersToCreate.get(p)).setSign(sign);
                SignEditor editor = new SignEditor(p, signs.get(playersToCreate.get(p)));
                editors.put(p, editor);
                config.saveToFile(signs.get(playersToCreate.get(p)), p);
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
                            playersToClick.remove(p);
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
                            playersToClick.remove(p);
                        }
                    }
                    if (!found) {
                        p.sendMessage(prefix + ChatColor.RED + " This is not a SudoSign!");
                    } else {
                        signs.remove(name);
                        config.deleteSign(name);
                        p.sendMessage(prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");
                    }
                } else if (playersToClick.get(p).equalsIgnoreCase("RUN")) {
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            entry.getValue().executeCommands(p);
                            found = true;
                            playersToClick.remove(p);
                        }
                    }
                    if (!found) {
                        p.sendMessage(prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
                    }
                } else if (playersToClick.get(p).equalsIgnoreCase("VIEW")) {
                for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        SudoSign ssign = signs.get(entry.getKey());
                        Location signLoc = ssign.getSign().getLocation();
                        String locString = "x=" + signLoc.getX() + " y=" + signLoc.getY() + " z=" + signLoc.getZ();
                        p.sendMessage(prefix + ChatColor.GRAY + " Displaying details for sign: " + ChatColor.GOLD + entry.getKey() + ChatColor.GRAY + ":");
                        p.sendMessage(prefix + ChatColor.GRAY + " Location: " + ChatColor.LIGHT_PURPLE + locString);
                        p.sendMessage(prefix + ChatColor.GRAY + " Player Commands: " + ChatColor.LIGHT_PURPLE + ssign.getPlayerCommands().size());
                        p.sendMessage(prefix + ChatColor.GRAY + " Console Commands: " + ChatColor.LIGHT_PURPLE + ssign.getConsoleCommands().size());
                        found = true;
                        playersToClick.remove(p);
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
                if (p.hasPermission(selectPerm) && e.getAction() == Action.RIGHT_CLICK_BLOCK || !p.hasPermission(selectPerm)) {
                    Sign sign = (Sign) e.getClickedBlock().getState();
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            entry.getValue().executeCommands(p);
                        }
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
                        String message = Util.getSelectString(p, entry.getKey());
                        p.spigot().sendMessage(ComponentSerializer.parse(message));
                    } else {
                        p.sendMessage(prefix + ChatColor.RED + " You don't have permission to destroy this sign!");
                    }
                }
            }
        } else {
            final BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : faces) {
                BlockState b = e.getBlock().getRelative(face).getState();
                if (b instanceof Sign) {
                    Sign sign = (Sign) b;
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            e.setCancelled(true);
                            p.sendMessage(prefix + ChatColor.RED + " You can't destroy a block that's attached to a SudoSign!");
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent e) {
        ArrayList<Block> blocksToRemove = new ArrayList<>();
        for (Block block : e.blockList()) {
            if (block.getState() instanceof Sign) {
                Sign sign = (Sign) block.getState();
                for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        blocksToRemove.add(block);
                    }
                }
            }
            final BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : faces) {
                BlockState b = block.getRelative(face).getState();
                if (b instanceof Sign) {
                    Sign sign = (Sign) b;
                    for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            blocksToRemove.add(block);
                        }
                    }
                }
            }
        }
        for (Block b : blocksToRemove) {
            e.blockList().remove(b);
        }
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent e) {
        final BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        for (BlockFace face : faces) {
            BlockState b = e.getBlock().getRelative(face).getState();
            if (b instanceof Sign) {
                Sign sign = (Sign) b;
                for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBlockFade(BlockFadeEvent e) {
        final BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
        for (BlockFace face : faces) {
            BlockState b = e.getBlock().getRelative(face).getState();
            if (b instanceof Sign) {
                Sign sign = (Sign) b;
                for (Map.Entry<String, SudoSign> entry : signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}


package dev.mylesmor.sudosigns.listeners;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.SignEditor;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
import org.bukkit.permissions.Permission;

import java.util.ArrayList;
import java.util.Map;


public class SignListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getClickedBlock() != null) {
            SudoUser user = SudoSigns.users.get(p.getUniqueId());
            if (user != null) {
                    Block b = e.getClickedBlock();
                    if (user.isCreate()) {
                        e.setCancelled(true);
                        create(p, user, b);
                    } else if (user.isSelectToCopy()) {
                        e.setCancelled(true);
                        selectToCopy(p, user, b);
                    } else if (user.isCopy()) {
                        e.setCancelled(true);
                        copy(p, user, b);
                    } else if (user.isEdit()) {
                        e.setCancelled(true);
                        edit(p, user, b);
                    } else if (user.isDelete()) {
                        e.setCancelled(true);
                        delete(p, user, b);
                    } else if (user.isRun()) {
                        e.setCancelled(true);
                        runRemote(p, user, b);
                    } else if (user.isView()) {
                        e.setCancelled(true);
                        view(p, user, b);
                    } else {
                        runSign(p, e);
                    }
            } else {
                runSign(p, e);
            }
        }
    }

    private void selectToCopy(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    entry.getValue().executeCommands(p);
                    user.setSelectToCopy(false);
                    p.performCommand("ss copy " + entry.getKey() + " " + user.getPassThru());
                    return;
                }
            }
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " Failed to copy: this is not a SudoSign!");
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
        }
        user.setSelectToCopy(false);
    }

    private void runSign(Player p, PlayerInteractEvent e) {
        if (e.getClickedBlock().getState() instanceof Sign) {
            if (p.hasPermission(Permissions.SELECT) && e.getAction() == Action.RIGHT_CLICK_BLOCK || !p.hasPermission(Permissions.SELECT)) {
                Sign sign = (Sign) e.getClickedBlock().getState();
                for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        entry.getValue().executeCommands(p);
                    }
                }
            }
        }
    }

    private void create(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            SudoSigns.signs.get(user.getPassThru()).setSign(sign);
            SignEditor editor = new SignEditor(p, SudoSigns.signs.get(user.getPassThru()), user);
            user.setEditor(editor);
            SudoSigns.config.saveToFile(SudoSigns.signs.get(user.getPassThru()), true, p);
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
            SudoSigns.signs.remove(user.getPassThru());
        }
        user.setCreate(false);
    }

    private void copy(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign newSign = (Sign) b.getState();
            SudoSigns.signs.get(user.getPassThru()).setSign(newSign);
            SudoSigns.config.saveToFile(SudoSigns.signs.get(user.getPassThru()), true, p);
            p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Sign has been copied to sign " + ChatColor.GOLD + user.getPassThru() + ChatColor.GRAY + " successfully!");
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
            SudoSigns.signs.remove(user.getPassThru());
        }
        user.setCopy(false);
    }

    private void runRemote(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            boolean found = false;
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    entry.getValue().executeCommands(p);
                    found = true;
                }
            }
            if (!found) {
                p.sendMessage(SudoSigns.prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
            }
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
        }
        user.setRun(false);
    }

    private void edit(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            boolean found = false;
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    SignEditor editor = new SignEditor(p, SudoSigns.signs.get(entry.getKey()), user);
                    user.setEditor(editor);
                    found = true;
                }
            }
            if (!found) {
                p.sendMessage(SudoSigns.prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
            }
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
        }
        user.setEdit(false);
    }

    private void delete(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            String name = null;
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    name = entry.getKey();
                }
            }
            if (name == null) {
                p.sendMessage(SudoSigns.prefix + ChatColor.RED + " This is not a SudoSign!");
            } else {
                SudoSigns.signs.remove(name);
                SudoSigns.config.deleteSign(name);
                p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Sign " + ChatColor.GOLD + name + ChatColor.GRAY + " successfully deleted!");
            }
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
        }
        user.setDelete(false);
    }

    private void view(Player p, SudoUser user, Block b) {
        if (b.getState() instanceof Sign) {
            Sign sign = (Sign) b.getState();
            boolean found = false;
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    SudoSign ssign = SudoSigns.signs.get(entry.getKey());
                    Location signLoc = ssign.getSign().getLocation();
                    String locString = "x=" + signLoc.getX() + " y=" + signLoc.getY() + " z=" + signLoc.getZ();
                    p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Displaying details for sign: " + ChatColor.GOLD + entry.getKey() + ChatColor.GRAY + ":");
                    p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Location: " + ChatColor.LIGHT_PURPLE + locString);
                    p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Player Commands: " + ChatColor.LIGHT_PURPLE + ssign.getPlayerCommands().size());
                    p.sendMessage(SudoSigns.prefix + ChatColor.GRAY + " Console Commands: " + ChatColor.LIGHT_PURPLE + ssign.getConsoleCommands().size());
                    found = true;
                }
            }
            if (!found) {
                p.sendMessage(SudoSigns.prefix + ChatColor.RED + " This is not a SudoSign. Use " + ChatColor.GRAY + "/ss create" + ChatColor.RED + " instead.");
            }
        } else {
            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " A sign wasn't clicked! Cancelling...");
        }
        user.setView(false);
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent e) {
        Player p = e.getPlayer();
        if (e.getBlock().getState() instanceof Sign) {
            Sign sign = (Sign) e.getBlock().getState();
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().getSign().equals(sign)) {
                    e.setCancelled(true);
                    if (p.hasPermission(Permissions.SELECT)) {
                        String message = Util.getSelectString(p, entry.getKey());
                        p.spigot().sendMessage(ComponentSerializer.parse(message));
                    } else {
                        p.sendMessage(SudoSigns.prefix + ChatColor.RED + " You don't have permission to destroy this sign!");
                    }
                }
            }
        } else {
            final BlockFace[] faces = {BlockFace.UP, BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
            for (BlockFace face : faces) {
                BlockState b = e.getBlock().getRelative(face).getState();
                if (b instanceof Sign) {
                    Sign sign = (Sign) b;
                    for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                        if (entry.getValue().getSign().equals(sign)) {
                            e.setCancelled(true);
                            p.sendMessage(SudoSigns.prefix + ChatColor.RED + " You can't destroy a block that's attached to a SudoSign!");
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
                for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
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
                    for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
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
                for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
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
                for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                    if (entry.getValue().getSign().equals(sign)) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

}


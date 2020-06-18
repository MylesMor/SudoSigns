package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;

import static dev.mylesmor.sudosigns.SudoSigns.*;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (users.containsKey(p.getUniqueId())) {
            SudoUser user = users.get(p.getUniqueId());
            if (user.isEditing()) {
                SignEditor editor = user.getEditor();
                if (e.getCurrentItem() != null) {
                    if (editor.getCurrentPage().equalsIgnoreCase("MAIN")) {
                        if (e.getCurrentItem().getType() == Material.BOOK) {
                            editor.prepareRename();
                        } else if (e.getCurrentItem().getType() == Material.OAK_SIGN) {
                            editor.editSignText();
                        } else if (e.getCurrentItem().getType() == Material.BARRIER) {
                            editor.goToPermissions();
                        } else if (e.getCurrentItem().getType() == Material.COMMAND_BLOCK) {
                            editor.goToCommands();
                        }
                    } else if (editor.getCurrentPage().equalsIgnoreCase("COMMANDS")) {
                        if (e.getView().getTitle().equalsIgnoreCase("Player or Console command?")) {
                            String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                            if (itemName.equalsIgnoreCase("Player Command")) {
                                editor.chooseCommandType(PlayerInput.PLAYER_COMMAND);
                            } else if (itemName.equalsIgnoreCase("Console Command")) {
                                editor.chooseCommandType(PlayerInput.CONSOLE_COMMAND);
                            }
                        }
                        if (e.getCurrentItem().getType() == Material.WRITABLE_BOOK) {
                            editor.prepareCommand();
                        } else if (e.getCurrentItem().getType() == Material.BOOK) {
                            editor.deleteCommand(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()));
                            e.setCancelled(true);
                        }
                    } else if (editor.getCurrentPage().equalsIgnoreCase("PERMISSIONS")) {
                        if (e.getCurrentItem().getType() == Material.WRITABLE_BOOK) {
                            editor.preparePermission();
                        }
                        if (e.getView().getTitle().equalsIgnoreCase("Default or Custom?")) {
                            String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                            if (itemName.equalsIgnoreCase("Custom Permission")) {
                                editor.addPermission(false, null);
                            } else if (itemName.equalsIgnoreCase("Default Permission")) {
                                editor.addPermission(true, null);
                            }
                        }
                    }
                    if (e.getCurrentItem().getType() == Material.ARROW) {
                        editor.goToMain();
                    }
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (users.containsKey(p.getUniqueId())) {
            SudoUser user = users.get(p.getUniqueId());
            if (user.isEditing()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(sudoSignsPlugin, () -> {
                    InventoryView currentInv = p.getOpenInventory();
                    if (currentInv.getTitle().equalsIgnoreCase("CRAFTING") && !user.isTextInput()) {
                        user.getEditor().endEditor();
                        user.removeEditor();
                    }
                }, 5L);
            }
        }
    }

}

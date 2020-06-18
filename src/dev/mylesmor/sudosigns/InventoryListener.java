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

/**
 * InventoryListener class to listen for GUI actions.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (users.containsKey(p.getUniqueId())) {
            SudoUser user = users.get(p.getUniqueId());
            if (user.isEditing()) {
                SignEditor editor = user.getEditor();
                if (e.getCurrentItem() != null) {
                    Material m = e.getCurrentItem().getType();
                    String invName = ChatColor.stripColor(e.getView().getTitle());
                    String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
                    if (editor.getCurrentPage().equalsIgnoreCase("MAIN")) {
                        checkForMainMenuClicks(editor, m);
                    } else if (editor.getCurrentPage().equalsIgnoreCase("COMMANDS")) {
                        checkForCommandsClicks(editor, m, invName, itemName);
                    } else if (editor.getCurrentPage().equalsIgnoreCase("PERMISSIONS")) {
                        checkForPermissionsClicks(editor, m, invName, itemName);
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
            // Checks whether the user has closed the GUI.
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

    /**
     * Checks for GUI clicks in the main menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     */
    public void checkForMainMenuClicks(SignEditor editor, Material m) {
        if (m == Material.BOOK) {
            editor.prepareRename();
        } else if (m == Material.OAK_SIGN) {
            editor.editSignText();
        } else if (m == Material.BARRIER) {
            editor.goToPermissions();
        } else if (m == Material.COMMAND_BLOCK) {
            editor.goToCommands();
        }
    }

    /**
     * Checks for GUI clicks in the Commands menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param invName The name of the inventory.
     * @param itemName The name of the item clicked.
     */
    public void checkForCommandsClicks(SignEditor editor, Material m, String invName, String itemName) {
        if (invName.equalsIgnoreCase("Player or Console command?")) {
            if (itemName.equalsIgnoreCase("Player Command")) {
                editor.chooseCommandType(PlayerInput.PLAYER_COMMAND);
            } else if (itemName.equalsIgnoreCase("Console Command")) {
                editor.chooseCommandType(PlayerInput.CONSOLE_COMMAND);
            }
        }
        if (m == Material.WRITABLE_BOOK) {
            editor.prepareCommand();
        } else if (m == Material.BOOK) {
            editor.deleteCommand(ChatColor.stripColor(itemName));
        }
    }

    /**
     * Checks for GUI clicks in the Permissions menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param invName The name of the inventory.
     * @param itemName The name of the item clicked.
     */
    public void checkForPermissionsClicks(SignEditor editor, Material m, String invName, String itemName) {
        if (invName.equalsIgnoreCase("Default or Custom?")) {
            if (itemName.equalsIgnoreCase("Custom Permission")) {
                editor.addPermission(false, null);
            } else if (itemName.equalsIgnoreCase("Default Permission")) {
                editor.addPermission(true, null);
            }
        }
        if (m == Material.BOOK) {
            editor.deletePermission(ChatColor.stripColor(itemName));
        }
        if (m == Material.WRITABLE_BOOK) {
            editor.preparePermission();
        }
    }

}

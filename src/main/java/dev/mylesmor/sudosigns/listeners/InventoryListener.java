package dev.mylesmor.sudosigns.listeners;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.menus.SignEditor;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;


/**
 * InventoryListener class to listen for GUI actions.
 * @author MylesMor
 * @author https://mylesmor.dev
 */
public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        SudoUser user = SudoSigns.users.get(p.getUniqueId());
        if (user != null) {
            if (user.isEditing()) {
                if (Objects.equals(e.getClickedInventory(), e.getView().getTopInventory())) {
                    SignEditor editor = user.getEditor();
                    if (e.getCurrentItem() != null) {
                        e.setCancelled(true);
                        Material m = e.getCurrentItem().getType();
                        String itemName = ChatColor.stripColor(Objects.requireNonNull(e.getCurrentItem().getItemMeta()).getDisplayName());
                        switch (editor.getCurrentPage()) {
                            case MAIN:
                                checkForMainMenuClicks(editor, m);
                                break;
                            case COMMANDS:
                                checkForCommandsClicks(p, editor, m, e.getCurrentItem());
                                break;
                            case PERMISSIONS:
                                checkForPermissionsClicks(p, editor, m, e.getCurrentItem());
                                break;
                            case CHOOSE_COMMAND:
                                chooseCommandType(editor, m, itemName);
                                break;
                            case CHOOSE_PERMISSION:
                                choosePermissionType(editor, m);
                                break;
                            case MESSAGES:
                                checkForMessagesClicks(p, editor, m, e.getCurrentItem());
                                break;
                            case COMMAND_OPTIONS:
                                checkForCommandOptionClicks(p, editor, m, itemName);
                                break;
                            case MESSAGE_OPTIONS:
                                checkForMessageOptionClicks(p, editor, m, itemName);
                                break;

                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        SudoUser user = SudoSigns.users.get(p.getUniqueId());
        if (user != null) {
            // Checks whether the user has closed the GUI.
            if (user.isEditing()) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(SudoSigns.sudoSignsPlugin, () -> {
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
        if (SudoSigns.version.contains("1.13")) {
            if (m.equals(Material.valueOf("SIGN"))) {
                editor.goToMessages();
                return;
            }
        }
        switch (m) {
            case NAME_TAG:
                editor.prepareRename();
                break;
            case WRITABLE_BOOK:
                editor.editSignNumber();
                break;
            case BARRIER:
                editor.goToPermissions();
                break;
            case COMMAND_BLOCK:
                editor.goToCommands();
                break;
            case BIRCH_SIGN:
                editor.goToMessages();
                break;
            case GOLD_NUGGET:
                editor.getMainMenu().prepareSetPrice();
                break;
        }
    }

    /**
     * Checks for GUI clicks in the Command Options menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param item The ItemStack of the clicked item.
     */
    public void checkForCommandsClicks(Player p, SignEditor editor, Material m, ItemStack item) {
        switch (m) {
            case WRITABLE_BOOK:
                editor.getCommandsMenu().prepareCommand();
                break;
            case BOOK:
                if (p.hasPermission(Permissions.COMMAND_OPTIONS)) {
                    editor.goToCommandOptionsMenu(item);
                }
                break;
            case ARROW:
                editor.goToMain();
                break;
        }
    }

    /**
     * Checks for GUI clicks in the Command Options menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param itemName The name of the item clicked.
     */
    public void checkForCommandOptionClicks(Player p, SignEditor editor, Material m, String itemName) {
        switch (m) {
            case CLOCK:
                editor.getCommandOptionsMenu().addDelay();
                break;
            case BARRIER:
                if (p.hasPermission(Permissions.DELETE_COMMAND)) {
                    editor.getCommandOptionsMenu().deleteCommand();
                }
                break;
            case ARROW:
                editor.goToCommands();
                break;
        }
    }

    /**
     * Checks for GUI clicks in the Commands menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param itemName The name of the item clicked.
     */
    public void checkForMessageOptionClicks(Player p, SignEditor editor, Material m, String itemName) {
        switch (m) {
            case CLOCK:
                editor.getMessageOptionsMenu().addDelay();
                break;
            case BARRIER:
                if (p.hasPermission(Permissions.DELETE_MESSAGE)) {
                    editor.getMessageOptionsMenu().deleteMessage();
                }
                break;
            case ARROW:
                editor.goToMessages();
                break;
        }
    }

    /**
     * Checks for GUI clicks in the Commands menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param item The ItemStack of the clicked item.
     */
    public void checkForMessagesClicks(Player p, SignEditor editor, Material m, ItemStack item) {
        switch (m) {
            case WRITABLE_BOOK:
                editor.getMessagesMenu().prepareMessage();
                break;
            case BOOK:
                if (p.hasPermission(Permissions.MESSAGE_OPTIONS)) {
                    editor.goToMessageOptionsMenu(item);
                }
                break;
            case ARROW:
                editor.goToMain();
                break;
        }
    }

    public void choosePermissionType(SignEditor editor, Material m) {
        switch (m) {
            case PLAYER_HEAD:
                editor.getPermMenu().addPermission(true, null);
                break;
            case COMMAND_BLOCK:
                editor.getPermMenu().addPermission(false, null);
                break;
            case ARROW:
                editor.goToPermissions();
                break;
        }
    }

    public void chooseCommandType(SignEditor editor, Material m, String itemName) {
        switch (m) {
            case PLAYER_HEAD:
                editor.getCommandsMenu().chooseCommandType(PlayerInput.PLAYER_COMMAND);
                break;
            case COMMAND_BLOCK:
                editor.getCommandsMenu().chooseCommandType(PlayerInput.CONSOLE_COMMAND);
                break;
            case ARROW:
                editor.goToCommands();
                break;
        }
    }




    /**
     * Checks for GUI clicks in the Permissions menu.
     * @param editor The SignEditor class of the particular user.
     * @param m The material clicked on in the menu.
     * @param item The ItemStack of the item clicked.
     */
    public void checkForPermissionsClicks(Player p, SignEditor editor, Material m, ItemStack item) {
        switch (m) {
            case BOOK:
                if (p.hasPermission(Permissions.DELETE_PERMISSION)) {
                    ItemMeta itemMeta = item.getItemMeta();
                    NamespacedKey key = new NamespacedKey(SudoSigns.sudoSignsPlugin, "permission");
                    if (itemMeta != null) {
                        editor.getPermMenu().deletePermission(itemMeta.getPersistentDataContainer().get(key, PersistentDataType.STRING));
                    }
                }
                break;
            case WRITABLE_BOOK:
                editor.getPermMenu().preparePermission();
                break;
            case ARROW:
                editor.goToMain();
        }
    }
}

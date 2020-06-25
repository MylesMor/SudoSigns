package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PermissionsMenu {

    private final GUIPage PAGE = GUIPage.PERMISSIONS;

    private Inventory menu;
    private Player p;
    private SudoSign sign;
    private SignEditor editor;
    private SudoUser su;

    public PermissionsMenu(SudoUser su, Player p, SudoSign sign, SignEditor editor) {
        this.su = su;
        this.sign = sign;
        this.editor = editor;
        this.p = p;
    }

    public void goToPermissionsMenu() {
        createPermissionsMenu();
        p.openInventory(menu);
        editor.setCurrentPage(PAGE);
    }

    private void createPermissionsMenu() {

        menu = Bukkit.createInventory(p, 45, "Permissions: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Permissions");
        barrier.setItemMeta(barrierMeta);

        if (p.hasPermission(Permissions.ADD_PERMISSION)) {
            ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta bqMeta = bookQuill.getItemMeta();
            bqMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GREEN + "Add new permission");
            bookQuill.setItemMeta(bqMeta);
            menu.setItem(40, bookQuill);
        }


        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.RED + "Click to delete!");

        int i = 1;
        for (String perm : sign.getPermissions()) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + perm);
            if (p.hasPermission(Permissions.DELETE_PERMISSION)) {
                lore.add("");
                bookMeta.setLore(lore);
            }
            book.setItemMeta(bookMeta);
            if (i == 9 || i == 18 || i == 27) i++;
            menu.setItem(i, book);
            i++;
        }

        menu.setItem(0, barrier);
        menu.setItem(9, barrier);
        menu.setItem(18, barrier);
        menu.setItem(27, barrier);
        menu.setItem(36, arrow);
    }


    public void preparePermission() {
        Inventory choiceInv = Bukkit.createInventory(p, 45, "Default or Custom?");
        for (int i = 0; i < choiceInv.getSize(); i++) {
            choiceInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GOLD + "sudosigns.sign." + sign.getName());
        headMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Default Permission");
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Custom Permission");
        cmdBlock.setItemMeta(cmdBlockMeta);

        choiceInv.setItem(21, head);
        choiceInv.setItem(23, cmdBlock);
        choiceInv.setItem(36, arrow);

        editor.setCurrentPage(GUIPage.CHOOSE_PERMISSION);
        p.openInventory(choiceInv);
    }

    public void addPermission(boolean provided, String perm) {
        if (provided) {
            if (perm == null) {
                sign.addPermission("sudosigns.sign." + sign.getName());
                SudoSigns.config.addPermissionToConfig(sign, "sudosigns.sign." + sign.getName());
                goToPermissionsMenu();
            } else {
                SudoSigns.config.addPermissionToConfig(sign, perm);
                sign.addPermission(perm);
            }
        } else {
            p.closeInventory();
            p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter in chat the permission which the player must have to run this sign or type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
            su.addTextInput(PlayerInput.PERMISSION);
        }
    }

    public void deletePermission(String perm) {
        sign.removePermission(perm);
        SudoSigns.config.deletePermissionFromConfig(sign, perm);
        goToPermissionsMenu();
    }

}

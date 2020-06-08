package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SignEditor {

    private Player p;
    private SudoSign sign;
    private Inventory mainInv;
    private Inventory permissionsInv;
    private Inventory commandsInv;
    private String currentPage;

    SignEditor(Player p, SudoSign s) {
        this.p = p;
        this.sign = s;
        currentPage = "MAIN";
        createMainMenu();
        p.openInventory(mainInv);
    }

    private void createMainMenu() {
        mainInv = Bukkit.createInventory(p, 45, "Editing: " + sign.getName());
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Rename Sign");
        book.setItemMeta(bookMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Permissions");
        barrier.setItemMeta(barrierMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Commands");
        cmdBlock.setItemMeta(cmdBlockMeta);

        mainInv.setItem(21, book);
        mainInv.setItem(22, barrier);
        mainInv.setItem(23, cmdBlock);
    }

    private void createPermissionsMenu() {
        permissionsInv = Bukkit.createInventory(p, 45, "Permissions: " + sign.getName());

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        permissionsInv.setItem(37, arrow);
    }

    public void goToPermissions() {
        currentPage = "PERMISSIONS";
        if (permissionsInv == null) {
            createPermissionsMenu();
        }
        p.openInventory(permissionsInv);


    }

    public void endEditor() {
        p.sendMessage(SudoSigns.prefix + ChatColor.GREEN + " Changes saved to " + ChatColor.GOLD + sign.getName() + ChatColor.GREEN + ".");
    }

    private void createCommandsMenu() {
        commandsInv = Bukkit.createInventory(p, 45, "Permissions: " + sign.getName());
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("BACK");
        arrow.setItemMeta(arrowMeta);

        commandsInv.setItem(37, arrow);
    }

    public void goToCommands() {
        currentPage = "COMMANDS";
        if (commandsInv == null) {
            createCommandsMenu();
        }
        p.openInventory(commandsInv);
    }

    public void renameSign() {

    }

    public void goToMain() {
        p.openInventory(mainInv);
        currentPage = "MAIN";
    }

    public String getCurrentPage() {
        return currentPage;
    }


}

package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.data.SudoSign;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MainMenu {

    private final GUIPage PAGE = GUIPage.MAIN;

    private Inventory menu;
    private SudoSign sign;
    private SignEditor editor;
    private Player p;

    public MainMenu(Player p, SudoSign sign, SignEditor editor) {
        this.sign = sign;
        this.editor = editor;
        this.p = p;
    }

    public void goToMainMenu() {
        createMainMenu();
        p.openInventory(menu);
        editor.setCurrentPage(PAGE);

    }

    private void createMainMenu() {
        menu = Bukkit.createInventory(p, 45, "Editing: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
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

        menu.setItem(20, book);
        //mainInv.setItem(21, editSign);
        menu.setItem(22, barrier);
        menu.setItem(24, cmdBlock);
    }
}

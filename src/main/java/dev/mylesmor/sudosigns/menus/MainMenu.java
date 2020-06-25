package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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
        ArrayList<ItemStack> items = new ArrayList<>();
        menu = Bukkit.createInventory(p, 45, "Editing: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        if (p.hasPermission(Permissions.RENAME)) {
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Rename Sign");
            book.setItemMeta(bookMeta);
            items.add(book);
        }

        if (p.hasPermission(Permissions.VIEW_PERMISSION)) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Permissions");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Players must have all permissions");
            lore.add(ChatColor.YELLOW + "listed in this section to be able");
            lore.add(ChatColor.YELLOW + "to use the sign.");
            barrierMeta.setLore(lore);
            barrier.setItemMeta(barrierMeta);
            items.add(barrier);
        }

        if (p.hasPermission(Permissions.VIEW_COMMAND)) {
            ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
            ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Commands listed here will be executed");
            lore.add(ChatColor.YELLOW + "when a player uses the sign.");
            cmdBlockMeta.setLore(lore);
            cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Commands");
            cmdBlock.setItemMeta(cmdBlockMeta);
            items.add(cmdBlock);
        }

        if (p.hasPermission(Permissions.VIEW_MESSAGE)) {
            ItemStack signBlock = new ItemStack(Material.BIRCH_SIGN);
            ItemMeta signMeta = signBlock.getItemMeta();
            signMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Messages");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Messages listed here will be shown");
            lore.add(ChatColor.YELLOW + "to the player when they use the sign.");
            signMeta.setLore(lore);
            signBlock.setItemMeta(signMeta);
            items.add(signBlock);
        }

        switch (items.size()) {
            case 4:
                menu.setItem(11, items.get(0));
                menu.setItem(13, items.get(1));
                menu.setItem(15, items.get(2));
                menu.setItem(29, items.get(3));
                break;
            case 3:
                menu.setItem(20, items.get(0));
                menu.setItem(22, items.get(1));
                menu.setItem(24, items.get(2));
                break;
            case 2:
                menu.setItem(21, items.get(0));
                menu.setItem(23, items.get(1));
                break;
            case 1:
                menu.setItem(22, items.get(0));
                break;
        }
    }
}

package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.data.SignCommand;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.data.SudoUser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MessagesMenu {

    private final GUIPage PAGE = GUIPage.MESSAGES;

    private Inventory menu;
    private Player p;
    private SudoSign sign;
    private SignEditor editor;
    private SudoUser su;

    public MessagesMenu(SudoUser su, Player p, SudoSign sign, SignEditor editor) {
        this.su = su;
        this.sign = sign;
        this.editor = editor;
        this.p = p;
    }

    public void goToMessagesMenu() {
        createMessagesMenu();
        p.openInventory(menu);
        editor.setCurrentPage(PAGE);
    }

    private void createMessagesMenu() {
        menu = Bukkit.createInventory(p, 45, "Messages: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta bqMeta = bookQuill.getItemMeta();
        bqMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GREEN + "Add new message");
        bookQuill.setItemMeta(bqMeta);

        ItemStack signItem = new ItemStack(Material.BIRCH_SIGN);
        ItemMeta signMeta = signItem.getItemMeta();
        signMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Messages");
        signItem.setItemMeta(signMeta);

        List<String> lore = new ArrayList<>();

        int i = 1;
        for (String message : sign.getMessages()) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName(ChatColor.RED + "Click to delete!");
            lore.clear();
            lore.add(ChatColor.translateAlternateColorCodes('&', message));
            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            if (i == 9 || i == 18 || i == 27) i++;
            menu.setItem(i, book);
            i++;
        }

        menu.setItem(0, signItem);
        menu.setItem(9, signItem);
        menu.setItem(18, signItem);
        menu.setItem(27, signItem);
        menu.setItem(36, arrow);
        menu.setItem(40, bookQuill);
    }

    
}

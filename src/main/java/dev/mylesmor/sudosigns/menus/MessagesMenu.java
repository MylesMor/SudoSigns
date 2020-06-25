package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SignCommand;
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

        if (p.hasPermission(Permissions.ADD_MESSAGE)) {
            ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta bqMeta = bookQuill.getItemMeta();
            bqMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GREEN + "Add new message");
            bookQuill.setItemMeta(bqMeta);
            menu.setItem(40, bookQuill);
        }

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
            if (p.hasPermission(Permissions.DELETE_MESSAGE)) {
                bookMeta.setDisplayName(ChatColor.RED + "Click to delete!");
            } else {
                bookMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Message:");
            }
            lore.clear();
            lore.add("" + ChatColor.RESET + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', message));
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
    }

    public void addMessage(String message) {
        SudoSigns.config.addMessageToConfig(sign, message);
        sign.addMessage(message);
    }

    public void prepareMessage() {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter in chat the message which will be shown. Use the '&' symbol for colour codes and %PLAYER% for the player who clicked the sign.. To cancel type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(PlayerInput.MESSAGE);
    }

    public void deleteMessage(String message) {
        String found = null;
        for (String m : sign.getMessages()) {
            if (message.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', m))) {
                found = m;
            }
        }
        sign.removeMessage(found);
        SudoSigns.config.deleteMessageFromConfig(sign, found);
        goToMessagesMenu();
    }

    
}

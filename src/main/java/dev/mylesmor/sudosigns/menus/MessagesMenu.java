package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.*;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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

        ItemStack signItem = null;
        if (SudoSigns.version.contains("1.13")) {
            signItem = new ItemStack(Material.valueOf("SIGN"));
        } else {
            signItem = new ItemStack(Material.BIRCH_SIGN);
        }        ItemMeta signMeta = signItem.getItemMeta();
        signMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Messages");
        signItem.setItemMeta(signMeta);

        List<String> lore = new ArrayList<>();

        NamespacedKey key = new NamespacedKey(SudoSigns.sudoSignsPlugin, "message-number");

        ArrayList<SignMessage> orderedSignMessages = sign.getMessages();
        orderedSignMessages.sort(Comparator.comparing(SignMessage::getDelay));

        int i = 1;
        for (SignMessage sm : orderedSignMessages) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            lore.clear();
            bookMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Message:");
            lore.add("" + ChatColor.RESET + ChatColor.WHITE + ChatColor.translateAlternateColorCodes('&', sm.getMessage()));
            lore.add("");
            lore.add("");
            lore.add(ChatColor.GRAY + "Delay: " + ChatColor.RED + (sm.getDelay() / 1000) + "s");
            if (p.hasPermission(Permissions.MESSAGE_OPTIONS)) {
                lore.add("");
                lore.add(ChatColor.GREEN + "Click for options!");
            }
            bookMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, sm.getNumber());
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
        SignMessage signMessage = new SignMessage(sign.getNextMessageNumber(), message, 0, PlayerInput.MESSAGE);
        SudoSigns.config.addMessage(sign, signMessage);
        sign.addMessage(signMessage);
    }

    public void prepareMessage() {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter in chat the message which will be shown. Use the " + ChatColor.GOLD + "& " + ChatColor.GRAY + "symbol for chat colour codes and the phrase" + ChatColor.GOLD + " %PLAYER% " + ChatColor.GRAY + "for the player who clicked the sign. To cancel type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(PlayerInput.MESSAGE);
    }

    
}

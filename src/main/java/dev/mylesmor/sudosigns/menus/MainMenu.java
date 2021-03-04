package dev.mylesmor.sudosigns.menus;

import dev.mylesmor.sudosigns.SudoSigns;
import dev.mylesmor.sudosigns.data.PlayerInput;
import dev.mylesmor.sudosigns.data.SudoSign;
import dev.mylesmor.sudosigns.util.Permissions;
import dev.mylesmor.sudosigns.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
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
    private int lineNumber = 0;

    public MainMenu(Player p, SudoSign sign, SignEditor editor) {
        this.sign = sign;
        this.editor = editor;
        this.p = p;
    }

    public void goToMainMenu() {
        createMainMenu();
        p.openInventory(menu);
    }

    private void createMainMenu() {
        ArrayList<ItemStack> items = new ArrayList<>();
        menu = Bukkit.createInventory(p, 45, "Editing: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        if (p.hasPermission(Permissions.RENAME)) {
            ItemStack nametag = new ItemStack(Material.NAME_TAG);
            ItemMeta ntMeta = nametag.getItemMeta();
            ntMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Rename Sign");
            nametag.setItemMeta(ntMeta);
            items.add(nametag);
        }

        if (p.hasPermission(Permissions.EDIT_TEXT)) {
            ItemStack book = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Edit Sign Text");
            book.setItemMeta(bookMeta);
            items.add(book);
        }

        if (p.hasPermission(Permissions.VIEW_PERMISSION)) {
            ItemStack barrier = new ItemStack(Material.BARRIER);
            ItemMeta barrierMeta = barrier.getItemMeta();
            barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Permissions");
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
            cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Commands");
            cmdBlock.setItemMeta(cmdBlockMeta);
            items.add(cmdBlock);
        }

        if (p.hasPermission(Permissions.VIEW_MESSAGE)) {
            ItemStack signBlock;
            if (SudoSigns.version.contains("1.13")) {
                signBlock = new ItemStack(Material.valueOf("SIGN"));
            } else {
                signBlock = new ItemStack(Material.BIRCH_SIGN);
            }
            ItemMeta signMeta = signBlock.getItemMeta();
            signMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD +  "Messages");
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.YELLOW + "Messages listed here will be shown");
            lore.add(ChatColor.YELLOW + "to the player when they use the sign.");
            signMeta.setLore(lore);
            signBlock.setItemMeta(signMeta);
            items.add(signBlock);
        }

        if (SudoSigns.econ != null) {
            if (p.hasPermission(Permissions.VIEW_PRICE)) {
                if (SudoSigns.econ != null) {
                    ItemStack goldNugget = new ItemStack(Material.GOLD_NUGGET);
                    ItemMeta goldNuggetItemMeta = goldNugget.getItemMeta();
                    List<String> lore = new ArrayList<>();
                    lore.add(ChatColor.YELLOW + "Set the price to use this sign.");
                    lore.add(ChatColor.YELLOW + "");
                    if (sign.getPrice() == 1.0) {
                        lore.add("" + ChatColor.GRAY + ChatColor.BOLD + "Price: " + ChatColor.GRAY + SudoSigns.econ.currencyNameSingular() + ChatColor.RED + sign.getPrice());
                    } else {
                        lore.add("" + ChatColor.GRAY + ChatColor.BOLD + "Price: " + ChatColor.GRAY + SudoSigns.econ.currencyNamePlural() + ChatColor.RED + sign.getPrice());
                    }
                    if (p.hasPermission(Permissions.SET_PRICE)) {
                        lore.add(ChatColor.YELLOW + "");
                        lore.add("");
                        lore.add(ChatColor.GREEN + "Click to edit!");
                    }
                    goldNuggetItemMeta.setLore(lore);
                    goldNuggetItemMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + ChatColor.BOLD + "Price");
                    goldNugget.setItemMeta(goldNuggetItemMeta);
                    items.add(goldNugget);
                }
            }
        }


        switch (items.size()) {
            case 6:
                menu.setItem(11, items.get(0));
                menu.setItem(13, items.get(1));
                menu.setItem(15, items.get(2));
                menu.setItem(29, items.get(3));
                menu.setItem(31, items.get(4));
                menu.setItem(33, items.get(5));
            case 5:
                menu.setItem(11, items.get(0));
                menu.setItem(13, items.get(1));
                menu.setItem(15, items.get(2));
                menu.setItem(29, items.get(3));
                menu.setItem(31, items.get(4));
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

    public void prepareSetPrice() {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the price to use the sign! Type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + " to cancel.");
        SudoSigns.users.get(p.getUniqueId()).addTextInput(PlayerInput.SET_PRICE);
    }

    public void setPrice(double price) {
        sign.setPrice(price);
        SudoSigns.config.setPrice(sign.getName(), price);
        if (sign.getPrice() == 1.0) {
            p.sendMessage(Util.prefix + ChatColor.GREEN + " The price of " + ChatColor.GRAY + sign.getName() + " has been set to: " + ChatColor.GOLD + SudoSigns.econ.currencyNameSingular() + price + ChatColor.GREEN + ".");
        } else {
            p.sendMessage(Util.prefix + ChatColor.GREEN + " The price of " + ChatColor.GRAY + sign.getName() + " has been set to: " + ChatColor.GOLD + SudoSigns.econ.currencyNamePlural() + price + ChatColor.GREEN + ".");
        }
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the new text (maximum 15 characters), using " + ChatColor.GOLD + "& " + ChatColor.GRAY + "for colour codes. Type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + " to cancel.");
        SudoSigns.users.get(p.getUniqueId()).addTextInput(PlayerInput.EDIT_TEXT);
    }

    public void setText(String message) {
        p.sendMessage(Util.prefix + ChatColor.GREEN + " Line " + lineNumber + " has been set to: " + ChatColor.translateAlternateColorCodes('&', message));
        sign.editLine(lineNumber-1, message);
        SudoSigns.config.editSignText(sign.getName(), lineNumber, message);
    }
}

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

public class CommandsMenu {

    private final GUIPage PAGE = GUIPage.COMMANDS;

    private Inventory menu;
    private Player p;
    private SudoSign sign;
    private SignEditor editor;
    private SudoUser su;

    public CommandsMenu(SudoUser su, Player p, SudoSign sign, SignEditor editor) {
        this.su = su;
        this.sign = sign;
        this.editor = editor;
        this.p = p;
    }

    public void goToCommandsMenu() {
        createCommandsMenu();
        p.openInventory(menu);
    }

    private void createCommandsMenu() {
        menu = Bukkit.createInventory(p, 45, "Commands: " + sign.getName());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        if (p.hasPermission(Permissions.ADD_COMMAND)) {
            ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
            ItemMeta bqMeta = bookQuill.getItemMeta();
            bqMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GREEN + "Add new command");
            bookQuill.setItemMeta(bqMeta);
            menu.setItem(40, bookQuill);
        }

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Console Commands");
        cmdBlock.setItemMeta(cmdBlockMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Player Commands");
        head.setItemMeta(headMeta);

        List<String> lore = new ArrayList<>();
        NamespacedKey key = new NamespacedKey(SudoSigns.sudoSignsPlugin, "command-number");

        ArrayList<SignCommand> orderedSignCommands = sign.getPlayerCommands();
        orderedSignCommands.sort(Comparator.comparing(SignCommand::getDelay));

        // Populates spaces with commands.
        int i = 1;
        for (SignCommand sc : orderedSignCommands) {
            if (i > 26) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            lore.add(ChatColor.YELLOW + "Player Command");
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + "/" + sc.getCommand());
            lore.add(ChatColor.GRAY + "Delay: " + ChatColor.RED + (sc.getDelay() / 1000) + "s");
            if (p.hasPermission(Permissions.COMMAND_OPTIONS)) {
                lore.add("");
                lore.add(ChatColor.GREEN + "Click for options!");
            }
            bookMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, sc.getNumber());
            bookMeta.setLore(lore);
            lore.clear();
            book.setItemMeta(bookMeta);
            if (i == 9) i++;
            menu.setItem(i, book);
            i++;
        }

        orderedSignCommands = sign.getConsoleCommands();
        orderedSignCommands.sort(Comparator.comparing(SignCommand::getDelay));

        i = 19;
        for (SignCommand sc : orderedSignCommands) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            lore.add(ChatColor.YELLOW + "Console Command");
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + "/" + sc.getCommand());
            lore.add(ChatColor.GRAY + "Delay: " + ChatColor.RED + (sc.getDelay() / 1000) + "s");
            if (p.hasPermission(Permissions.COMMAND_OPTIONS)) {
                lore.add("");
                lore.add(ChatColor.GREEN + "Click for options!");
            }
            bookMeta.getPersistentDataContainer().set(key, PersistentDataType.INTEGER, sc.getNumber());
            bookMeta.setLore(lore);
            lore.clear();
            book.setItemMeta(bookMeta);
            if (i == 27) i++;
            menu.setItem(i, book);
            i++;
        }

        menu.setItem(0, head);
        menu.setItem(9, head);
        menu.setItem(18, cmdBlock);
        menu.setItem(27, cmdBlock);

        menu.setItem(36, arrow);
    }

    public void prepareCommand() {
        Inventory choiceInv = Bukkit.createInventory(p, 45, "Player or Console command?");
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
        lore.add(ChatColor.RED + "The player must have permission to run the command!");
        headMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Player Command");
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Console Command");
        cmdBlock.setItemMeta(cmdBlockMeta);

        if (p.hasPermission(Permissions.CONSOLE_COMMAND)) {
            choiceInv.setItem(21, head);
            choiceInv.setItem(23, cmdBlock);
        } else {
            choiceInv.setItem(22, head);
        }
        choiceInv.setItem(36, arrow);

        editor.setCurrentPage(GUIPage.CHOOSE_COMMAND);
        p.openInventory(choiceInv);
    }


    public void chooseCommandType(PlayerInput type) {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the full command in chat with the beginning /. The phrase" + ChatColor.GOLD + " %PLAYER%" + ChatColor.GRAY +
                " will be replaced with the player who clicked the sign. To cancel, type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(type);
    }

    public void addCommand(String cmd, PlayerInput type) {
        SignCommand command = new SignCommand(sign.getNextCommandNumber(), cmd, 0, type);
        switch (type) {
            case CONSOLE_COMMAND:
                sign.addConsoleCommand(command);
                break;
            case PLAYER_COMMAND:
                sign.addPlayerCommand(command);
                break;
        }
        SudoSigns.config.addCommand(sign, command, type);
        Util.sudoSignsMessage(p, ChatColor.GRAY, "Command added successfully!", null);
        editor.goToCommands();
    }

}

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
        editor.setCurrentPage(PAGE);

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

        boolean anyOpPermissions = false;

        // Populates spaces with commands.
        int i = 1;
        for (Map.Entry<SignCommand, Boolean> entry : sign.getPlayerCommands().entrySet()) {
            SignCommand sc = entry.getKey();
            if (i > 26) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            if (entry.getValue()) {
                lore.add(ChatColor.YELLOW + "Player Command with Permissions (player is granted OP for the duration of this command).");
            } else {
                lore.add(ChatColor.YELLOW + "Player Command");
            }
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + "/" + sc.getCommand());
            if (p.hasPermission(Permissions.DELETE_COMMAND)) {
                lore.add(ChatColor.RED + "Click to delete!");
                lore.add("");
            }
            bookMeta.setLore(lore);
            lore.clear();
            book.setItemMeta(bookMeta);
            if (i == 9) i++;
            menu.setItem(i, book);
            i++;
        }

        i = 19;
        for (SignCommand sc : sign.getConsoleCommands()) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + ChatColor.RESET + ChatColor.GOLD + sc.getCommand());
            if (p.hasPermission(Permissions.DELETE_COMMAND)) {
                bookMeta.setLore(lore);
            }
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

        ItemStack chainCmdBlock = new ItemStack(Material.CHAIN_COMMAND_BLOCK);
        ItemMeta chainCmdBlockItemMeta = chainCmdBlock.getItemMeta();
        lore.clear();
        lore.add(ChatColor.YELLOW + "The player is granted operator for this command only.");
        lore.add("");
        lore.add(ChatColor.RED + "THIS IS DISCOURAGED FOR SECURITY REASONS, ONLY USE IF CONSOLE COMMAND CANNOT BE USED.");
        chainCmdBlockItemMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Player Command with Permissions");
        chainCmdBlockItemMeta.setLore(lore);
        chainCmdBlock.setItemMeta(chainCmdBlockItemMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Console Command");
        cmdBlock.setItemMeta(cmdBlockMeta);

        if (p.hasPermission(Permissions.CONSOLE_COMMAND)) {
            choiceInv.setItem(20, head);
            choiceInv.setItem(22, chainCmdBlock);
            choiceInv.setItem(24, cmdBlock);
        } else {
            choiceInv.setItem(22, head);
        }
        choiceInv.setItem(36, arrow);

        editor.setCurrentPage(GUIPage.CHOOSE_COMMAND);
        p.openInventory(choiceInv);
    }

    public void deleteCommand(String cmd) {
        SignCommand found = null;
        for (SignCommand c : sign.getConsoleCommands()) {
            if (c.getCommand().equals(cmd)) {
                found = c;
                SudoSigns.config.deleteCommandFromConfig(sign, c, PlayerInput.CONSOLE_COMMAND);
            }
        }
        if (found != null) {
            sign.deleteConsoleCommand(found);
        }
        found = null;
        for (Map.Entry<SignCommand, Boolean> entry : sign.getPlayerCommands().entrySet()) {
            SignCommand command = entry.getKey();
            if (command.getCommand().equals(cmd.replace("/", ""))) {
                found = command;
                if (entry.getValue()) {
                    SudoSigns.config.deleteCommandFromConfig(sign, command, PlayerInput.PLAYER_COMMAND_WITH_PERMISSIONS);
                } else {
                    SudoSigns.config.deleteCommandFromConfig(sign, command, PlayerInput.PLAYER_COMMAND);
                }
            }
        }
        if (found != null) {
            sign.deletePlayerCommand(found);
        }
        goToCommandsMenu();
    }

    public void chooseCommandType(PlayerInput type) {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the full command in chat. The phrase" + ChatColor.GOLD + " %PLAYER%" + ChatColor.GRAY +
                " will be replaced with the player who clicked the sign. To cancel, type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(type);
    }

    public void addCommand(String cmd, PlayerInput type) {
        SignCommand command = new SignCommand(cmd, type);
        switch (type) {
            case CONSOLE_COMMAND:
                sign.addConsoleCommand(command);
                break;
            case PLAYER_COMMAND:
                sign.addPlayerCommand(command, false);
                break;
            case PLAYER_COMMAND_WITH_PERMISSIONS:
                sign.addPlayerCommand(command, true);
                break;
        }
        SudoSigns.config.addCommandToConfig(sign, command, type);
        Util.sudoSignsMessage(p, ChatColor.GRAY, "Command added successfully!", null);
        goToCommandsMenu();
    }

}

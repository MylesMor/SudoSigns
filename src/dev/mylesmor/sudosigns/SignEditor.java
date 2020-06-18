package dev.mylesmor.sudosigns;

import net.md_5.bungee.chat.ComponentSerializer;
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

import static dev.mylesmor.sudosigns.SudoSigns.prefix;
import static org.bukkit.ChatColor.*;

public class SignEditor {

    private Player p;
    private SudoUser su;
    private SudoSign sign;
    private Inventory mainInv;
    private Inventory permissionsInv;
    private Inventory commandsInv;
    private String currentPage;

    SignEditor(Player p, SudoSign s, SudoUser su) {
        this.su = su;
        this.p = p;
        this.sign = s;
        currentPage = "MAIN";
        createMainMenu();
        p.openInventory(mainInv);
    }

    private void createMainMenu() {
        mainInv = Bukkit.createInventory(p, 45, "Editing: " + sign.getName());
        for (int i = 0; i < mainInv.getSize(); i++) {
            mainInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta bookMeta = book.getItemMeta();
        bookMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Rename Sign");
        book.setItemMeta(bookMeta);

//        ItemStack editSign = new ItemStack(Material.OAK_SIGN);
//        ItemMeta signMeta = editSign.getItemMeta();
//        signMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Edit sign text");
//        editSign.setItemMeta(signMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Permissions");
        barrier.setItemMeta(barrierMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Commands");
        cmdBlock.setItemMeta(cmdBlockMeta);

        mainInv.setItem(20, book);
        //mainInv.setItem(21, editSign);
        mainInv.setItem(22, barrier);
        mainInv.setItem(24, cmdBlock);
    }

    public void editSignText() {

    }

    private void createPermissionsMenu() {

        permissionsInv = Bukkit.createInventory(p, 45, "Permissions: " + sign.getName());
        for (int i = 0; i < permissionsInv.getSize(); i++) {
            permissionsInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Permissions");
        barrier.setItemMeta(barrierMeta);

        ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta bqMeta = bookQuill.getItemMeta();
        bqMeta.setDisplayName("" + RESET + GREEN + "Add new permission");
        bookQuill.setItemMeta(bqMeta);

        List<String> lore = new ArrayList<>();
        lore.add(RED + "Click to delete!");

        int i = 1;
        for (String perm : sign.getPermissions()) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + RESET + GOLD + perm);
            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            if (i == 9 || i == 18 || i == 27) i++;
            permissionsInv.setItem(i, book);
            i++;
        }

        permissionsInv.setItem(0, barrier);
        permissionsInv.setItem(9, barrier);
        permissionsInv.setItem(18, barrier);
        permissionsInv.setItem(27, barrier);
        permissionsInv.setItem(36, arrow);
        permissionsInv.setItem(40, bookQuill);
    }

    public void goToPermissions() {
        currentPage = "PERMISSIONS";
        createPermissionsMenu();
        p.openInventory(permissionsInv);


    }

    public void endEditor() {
        p.sendMessage(SudoSigns.prefix + GRAY + " Changes saved to sign " + GOLD + sign.getName() + GRAY + ".");
    }

    private void createCommandsMenu() {
        commandsInv = Bukkit.createInventory(p, 45, "Commands: " + sign.getName());
        for (int i = 0; i < commandsInv.getSize(); i++) {
            commandsInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(RESET + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack bookQuill = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta bqMeta = bookQuill.getItemMeta();
        bqMeta.setDisplayName("" + RESET + GREEN + "Add new command");
        bookQuill.setItemMeta(bqMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Console Commands");
        cmdBlock.setItemMeta(cmdBlockMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        headMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Player Commands");
        head.setItemMeta(headMeta);

        List<String> lore = new ArrayList<>();
        lore.add(RED + "Click to delete!");

        int i = 1;
        for (SignCommand sc : sign.getPlayerCommands()) {
            if (i > 26) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + RESET + GOLD + "/" + sc.getCommand());
            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            if (i == 9) i++;
            commandsInv.setItem(i, book);
            i++;
        }

        i = 19;
        for (SignCommand sc : sign.getConsoleCommands()) {
            if (i > 35) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + RESET + GOLD + sc.getCommand());
            bookMeta.setLore(lore);
            book.setItemMeta(bookMeta);
            if (i == 27) i++;
            commandsInv.setItem(i, book);
            i++;
        }

        commandsInv.setItem(0, head);
        commandsInv.setItem(9, head);
        commandsInv.setItem(18, cmdBlock);
        commandsInv.setItem(27, cmdBlock);

        commandsInv.setItem(36, arrow);
        commandsInv.setItem(40, bookQuill);
    }

    public void prepareCommand() {
        Inventory choiceInv = Bukkit.createInventory(p, 45, "Player or Console command?");
        for (int i = 0; i < choiceInv.getSize(); i++) {
            choiceInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(RESET + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(RED + "The player must have permission to run the command!");
        headMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Player Command");
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Console Command");
        cmdBlock.setItemMeta(cmdBlockMeta);

        choiceInv.setItem(21, head);
        choiceInv.setItem(23, cmdBlock);
        choiceInv.setItem(36, arrow);

        p.openInventory(choiceInv);
    }

    public void preparePermission() {
        Inventory choiceInv = Bukkit.createInventory(p, 45, "Default or Custom?");
        for (int i = 0; i < choiceInv.getSize(); i++) {
            choiceInv.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName(RESET + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta headMeta = head.getItemMeta();
        List<String> lore = new ArrayList<>();
        lore.add(GOLD + "sudosigns.sign." + sign.getName());
        headMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Default Permission");
        headMeta.setLore(lore);
        head.setItemMeta(headMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Custom Permission");
        cmdBlock.setItemMeta(cmdBlockMeta);

        choiceInv.setItem(21, head);
        choiceInv.setItem(23, cmdBlock);
        choiceInv.setItem(36, arrow);

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
        for (SignCommand c : sign.getPlayerCommands()) {
            if (c.getCommand().equals(cmd)) {
                found = c;
                SudoSigns.config.deleteCommandFromConfig(sign, c, PlayerInput.PLAYER_COMMAND);
            }
        }
        if (found != null) {
            sign.deletePlayerCommand(found);
        }
        goToCommands();
    }

    public void chooseCommandType(PlayerInput type) {
        p.closeInventory();
        p.sendMessage(prefix + GRAY + " Please enter the full command in chat. The phrase" + GOLD + " %PLAYER%" + GRAY +
                 " will be replaced with the player who clicked the sign. To cancel, type " + RED + "CANCEL" + GRAY + ".");
        su.addTextInput(type);
    }

    public void addPermission(Boolean provided, String perm) {
        if (provided) {
            if (perm == null) {
                sign.addPermission("sudosigns.sign." + sign.getName());
                goToPermissions();
            } else {
                sign.addPermission(perm);
            }
        } else {
            p.closeInventory();
            p.sendMessage(prefix + GRAY + " Please enter in chat the permission which the player must have to run this sign or type " + RED + "CANCEL" + GRAY + ".");
            su.addTextInput(PlayerInput.PERMISSION);
        }
    }

    public void addCommand(String cmd, PlayerInput type) {
        SignCommand command = new SignCommand(cmd, type);
        if (type.equals(PlayerInput.CONSOLE_COMMAND)) {
            sign.addConsoleCommand(command);
            SudoSigns.config.addCommandToConfig(sign, command, PlayerInput.CONSOLE_COMMAND);
        } else if (type.equals(PlayerInput.PLAYER_COMMAND)) {
            sign.addPlayerCommand(command);
            SudoSigns.config.addCommandToConfig(sign, command, PlayerInput.PLAYER_COMMAND);
        }
        su.removeTextInput();
        p.sendMessage(prefix + GRAY + " Command added successfully!");
        goToCommands();
    }

    public void goToCommands() {
        currentPage = "COMMANDS";
        createCommandsMenu();
        p.openInventory(commandsInv);
    }

    public void prepareRename() {
        p.sendMessage(prefix + GRAY + " Please enter the new name for the sign in chat or type " + RED + "CANCEL" + GRAY + ".");
        su.addTextInput(PlayerInput.RENAME);
        p.closeInventory();
    }

    public void renameSign(String s) {
        su.removeTextInput();
        if (SudoSigns.signs.containsKey(s)) {
            p.sendMessage(prefix + RED + "A sign with name " + GOLD + s + RED + " already exists! Cancelling...");
        } else {
            sign.setName(s);
            Map.Entry<String, SudoSign> found = null;
            for (Map.Entry<String, SudoSign> entry : SudoSigns.signs.entrySet()) {
                if (entry.getValue().equals(sign)) {
                    found = entry;
                }
            }
            if (found != null) {
                SudoSigns.signs.remove(found.getKey());
                SudoSigns.signs.put(s, sign);
                p.sendMessage(prefix + GRAY + " Sign successfully renamed to " + GOLD + s + GRAY + ".");
                SudoSigns.config.saveToFile(found.getValue(), true, p);
                SudoSigns.config.deleteSign(found.getKey());
            }
        }
    }

    public void goToMain() {
        p.openInventory(mainInv);
        currentPage = "MAIN";
    }

    public String getCurrentPage() {
        return currentPage;
    }


}

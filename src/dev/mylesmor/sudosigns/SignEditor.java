package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static dev.mylesmor.sudosigns.SudoSigns.prefix;
import static dev.mylesmor.sudosigns.SudoSigns.textInput;
import static org.bukkit.ChatColor.*;

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
        bookMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Rename Sign");
        book.setItemMeta(bookMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Permissions");
        barrier.setItemMeta(barrierMeta);

        ItemStack cmdBlock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmdBlockMeta = cmdBlock.getItemMeta();
        cmdBlockMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "Commands");
        cmdBlock.setItemMeta(cmdBlockMeta);

        mainInv.setItem(21, book);
        mainInv.setItem(22, barrier);
        mainInv.setItem(23, cmdBlock);
    }

    private void createPermissionsMenu() {
        permissionsInv = Bukkit.createInventory(p, 45, "Permissions: " + sign.getName());

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + RESET + LIGHT_PURPLE + "BACK");
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
        p.sendMessage(SudoSigns.prefix + GRAY + " Changes saved to sign " + GOLD + sign.getName() + GRAY + ".");
    }

    private void createCommandsMenu() {
        commandsInv = Bukkit.createInventory(p, 45, "Commands: " + sign.getName());
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
        lore.add(RED + "CLICK TO DELETE");

        int i = 1;
        for (SignCommand sc : sign.getPlayerCommands()) {
            if (i > 26) break;
            ItemStack book = new ItemStack(Material.BOOK);
            ItemMeta bookMeta = book.getItemMeta();
            bookMeta.setDisplayName("" + RESET + GOLD + sc.getCommand());
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
            book.setItemMeta(cmdBlockMeta);
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

        p.openInventory(choiceInv);
    }

    public void chooseCommandType(PlayerInput type) {
        p.closeInventory();
        p.sendMessage(prefix + GRAY + " Please enter the full command in chat.");
        textInput.put(p, type);
    }

    public void addCommand(String cmd, PlayerInput type) {
        SignCommand command = new SignCommand(cmd, 1, type);
        if (type.equals(PlayerInput.CONSOLE_COMMAND)) {
            sign.addConsoleCommand(command);
        } else if (type.equals(PlayerInput.PLAYER_COMMAND)) {
            sign.addPlayerCommand(command);
        }
        textInput.remove(p);
        p.sendMessage(prefix + GRAY + " Command added successfully!");
        goToCommands();
    }

    public void goToCommands() {
        currentPage = "COMMANDS";
        createCommandsMenu();
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

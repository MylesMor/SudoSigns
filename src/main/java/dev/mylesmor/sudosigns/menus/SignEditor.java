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

public class SignEditor {

    private Player p;
    private SudoUser su;
    private SudoSign sign;
    private MainMenu mainMenu;
    private PermissionsMenu permMenu;
    private CommandsMenu commandsMenu;
    private MessagesMenu messagesMenu;
    private GUIPage currentPage;

    public SignEditor(Player p, SudoSign s, SudoUser su) {
        this.su = su;
        this.p = p;
        this.sign = s;
        goToMain();
    }



    public GUIPage getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(GUIPage page) {
        currentPage = page;

    }

    public void goToMain() {
        if (mainMenu == null) mainMenu = new MainMenu(p, sign, this);
        mainMenu.goToMainMenu();
    }

    public void goToPermissions() {
        if (permMenu == null) permMenu = new PermissionsMenu(su, p, sign, this);
        permMenu.goToPermissionsMenu();
    }

    public void goToCommands() {
        if (commandsMenu == null) commandsMenu = new CommandsMenu(su, p, sign, this);
        commandsMenu.goToCommandsMenu();
    }

    public void goToMessages() {
        if (messagesMenu == null) messagesMenu = new MessagesMenu(su, p, sign, this);
        messagesMenu.goToMessagesMenu();
    }

    public MainMenu getMainMenu() {
        return mainMenu;
    }

    public PermissionsMenu getPermMenu() {
        return permMenu;
    }

    public CommandsMenu getCommandsMenu() {
        return commandsMenu;
    }

    public MessagesMenu getMessagesMenu() {
        return messagesMenu;
    }

    public void endEditor() {
        Util.sudoSignsMessage(p, ChatColor.GRAY, "Changes saved to sign %NAME%.", sign.getName());
    }


    public void editSignText() {
        //TODO: Add editing of sign text.
    }

    public void prepareRename() {
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the new name for the sign in chat or type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(PlayerInput.RENAME);
        p.closeInventory();
    }

    public void renameSign(String s) {
        if (SudoSigns.signs.containsKey(s)) {
            Util.sudoSignsMessage(p, ChatColor.RED,"A sign with name %NAME% already exists! Cancelling...", s);
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
                Util.sudoSignsMessage(p, ChatColor.GRAY, "Sign successfully renamed to %NAME%.", s);
                SudoSigns.config.saveToFile(found.getValue(), true, p);
                SudoSigns.config.deleteSign(found.getKey());
            }
        }
    }

}

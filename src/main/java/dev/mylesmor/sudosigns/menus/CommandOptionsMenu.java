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
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class CommandOptionsMenu {

    private Inventory menu;
    private SudoUser su;
    private SudoSign sign;
    private SignCommand sc;
    private Player p;
    private SignEditor editor;

    CommandOptionsMenu(SudoUser su, Player p, SudoSign sign, ItemStack item, SignEditor editor) {
        this.su = su;
        this.sign = sign;
        this.p = p;
        this.editor = editor;
        this.sc = findSignCommand(item);
        if (this.sc == null) {
            editor.goToCommands();
            return;
        }

    }

    public void goToCommandOptionsMenu() {
        createCommandOptionsMenu();
        p.openInventory(menu);
    }

    private SignCommand findSignCommand(ItemStack item) {
        NamespacedKey key = new NamespacedKey(SudoSigns.sudoSignsPlugin, "command-number");
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(key, PersistentDataType.INTEGER)) {
            int foundValue = container.get(key, PersistentDataType.INTEGER);
            return sign.getSignCommandByNumber(foundValue);
        }
        return null;
    }

    private void createCommandOptionsMenu() {
        menu = Bukkit.createInventory(p, 45, "Command options: /" + sc.getCommand());
        for (int i = 0; i < menu.getSize(); i++) {
            menu.setItem(i, new ItemStack(Material.GRAY_STAINED_GLASS_PANE));
        }
        List<String> lore = new ArrayList<>();

        ItemStack arrow = new ItemStack(Material.ARROW);
        ItemMeta arrowMeta = arrow.getItemMeta();
        arrowMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "BACK");
        arrow.setItemMeta(arrowMeta);

        ItemStack clock = new ItemStack(Material.CLOCK);
        ItemMeta clockMeta = clock.getItemMeta();
        lore.add(ChatColor.YELLOW + "Change the delay after which the command executes");
        clockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Change Delay");
        clockMeta.setLore(lore);
        clock.setItemMeta(clockMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Delete Command");
        barrier.setItemMeta(barrierMeta);

        if (p.hasPermission(Permissions.COMMAND_OPTIONS) && p.hasPermission(Permissions.DELETE_COMMAND)) {
            menu.setItem(20, clock);
            menu.setItem(24, barrier);
        } else {
            menu.setItem(22, clock);
        }
        menu.setItem(36, arrow);
    }

    public void addDelay() {
        p.closeInventory();
        p.sendMessage(Util.prefix + ChatColor.GRAY + " Please enter the delay in seconds. To cancel, type " + ChatColor.RED + "CANCEL" + ChatColor.GRAY + ".");
        su.addTextInput(PlayerInput.COMMAND_DELAY);
    }

    public void setDelay(double delay) {
        double oldDelay = sc.getDelay();
        sc.setDelay(delay * 1000);
        SudoSigns.config.deleteCommand(sign, sc, sc.getType(), oldDelay);
        SudoSigns.config.addCommand(sign, sc, sc.getType());
    }

    public void deleteCommand() {
        if (sc.getType() == PlayerInput.CONSOLE_COMMAND) {
            SudoSigns.config.deleteCommand(sign, sc, PlayerInput.CONSOLE_COMMAND, sc.getDelay());
            sign.deleteConsoleCommand(sc);
        } else if (sc.getType() == PlayerInput.PLAYER_COMMAND) {
            SudoSigns.config.deleteCommand(sign, sc, PlayerInput.PLAYER_COMMAND, sc.getDelay());
            sign.deletePlayerCommand(sc);
        }
        editor.goToCommands();
    }

}

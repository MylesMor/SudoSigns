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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class MessageOptionsMenu {

    private Inventory menu;
    private SudoUser su;
    private SudoSign sign;
    private SignMessage sm;
    private Player p;
    private SignEditor editor;

    MessageOptionsMenu(SudoUser su, Player p, SudoSign sign, ItemStack item, SignEditor editor) {
        this.su = su;
        this.sign = sign;
        this.p = p;
        this.editor = editor;
        this.sm = findSignMessage(item);
        if (this.sm == null) {
            editor.goToMessages();
            return;
        }
    }

    public void goToMessageOptionsMenu() {
        createMessageOptionsMenu();
        p.openInventory(menu);
    }

    private SignMessage findSignMessage(ItemStack item) {
        NamespacedKey key = new NamespacedKey(SudoSigns.sudoSignsPlugin, "message-number");
        ItemMeta itemMeta = item.getItemMeta();
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.has(key, PersistentDataType.INTEGER)) {
            int foundValue = container.get(key, PersistentDataType.INTEGER);
            return sign.getSignMessageByNumber(foundValue);
        }
        return null;
    }

    private void createMessageOptionsMenu() {
        menu = Bukkit.createInventory(p, 45, "Message options");
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
        lore.add(ChatColor.YELLOW + "Change the delay after which the message is sent");
        clockMeta.setDisplayName("" + ChatColor.RESET + ChatColor.LIGHT_PURPLE + "Change Delay");
        clockMeta.setLore(lore);
        clock.setItemMeta(clockMeta);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta barrierMeta = barrier.getItemMeta();
        barrierMeta.setDisplayName("" + ChatColor.RESET + ChatColor.RED + "Delete Message");
        barrier.setItemMeta(barrierMeta);

        if (p.hasPermission(Permissions.MESSAGE_OPTIONS) && p.hasPermission(Permissions.DELETE_MESSAGE)) {
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
        su.addTextInput(PlayerInput.MESSAGE_DELAY);
    }

    public void setDelay(double delay) {
        double oldDelay = sm.getDelay();
        sm.setDelay(delay * 1000);
        SudoSigns.config.deleteMessage(sign, sm, oldDelay);
        SudoSigns.config.addMessage(sign, sm);
    }

    public void deleteMessage() {
        sign.removeMessage(sm);
        SudoSigns.config.deleteMessage(sign, sm, sm.getDelay());
        // if (message.equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', m))) {
        editor.goToMessages();
    }

}

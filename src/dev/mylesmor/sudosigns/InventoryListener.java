package dev.mylesmor.sudosigns;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.plugin.Plugin;

import static dev.mylesmor.sudosigns.SudoSigns.editors;
import static dev.mylesmor.sudosigns.SudoSigns.sudoSignsPlugin;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (editors.containsKey(p)) {
            SignEditor editor = editors.get(p);
            if (editor.getCurrentPage().equalsIgnoreCase("MAIN")) {
                if (e.getCurrentItem().getType() == Material.BOOK) {
                    editor.renameSign();
                }
                else if (e.getCurrentItem().getType() == Material.BARRIER) {
                    editor.goToPermissions();
                }
                else if (e.getCurrentItem().getType() == Material.COMMAND_BLOCK) {
                    editor.goToCommands();
                }
            }
            if (e.getCurrentItem().getType() == Material.ARROW) {
                editor.goToMain();
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();
        if (editors.containsKey(p)) {
            InventoryView currentInv = p.getOpenInventory();
            Bukkit.getScheduler().scheduleSyncDelayedTask(sudoSignsPlugin, () -> {
                p.sendMessage(currentInv.getTitle());
                p.sendMessage(e.getView().getTitle());
            }, 60L);
            if (currentInv.getTitle() == e.getView().getTitle()) {
                //editors.get(p).endEditor();
                //editors.remove(p);
            }
        }

    }

}

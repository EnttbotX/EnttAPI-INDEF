package x.Entt.EnttAPI.API.Inventory;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public final class Events implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getHolder() instanceof Holder h)) return;

        Player player = (Player) e.getWhoClicked();
        GUI gui = h.getGui();

        if (!gui.hasPermission(player)) {
            e.setCancelled(true);
            player.closeInventory();
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', gui.getNoPermsMsg()));
            return;
        }

        if (e.getClickedInventory() != null && e.getClickedInventory().equals(e.getView().getBottomInventory())) {
            if (e.isShiftClick()) {
                e.setCancelled(true);
            }

            return;
        }

        e.setCancelled(true);
        gui.click(e);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        if (e.getInventory().getHolder() instanceof Holder) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        if (e.getInventory().getHolder() instanceof Holder h) {
            h.getGui().close(e);
        }
    }
}
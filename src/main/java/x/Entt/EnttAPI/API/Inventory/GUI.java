package x.Entt.EnttAPI.API.Inventory;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class GUI {
    private String noPermsMsg = "You don´t have permissions to open this menu!";
    final Inventory inv;
    private final Map<Integer, Item> items = new HashMap<>();
    private final int size;
    private String permission = null;

    GUI(int size, String title) {
        this.size = size;
        this.inv = Bukkit.createInventory(new Holder(this), size, ChatColor.translateAlternateColorCodes('&', title));
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean hasPermission(Player player) {
        if (permission == null || permission.isEmpty()) return true;
        return player.hasPermission(permission);
    }

    public void set(int slot, Item item) {
        if (slot >= 0 && slot < size) {
            inv.setItem(slot, item == null ? null : item.stack());
            if (item != null) {
                items.put(slot, item);
            } else {
                items.remove(slot);
            }
        }
    }

    public void set(int row, int col, Item item) {
        set((row - 1) * 9 + (col - 1), item);
    }

    public void setNoPermsMessage(String message) {
        noPermsMsg = message;
    }

    public String getNoPermsMsg() {
        return noPermsMsg;
    }

    public void fillEmpty(Item item) {
        for (int i = 0; i < size; i++) {
            if (inv.getItem(i) == null) {
                set(i, item);
            }
        }
    }

    public void border(Item item) {
        int rows = size / 9;
        for (int i = 0; i < size; i++) {
            int row = i / 9;
            int col = i % 9;
            if (row == 0 || row == rows - 1 || col == 0 || col == 8) {
                set(i, item);
            }
        }
    }

    public void open(Player p) {
        if (!hasPermission(p)) {
            p.sendMessage(ChatColor.RED + noPermsMsg);
            return;
        }

        p.openInventory(inv);
    }

    void click(InventoryClickEvent e) {
        Item i = items.get(e.getSlot());
        if (i != null) i.click(e);
    }

    Inventory inv() {
        return inv;
    }

    private Consumer<InventoryCloseEvent> closeAction = null;

    public void onClose(Consumer<InventoryCloseEvent> action) {
        this.closeAction = action;
    }

    void close(InventoryCloseEvent e) {
        if (closeAction != null) {
            closeAction.accept(e);
        }
    }
}
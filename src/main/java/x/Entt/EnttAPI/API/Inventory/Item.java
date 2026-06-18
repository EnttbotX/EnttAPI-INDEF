package x.Entt.EnttAPI.API.Inventory;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import x.Entt.EnttAPI.API.Messages.Color;

import java.util.List;
import java.util.function.Consumer;

public final class Item {

    private final ItemStack stack;
    private Consumer<InventoryClickEvent> action;

    private Item(Material material) {
        this.stack = new ItemStack(material);
    }

    public static Item of(Material material) {
        return new Item(material);
    }

    public Item amount(int amount) {
        stack.setAmount(amount);
        return this;
    }

    public Item name(String name) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
            stack.setItemMeta(meta);
        }
        return this;
    }

    public Item lore(List<String> lines) {
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            meta.setLore(Color.setList(lines));
            stack.setItemMeta(meta);
        }
        return this;
    }

    public Item onClick(Consumer<InventoryClickEvent> action) {
        this.action = action;
        return this;
    }

    ItemStack stack() {
        return stack;
    }

    void click(InventoryClickEvent e) {
        if (action != null) action.accept(e);
    }
}
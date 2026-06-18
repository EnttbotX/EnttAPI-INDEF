package x.Entt.EnttAPI.API.Inventory;

import org.bukkit.event.inventory.InventoryCloseEvent;
import java.util.function.Consumer;

public final class Builder {

    private final GUI gui;

    private Builder(int size, String title) {
        this.gui = new GUI(size, title);
    }

    public static Builder create(int size, String title) {
        return new Builder(size, title);
    }

    public Builder needPerm(String permission) {
        gui.setPermission(permission);
        return this;
    }

    public Builder setNoPermsMessage(String message) {
        gui.setNoPermsMessage(message);
        return this;
    }

    public Builder onClose(Consumer<InventoryCloseEvent> action) {
        gui.onClose(action);
        return this;
    }

    public Builder item(int slot, Item item) {
        gui.set(slot, item);
        return this;
    }

    public Builder item(int row, int col, Item item) {
        gui.set(row, col, item);
        return this;
    }

    public Builder border(Item item) {
        gui.border(item);
        return this;
    }

    public Builder fillEmpty(Item item) {
        gui.fillEmpty(item);
        return this;
    }

    public GUI build() {
        return gui;
    }
}
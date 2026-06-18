package x.Entt.EnttAPI.API.Inventory;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

final class Holder implements InventoryHolder {
    final GUI gui;

    Holder(GUI gui) {
        this.gui = gui;
    }

    @Override
    public @NotNull Inventory getInventory() {
        return gui.inv();
    }

    public GUI getGui() {
        return gui;
    }
}
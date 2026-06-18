package x.Entt.EnttAPI.API.Player;

import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class NameTag {
    private NameTag() {}

    public static void set(Entity entity, String name, boolean visible) {
        if (entity == null) return;

        String coloredName = name != null ? ChatColor.translateAlternateColorCodes('&', name) : null;

        if (entity instanceof Player player) {
            player.setCustomName(coloredName);
            player.setCustomNameVisible(visible);
            player.setPlayerListName(coloredName);
            return;
        }

        entity.setCustomName(coloredName);
        entity.setCustomNameVisible(visible);
    }

    public static void set(Entity entity, String name) {
        set(entity, name, true);
    }

    public static void remove(Entity entity) {
        if (entity == null) return;

        entity.setCustomName(null);
        entity.setCustomNameVisible(false);

        if (entity instanceof Player player) {
            player.setPlayerListName(player.getName());
        }
    }
}
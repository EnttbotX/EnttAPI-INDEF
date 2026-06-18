package x.Entt.EnttAPI.API.Selection;

import org.bukkit.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Manager {

    private static final Map<UUID, Selection> map = new HashMap<>();

    public static Selection get(UUID id, World world) {
        return map.computeIfAbsent(id, k -> new Selection(id, world));
    }

    public static void clear(UUID id) {
        map.remove(id);
    }
}
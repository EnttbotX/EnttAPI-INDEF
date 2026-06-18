package x.Entt.EnttAPI.API.Utils;

import java.util.HashMap;
import java.util.UUID;

public class Cooldown {
    private static final HashMap<UUID, Long> cooldowns = new HashMap<>();

    public static void setCooldown(UUID uuid, String key, long millis) {
        cooldowns.put(generateKey(uuid, key), System.currentTimeMillis() + millis);
    }

    public static boolean hasCooldown(UUID uuid, String key) {
        Long expire = cooldowns.get(generateKey(uuid, key));
        return expire != null && expire > System.currentTimeMillis();
    }

    public static long getRemaining(UUID uuid, String key) {
        Long expire = cooldowns.get(generateKey(uuid, key));
        return expire == null ? 0 : Math.max(0, expire - System.currentTimeMillis());
    }

    private static UUID generateKey(UUID uuid, String key) {
        return UUID.nameUUIDFromBytes((uuid.toString() + key).getBytes());
    }
}
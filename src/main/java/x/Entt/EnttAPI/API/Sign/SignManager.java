package x.Entt.EnttAPI.API.Sign;

import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import java.util.HashMap;
import java.util.Map;

public final class SignManager {

    private static final Map<String, SignTemplate> templates = new HashMap<>();
    private static final Map<Location, SignTemplate> activeSigns = new HashMap<>();

    private SignManager() {
    }

    public static void registerTemplate(String trigger, SignTemplate template) {
        templates.put(trigger.toLowerCase(), template);
    }

    @SuppressWarnings("deprecation")
    public static void updateSign(Location loc, String line1, String line2, String line3, String line4) {
        BlockState state = loc.getBlock().getState();
        if (state instanceof Sign sign) {

            sign.setLine(0, org.bukkit.ChatColor.translateAlternateColorCodes('&', line1));
            sign.setLine(1, org.bukkit.ChatColor.translateAlternateColorCodes('&', line2));
            sign.setLine(2, org.bukkit.ChatColor.translateAlternateColorCodes('&', line3));
            sign.setLine(3, org.bukkit.ChatColor.translateAlternateColorCodes('&', line4));

            sign.update(true);
        }
    }

    static SignTemplate getTemplate(String trigger) {
        return templates.get(trigger.toLowerCase());
    }

    static void addActiveSign(Location loc, SignTemplate template) {
        activeSigns.put(loc, template);
    }

    static SignTemplate getActiveSign(Location loc) {
        return activeSigns.get(loc);
    }

    static void removeActiveSign(Location loc) {
        activeSigns.remove(loc);
    }
}
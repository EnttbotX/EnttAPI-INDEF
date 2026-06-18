package x.Entt.EnttAPI.API.Player;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class RadiusCheck {
    private final Player player;
    private int maxBlockRadius = 16;
    private boolean spherical = true;

    public RadiusCheck(Player player) {
        this.player = player;
    }

    public RadiusCheck maxSafeRadius(int max) {
        this.maxBlockRadius = max;
        return this;
    }

    public RadiusCheck useSphere(boolean spherical) {
        this.spherical = spherical;
        return this;
    }

    public List<Entity> getEntities(double radius) {
        Location loc = player.getLocation();
        return player.getWorld().getNearbyEntities(loc, radius, radius, radius)
                .stream()
                .filter(entity -> !entity.equals(player))
                .collect(Collectors.toList());
    }

    public List<Block> getBlocks(int radius) {
        List<Block> blocks = new ArrayList<>();
        Location loc = player.getLocation();
        World world = loc.getWorld();

        if (world == null) return blocks;

        int safeRadius = Math.min(radius, maxBlockRadius);

        int px = loc.getBlockX();
        int py = loc.getBlockY();
        int pz = loc.getBlockZ();
        int radiusSq = safeRadius * safeRadius;

        for (int x = -safeRadius; x <= safeRadius; x++) {
            for (int y = -safeRadius; y <= safeRadius; y++) {
                for (int z = -safeRadius; z <= safeRadius; z++) {

                    if (spherical && (x * x + y * y + z * z) > radiusSq) {
                        continue;
                    }

                    blocks.add(world.getBlockAt(px + x, py + y, pz + z));
                }
            }
        }
        return blocks;
    }
}
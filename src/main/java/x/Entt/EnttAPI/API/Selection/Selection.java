package x.Entt.EnttAPI.API.Selection;

import org.bukkit.World;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Selection {

    private final UUID owner;
    private final World world;
    private final List<Vector> points = new ArrayList<>();

    private int minY;
    private int maxY;
    private boolean fixedHeight;

    public Selection(UUID owner, World world) {
        this.owner = owner;
        this.world = world;
    }

    public void addPoint(Vector v) {
        points.add(v);
        if (!fixedHeight) recalcY();
    }

    public void setHeight(int min, int max) {
        fixedHeight = true;
        minY = min;
        maxY = max;
    }

    public List<Vector> shape() {
        if (points.size() == 2) return square(points.get(0), points.get(1));
        return points;
    }

    public int minY() {
        return minY;
    }

    public int maxY() {
        return maxY;
    }

    private void recalcY() {
        minY = points.stream().mapToInt(Vector::getBlockY).min().orElse(0);
        maxY = points.stream().mapToInt(Vector::getBlockY).max().orElse(0);
    }

    private List<Vector> square(Vector a, Vector b) {
        int minX = Math.min(a.getBlockX(), b.getBlockX());
        int maxX = Math.max(a.getBlockX(), b.getBlockX());
        int minZ = Math.min(a.getBlockZ(), b.getBlockZ());
        int maxZ = Math.max(a.getBlockZ(), b.getBlockZ());

        return List.of(
                new Vector(minX, 0, minZ),
                new Vector(maxX, 0, minZ),
                new Vector(maxX, 0, maxZ),
                new Vector(minX, 0, maxZ)
        );
    }
}
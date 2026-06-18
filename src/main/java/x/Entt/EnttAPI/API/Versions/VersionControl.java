package x.Entt.EnttAPI.API.Versions;

import x.Entt.EnttAPI.EAPI;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class VersionControl {
    private final EAPI plugin;
    private final int resourceId;

    public VersionControl(EAPI plugin, int resourceId) {
        this.plugin = plugin;
        this.resourceId = resourceId;
    }

    public String getLatestVersion() {
        try {
            URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);

            try (InputStream in = con.getInputStream(); Scanner s = new Scanner(in)) {
                if (s.hasNext()) return s.next();
            }
        } catch (Exception e) {
            plugin.getLogger().warning("Update check failed: " + e.getMessage());
        }
        return null;
    }

    public boolean isUpdateAvailable() {
        String latest = getLatestVersion();
        if (latest == null) return false;
        return !plugin.getDescription().getVersion().equalsIgnoreCase(latest);
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + resourceId + "/";
    }
}
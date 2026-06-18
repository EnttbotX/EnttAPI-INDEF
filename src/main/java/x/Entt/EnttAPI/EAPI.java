package x.Entt.EnttAPI;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import x.Entt.EnttAPI.API.Inventory.Events;
import x.Entt.EnttAPI.API.Messages.Color;
import x.Entt.EnttAPI.API.Versions.VersionControl;

public class EAPI extends JavaPlugin {
    private Metrics metrics;
    private VersionControl versionControl;
    public String version = getDescription().getVersion();
    public static String prefix = "&8[&6&lEntt&f&lAPI&8]&r &7» &r";

    @Override
    public void onEnable() {
        metrics = new Metrics(this, 28331);
        versionControl = new VersionControl(this, 127845);

        getServer().getPluginManager().registerEvents(new Events(), this);

        Bukkit.getConsoleSender().sendMessage(Color.set
                (prefix + "&av" + version + " &2Enabled! &5&l[API]"));
    }

    @Override
    public void onDisable() {
        metrics.shutdown();

        Bukkit.getConsoleSender().sendMessage(Color.set
                (prefix + "&av" + version + " &cDisabled &5&l[API]"));
    }

    public void searchUpdates() {
        String latest = versionControl.getLatestVersion();
        if (latest == null) return;

        if (compareVersions(version, latest) >= 0) return;

        String url = versionControl.getResourceURL();

        TextComponent link = new TextComponent(Color.set("&e&l[ DOWNLOAD UPDATE ]"));
        link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url));

        Color.log("");
        Color.log("&2&l======== " + prefix + "&2&l========");
        Color.log("&e&lUPDATE AVAILABLE");
        Color.log("&e&lVersion: &f" + version);
        Color.log("&e&lNew Version: &f" + latest);
        Color.log("&e&lDownload Link:");
        Color.log(url);
        Color.log("&2&l======== " + prefix + "&2&l========");
        Color.log("");

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("eapi.op")) {
                p.sendMessage(Color.set(prefix + "&e&lA new plugin update is available!"));
                p.spigot().sendMessage(link);
            }
        }
    }

    private int compareVersions(String current, String latest) {
        String[] c = current.split("\\.");
        String[] l = latest.split("\\.");

        int max = Math.max(c.length, l.length);

        for (int i = 0; i < max; i++) {
            int cv = i < c.length ? Integer.parseInt(c[i]) : 0;
            int lv = i < l.length ? Integer.parseInt(l[i]) : 0;

            if (cv < lv) return -1;
            if (cv > lv) return 1;
        }

        return 0;
    }
}
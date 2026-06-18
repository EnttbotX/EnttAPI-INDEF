package x.Entt.EnttAPI.API.Messages;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.stream.Collectors;

public class Color {
    public static String set(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }

    public static void log(String msg) {
        Bukkit.getConsoleSender().sendMessage(set(msg));
    }

    public static List<String> setList(List<String> messages) {
        return messages.stream()
                .map(Color::set)
                .collect(Collectors.toList());
    }
}
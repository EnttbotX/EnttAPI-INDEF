package x.Entt.EnttAPI.API.Sign;

import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class SignTemplate {

    private final List<String> lines = new ArrayList<>();
    private String permission = null;
    private Consumer<PlayerInteractEvent> clickAction = null;

    private SignTemplate() {
    }

    public static SignTemplate create() {
        return new SignTemplate();
    }

    public SignTemplate lines(String line1, String line2, String line3, String line4) {
        this.lines.add(ChatColor.translateAlternateColorCodes('&', line1));
        this.lines.add(ChatColor.translateAlternateColorCodes('&', line2));
        this.lines.add(ChatColor.translateAlternateColorCodes('&', line3));
        this.lines.add(ChatColor.translateAlternateColorCodes('&', line4));
        return this;
    }

    public SignTemplate needPerm(String permission) {
        this.permission = permission;
        return this;
    }

    public SignTemplate onClick(Consumer<PlayerInteractEvent> action) {
        this.clickAction = action;
        return this;
    }

    public SignTemplate onClickCommand(String command) {
        this.clickAction = event -> event.getPlayer().performCommand(command);
        return this;
    }

    public List<String> getLines() {
        return lines;
    }

    public String getPermission() {
        return permission;
    }

    void executeClick(PlayerInteractEvent event) {
        if (clickAction != null) {
            clickAction.accept(event);
        }
    }
}
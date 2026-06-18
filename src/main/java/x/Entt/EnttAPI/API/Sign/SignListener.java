package x.Entt.EnttAPI.API.Sign;

import org.bukkit.ChatColor;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public final class SignListener implements Listener {

    @EventHandler
    public void onSignChange(SignChangeEvent e) {
        String firstLine = e.getLine(0);
        if (firstLine == null) return;

        SignTemplate template = SignManager.getTemplate(firstLine.trim());
        if (template == null) return;

        Player p = e.getPlayer();

        if (template.getPermission() != null && !p.hasPermission(template.getPermission())) {
            p.sendMessage(ChatColor.RED + "No tienes permiso para crear este tipo de cartel.");
            e.setCancelled(true);
            return;
        }

        for (int i = 0; i < 4; i++) {
            String rawLine = template.getLines().get(i);
            e.setLine(i, ChatColor.translateAlternateColorCodes('&', rawLine));
        }

        SignManager.addActiveSign(e.getBlock().getLocation(), template);
        p.sendMessage(ChatColor.GREEN + "¡Cartel interactivo creado correctamente!");
    }

    @EventHandler
    public void onSignClick(PlayerInteractEvent e) {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (e.getClickedBlock() == null) return;

        BlockState state = e.getClickedBlock().getState();
        if (!(state instanceof Sign)) return;

        SignTemplate template = SignManager.getActiveSign(e.getClickedBlock().getLocation());
        if (template == null) return;

        Player p = e.getPlayer();

        if (template.getPermission() != null && !p.hasPermission(template.getPermission())) {
            p.sendMessage(ChatColor.RED + "No tienes permiso para usar este cartel.");
            return;
        }

        template.executeClick(e);
    }

    @EventHandler
    public void onSignBreak(BlockBreakEvent e) {
        SignManager.removeActiveSign(e.getBlock().getLocation());
    }
}
package net.novucs.flightlimiter;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class FlightLimiterPlugin extends JavaPlugin implements Listener {

    private String message;
    private int maxHeight;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        loadSettings();
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        loadSettings();
        return true;
    }

    private void loadSettings() {
        message = ChatColor.translateAlternateColorCodes('&', getConfig().getString("message"));
        maxHeight = getConfig().getInt("max-height");
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void limitFlight(PlayerMoveEvent event) {
        // Block flight if player cannot bypass restriction and is at an invalid height.
        Player player = event.getPlayer();
        if (player.isFlying() && !player.hasPermission("flightlimiter.bypass") && event.getTo().getY() > maxHeight) {
            event.setCancelled(true);
            if (!message.isEmpty()) {
                player.sendMessage(message);
            }
        }
    }
}

package net.mineaus.lunar.listener;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.event.impl.AuthenticateEvent;
import net.mineaus.lunar.api.type.Notification;
import net.mineaus.lunar.api.user.User;
import org.bukkit.ChatColor;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerListener implements Listener {

    private LunarClientPlugin plugin;

    public PlayerListener(LunarClientPlugin plugin) {
        this.plugin = plugin;
    }
    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        User data = new User(event.getPlayer().getUniqueId(), event.getPlayer().getName());
        LunarClientPlugin.getApi().getUserManager().setPlayerData(event.getPlayer().getUniqueId(), data);

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            User user = LunarClientPlugin.getApi().getUserManager().getPlayerData(event.getPlayer());
            if (user != null) {
                if (!user.isLunarClient() && plugin.getConfig().getBoolean("kick-player.enabled")) {
                    event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("kick-player.message").replaceAll("%SPACER%", "\n")));
                }
            } else {
                event.getPlayer().kickPlayer(ChatColor.RED + "An error occurred while loading your data.");
            }
        }, 40L);
    }

    @EventHandler
    public void onPlayerAuthenticate(AuthenticateEvent event) {
        Player player = event.getPlayer();
        Configuration config = plugin.getConfig();

        // Authenticate chat message
        if (config.getBoolean("player.authenticate-message.enable")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("player.authenticate-message.message")));
        }

        // Server name
        if (config.getBoolean("server-name.enabled")) {
            try {
                LunarClientPlugin.getApi().updateServerName(player, config.getString("server-name.value"));
            } catch (IOException e) {
                // handle error
            }
        }

        // Authenticate notification
        if (config.getBoolean("player.authenticate-notification.enable")) {
            try {
                LunarClientPlugin.getApi().sendNotification(
                        player,
                        config.getString("player.authenticate-notification.message"),
                        Notification.valueOf(config.getString("player.authenticate-notification.level")),
                        config.getInt("player.authenticate-notification.delay")
                );
            } catch (IOException e) {
                // handle error
            }
        }

        // Authenticate title
        if (config.getBoolean("player.authenticate-title.enable")) {
            try {
                LunarClientPlugin.getApi().sendTitle(
                        player,
                        config.getBoolean("player.authenticate-title.subtitle"),
                        ChatColor.translateAlternateColorCodes('&', config.getString("player.authenticate-title.message")),
                        1F,
                        6,
                        3,
                        3
                );
            } catch (IOException e) {
                // handle error
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        LunarClientPlugin.getApi().getUserManager().removePlayerData(event.getPlayer().getUniqueId());
    }
}
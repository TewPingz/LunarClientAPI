package net.mineaus.lunar.listener;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.event.impl.AuthenticateEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WaypointListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onAuthenticate(AuthenticateEvent event) {
        LunarClientPlugin.getApi().getWaypointManager().reloadWaypoints(event.getPlayer(), true);
    }

    @EventHandler
    public void onPlayerChangeWorld(PlayerChangedWorldEvent event) {
        LunarClientPlugin.getApi().getWaypointManager().reloadWaypoints(event.getPlayer(), true);
    }
}

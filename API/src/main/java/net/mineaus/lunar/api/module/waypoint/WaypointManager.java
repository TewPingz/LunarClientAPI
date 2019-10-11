package net.mineaus.lunar.api.module.waypoint;

import lombok.Getter;
import lombok.Setter;
import net.mineaus.lunar.api.type.Minimap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class WaypointManager {

    /* the list of waypoints */
    public List<Waypoint> waypoints = new ArrayList<>();

    /* waypoints */
    public HashMap<UUID, List<Waypoint>> personalWaypoints = new HashMap<>();

    /**
     * create the waypoint instance.
     * @param name the name of the waypoint.
     * @param location the location of the waypoint.
     * @param red the red color of the waypoint.
     * @param green the green color of the waypoint.
     * @param blue the blue color of the waypoint.
     * @param forced if the waypoint is forced.
     */
    public abstract void createWaypoint(String name, Location location, int red, int green, int blue, boolean forced);

    /**
     * create the personal waypoint instance.
     * @param name the name of the waypoint.
     * @param player the player uuid.
     * @param location the location of the waypoint.
     * @param red the red color of the waypoint.
     * @param green the green color of the waypoint.
     * @param blue the blue color of the waypoint.
     * @param forced if the waypoint is forced.
     */
    public abstract void createWaypoint(String name, UUID player, Location location, int red, int green, int blue, boolean forced);

    /**
     * delete the waypoint instance.
     * @param name the name of the waypoint you want to delete.
     */
    public void deleteWaypoint(String name, UUID player) {
        Waypoint waypoint = getWaypoint(name, player);

        if (waypoint == null) {
            return;
        }

        for (Player target : Bukkit.getOnlinePlayers()) {
            try {
                waypoint.disable(target, false);
            } catch (IOException e) {
                //ignore
            }
        }

        if (personalWaypoints.containsKey(player)) {
            List<Waypoint> waypoints = personalWaypoints.get(player);

            waypoints.remove(waypoint);

            personalWaypoints.put(player, waypoints);
        }

        this.waypoints.remove(waypoint);
    }

    /**
     * reload the waypoints for the player.
     * @param player the player to reload the waypoints for.
     */
    public abstract void reloadWaypoints(Player player, boolean enable);

    /**
     * enable the minimap instance for the player.
     * @param player the player who would like to enable minimap.
     * @throws IOException if the packets fuck up.
     */
    public abstract void enableMiniMap(Player player, Minimap minimap) throws IOException;

    /**
     * get the waypoint instance by the name.
     * @param name the name of the waypoint.
     * @return the waypoint with the name.
     */
    public Waypoint getWaypoint(String name, UUID player) {
        if (this.personalWaypoints.containsKey(player)) {
            for (Waypoint waypoint : this.personalWaypoints.get(player)) {
                if (waypoint.getName().equalsIgnoreCase(name)) {
                    return waypoint;
                }
            }
        }

        for (Waypoint waypoint : this.waypoints) {
            if (waypoint.getName().equalsIgnoreCase(name)) {
                return waypoint;
            }
        }
        return null;
    }
}

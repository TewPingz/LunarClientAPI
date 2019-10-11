package net.mineaus.lunar.api.module.waypoint;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
@Setter
public abstract class Waypoint {
    /* name & uuid of the waypoint */
    public String name;

    /* location of packet */
    public Location location;

    /* color information */
    public int red;
    public int green;
    public int blue;

    /* forced information */
    public boolean forced;

    /**
     * creates the object for the waypoint.
     * @param name the name of the waypoint.
     * @param red the red in the waypoint.
     * @param green the green in the waypoint.
     * @param blue the blue in the waypoint.
     * @param forced if the waypoint is forced or not.
     */
    public Waypoint(String name, Location location, int red, int green, int blue, boolean forced) {
        this.name = name;
        this.location = location;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.forced = forced;
    }

    /**
     * assign the waypoint to the player.
     * @param player the player instance.
     */
    public abstract void enable(Player player) throws IOException;

    /**
     * disable the waypoint for the player.
     * @param player the player instance.
     */
    public abstract void disable(Player player, boolean enable) throws IOException;

}

package net.mineaus.lunar.api.module.border;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;

@Getter
@Setter
public abstract class Border {
    /* name of border */
    public String name;

    /* world of border */
    public World world;

    /* settings of border */
    public boolean cancelsExit;
    public boolean canShrinkExpand;

    /* color of border */
    public int red;
    public int green;
    public int blue;

    /* location of corners */
    public Location minLocation;
    public Location maxLocation;

    /**
     * create the object for a border.
     *
     * @param name the name of the border.
     * @param world the world of the border.
     * @param cancelsExit a setting for the border.
     * @param canShrinkExpand a setting for the border.
     * @param red the red color of the border.
     * @param green the green color of the border.
     * @param blue the blue color of the border.
     * @param minLocation the minimum location of the border.
     * @param maxLocation the maximum location of the border.
     */
    public Border(String name, World world, boolean cancelsExit, boolean canShrinkExpand, int red, int green, int blue, Location minLocation, Location maxLocation){
        this.name = name;
        this.world = world;
        this.cancelsExit = cancelsExit;
        this.canShrinkExpand = canShrinkExpand;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.minLocation = minLocation;
        this.maxLocation = maxLocation;
    }

    /**
     * enable world border for player.
     *
     * @param player the player to enable the border for.
     */
    public abstract void enable(Player player) throws IOException;

    /**
     * update world border for player.
     *
     * @param player the player to update the border for.
     */
    public abstract void update(Player player) throws IOException;

    /**
     * disable world border for player.
     *
     * @param player the player to disable the border for.
     */
    public abstract void disable(Player player) throws IOException;
}

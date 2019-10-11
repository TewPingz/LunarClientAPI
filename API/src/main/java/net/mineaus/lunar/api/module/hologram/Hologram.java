package net.mineaus.lunar.api.module.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public abstract class Hologram {
    /* uuid of hologram */
    public UUID id;

    /* name of hologram */
    public String name;

    /* the world of the hologram */
    public World world;

    /* players with the hologram enabled */
    public List<UUID> enabled;

    /* location of hologram */
    public int x;
    public int y;
    public int z;

    /* lines of holograms */
    public List<String> lines;

    /**
     * create the object of a hologram.
     *
     * @param id the id of the hologram.
     * @param name the name of the hologram.
     * @param x the x location of a hologram.
     * @param y the y location of a hologram.
     * @param z the z location of a hologram.
     * @param world the world of the hologram.
     * @param lines the lines of the hologram.
     */
    public Hologram(UUID id, String name, int x, int y, int z, World world, String... lines){
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.lines = new ArrayList<>();
        this.enabled = new ArrayList<>();
        this.lines.addAll(Arrays.asList(lines));
    }

    /**
     * add a line to the array list.
     *
     * @param text the text of the line.
     * @param index the index of the line.
     */
    public void addLine(String text, int index){
        this.lines.add(index, text);
    }

    /**
     * remove a line from the array list.
     *
     * @param index the index of the line.
     */
    public void removeLine(int index){
        this.lines.remove(index);
    }

    /**
     * enable hologram for player
     *
     * @param player the player to enable it for.
     */
    public abstract void enable(Player player) throws IOException;

    /**
     * update hologram for player.
     *
     * @param player the player to enable it for.
     */
    public abstract void update(Player player) throws IOException;

    /**
     * disable hologram for player.
     *
     * @param player the player to disable it for.
     */
    public abstract void disable(Player player) throws IOException;
}

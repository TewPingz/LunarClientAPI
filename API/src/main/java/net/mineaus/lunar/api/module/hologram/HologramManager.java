package net.mineaus.lunar.api.module.hologram;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class HologramManager {
    /* list of holograms */
    public List<Hologram> hologramList = new ArrayList<>();

    /**
     * create a hologram.
     *
     * @param name the name of hologram.
     * @param location the location of hologram.
     */
    public abstract void createHologram(String name, Location location, String... lines);

    /**
     * delete a hologram.
     *
     * @param name the name of the hologram.
     */
    public void deleteHologram(String name) throws IOException {
        if (this.getHologram(name) == null){
            return;
        }

        Hologram hologram = this.getHologram(name);

        for (Player player : Bukkit.getOnlinePlayers()){
            hologram.disable(player);
        }

        this.hologramList.remove(hologram);
    }

    /**
     * reload holograms for the player.
     *
     * @param player the player to reload holograms for.
     */
    public void reloadHolograms(Player player) throws IOException {
        for (Hologram hologram : this.hologramList){
            if (hologram.getWorld() == player.getLocation().getWorld()){
                hologram.enable(player);
            } else {
                hologram.disable(player);
            }
        }
    }

    /**
     * get hologram by name.
     *
     * @param name the name of the hologram.
     * @return the hologram with the name.
     */
    public Hologram getHologram(String name){
        for (Hologram hologram : this.hologramList){
            if (hologram.getName().equalsIgnoreCase(name)){
                return hologram;
            }
        }
        return null;
    }
}

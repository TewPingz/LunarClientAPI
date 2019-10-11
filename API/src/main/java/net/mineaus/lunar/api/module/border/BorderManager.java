package net.mineaus.lunar.api.module.border;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public abstract class BorderManager {
    /* borders */
    public List<Border> borderList = new ArrayList<>();

    /**
     * create border object.
     *
     * @param name the name of the border.
     * @param world the world of the border.
     * @param cancelsExit setting for the border.
     * @param canShrinkExpand setting for the border.
     * @param red the red color in the border.
     * @param green the green color in the border.
     * @param blue the blue color in the border.
     * @param minLocation the minimum location of the border.
     * @param maxLocation the maximum location of th border.
     */
    public abstract void createBorder(String name, World world, boolean cancelsExit, boolean canShrinkExpand, int red, int green, int blue, Location minLocation, Location maxLocation);

    /**
     * delete a border object.
     *
     * @param name the name of the border to delete.
     */
    public void deleteBorder(String name){
        if (this.getBorder(name) == null){
            return;
        }

        Border border = this.getBorder(name);

        for (Player player : Bukkit.getOnlinePlayers()){
            try {
                border.disable(player);
            } catch (IOException e) {
                //ignore
            }
        }

        this.borderList.remove(border);
    }

    /**
     * get the border object.
     * @param name the name of the object.
     * @return the border object.
     */
    public Border getBorder(String name){
        for (Border border : this.borderList){
            if (border.getName().equalsIgnoreCase(name)){
                return border;
            }
        }
        return null;
    }
}

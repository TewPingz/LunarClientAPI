package net.mineaus.lunar.module.waypoint;

import lombok.Getter;
import lombok.Setter;
import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.module.waypoint.Waypoint;
import net.mineaus.lunar.api.module.waypoint.WaypointManager;
import net.mineaus.lunar.api.type.Minimap;
import net.mineaus.lunar.api.type.ServerRule;
import net.mineaus.lunar.listener.WaypointListener;
import net.mineaus.lunar.BufferUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class WaypointManagerImplementation extends WaypointManager {

    private LunarClientPlugin plugin;

    public WaypointManagerImplementation(LunarClientPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new WaypointListener(), plugin);
    }

    @Override
    public void createWaypoint(String name, Location location, int red, int green, int blue, boolean forced) {
        this.waypoints.add(new WaypointImplemenation(name, location, red, green, blue, forced));
    }

    @Override
    public void createWaypoint(String name, UUID player, Location location, int red, int green, int blue, boolean forced) {
        List<Waypoint> waypoints = new ArrayList<>();

        if (personalWaypoints.containsKey(player)) {
            waypoints = personalWaypoints.get(player);
        }

        waypoints.add(new WaypointImplemenation(name, location, red, green, blue, forced));

        personalWaypoints.put(player, waypoints);
    }

    @Override
    public void reloadWaypoints(Player player, boolean enable) {
        if (!LunarClientPlugin.getApi().isAuthenticated(player)) {
            return;
        }

        if (personalWaypoints.containsKey(player.getUniqueId())) {
            for (Waypoint waypoint : personalWaypoints.get(player.getUniqueId())) {
                try {
                    waypoint.disable(player, enable);
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        for (Waypoint waypoint : this.waypoints) {
            try {
                waypoint.disable(player, enable);
            } catch (IOException e) {
                //ignore
            }
        }

        try {
            ByteArrayOutputStream packetFifteen = new ByteArrayOutputStream();

            packetFifteen.write((byte) 15);
            packetFifteen.write(BufferUtils.writeString(player.getLocation().getWorld().getUID().toString()));

            packetFifteen.close();

            player.sendPluginMessage(plugin, "Lunar-Client", packetFifteen.toByteArray());
        } catch (IOException e) {
            //ignore
        }
    }

    @Override
    public void enableMiniMap(Player player, Minimap minimap) throws IOException {
        LunarClientPlugin.getApi().updateServerRule(player, ServerRule.MINIMAP_STATUS, false, 0, 0F, minimap.name());
    }

    private class WaypointImplemenation extends Waypoint {

        public WaypointImplemenation(String name, Location location, int red, int green, int blue, boolean forced) {
            super(name, location, red, green, blue, forced);
        }

        @Override
        public void enable(Player player) throws IOException {
            if (!LunarClientPlugin.getApi().isAuthenticated(player)){
                return;
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            os.write((byte) 23);
            os.write(BufferUtils.writeString(this.name));
            os.write(BufferUtils.writeString(this.location.getWorld().getUID().toString()));
            os.write(BufferUtils.writeRGB(this.red, this.green, this.blue));
            os.write(BufferUtils.writeInt(this.location.getBlockX()));
            os.write(BufferUtils.writeInt(this.location.getBlockY()));
            os.write(BufferUtils.writeInt(this.location.getBlockZ()));
            os.write(BufferUtils.writeBoolean(forced));
            os.write(1);

            os.close();

            player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
        }

        @Override
        public void disable(Player player, boolean enable) throws IOException {
            if (!LunarClientPlugin.getApi().isAuthenticated(player)){
                return;
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            os.write((byte)24);
            os.write(BufferUtils.writeString(this.name));
            os.write(BufferUtils.writeString(this.location.getWorld().getUID().toString()));

            os.close();

            player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());

            if (enable) {
                this.enable(player);
            }
        }
    }
}

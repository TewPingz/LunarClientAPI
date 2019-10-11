package net.mineaus.lunar.module.border;

import lombok.Getter;
import lombok.Setter;
import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.module.border.Border;
import net.mineaus.lunar.api.module.border.BorderManager;
import net.mineaus.lunar.listener.BorderListener;
import net.mineaus.lunar.BufferUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Getter
@Setter
public class BorderManagerImplementation extends BorderManager {

    private LunarClientPlugin plugin;

    public BorderManagerImplementation(LunarClientPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new BorderListener(), plugin);
    }

    @Override
    public void createBorder(String name, World world, boolean cancelsExit, boolean canShrinkExpand, int red, int green, int blue, Location minLocation, Location maxLocation) {
        if (this.getBorder(name) != null){
            return;
        }
        this.borderList.add(new BorderImplementation(name, world, cancelsExit, canShrinkExpand, red, green, blue, minLocation, maxLocation));
    }

    private class BorderImplementation extends Border {
        public BorderImplementation(String name, World world, boolean cancelsExit, boolean canShrinkExpand, int red, int green, int blue, Location minLocation, Location maxLocation) {
            super(name, world, cancelsExit, canShrinkExpand, red, green, blue, minLocation, maxLocation);
        }

        @Override
        public void enable(Player player) throws IOException {
            if (!LunarClientPlugin.getApi().isAuthenticated(player)){
                return;
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            os.write(20);
            os.write(BufferUtils.writeBoolean(true));
            os.write(BufferUtils.writeString(this.name));
            os.write(BufferUtils.writeString(this.world.getUID().toString()));
            os.write(BufferUtils.writeBoolean(this.cancelsExit));
            os.write(BufferUtils.writeBoolean(this.canShrinkExpand));
            os.write(BufferUtils.writeRGB(this.red, this.green, this.blue));
            os.write(BufferUtils.writeDouble(this.minLocation.getBlockX()));
            os.write(BufferUtils.writeDouble(this.minLocation.getBlockZ()));
            os.write(BufferUtils.writeDouble(this.maxLocation.getBlockX()));
            os.write(BufferUtils.writeDouble(this.maxLocation.getBlockZ()));

            os.close();

            player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());

            ByteArrayOutputStream packetFifteen = new ByteArrayOutputStream();

            packetFifteen.write((byte) 15);
            packetFifteen.write(BufferUtils.writeString(this.world.getUID().toString()));

            packetFifteen.close();

            player.sendPluginMessage(plugin, "Lunar-Client", packetFifteen.toByteArray());
        }

        @Override
        public void update(Player player) throws IOException {
            disable(player);
            enable(player);
        }

        @Override
        public void disable(Player player) throws IOException {
            if (!LunarClientPlugin.getApi().isAuthenticated(player)){
                return;
            }

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            os.write(21);
            os.write(BufferUtils.writeString(this.name));

            os.close();

            player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());

            ByteArrayOutputStream packetFifteen = new ByteArrayOutputStream();

            packetFifteen.write((byte) 15);
            packetFifteen.write(BufferUtils.writeString(this.world.getUID().toString()));

            packetFifteen.close();

            player.sendPluginMessage(plugin, "Lunar-Client", packetFifteen.toByteArray());
        }
    }
}

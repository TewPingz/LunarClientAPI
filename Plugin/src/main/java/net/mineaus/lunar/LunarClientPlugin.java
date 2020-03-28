package net.mineaus.lunar;

import lombok.Getter;
import net.mineaus.lunar.api.LunarClientAPI;
import net.mineaus.lunar.api.event.impl.AuthenticateEvent;
import net.mineaus.lunar.api.module.border.BorderManager;
import net.mineaus.lunar.api.module.hologram.HologramManager;
import net.mineaus.lunar.api.module.waypoint.WaypointManager;
import net.mineaus.lunar.api.user.User;
import net.mineaus.lunar.api.user.UserManager;
import net.mineaus.lunar.command.EmoteCommand;
import net.mineaus.lunar.command.LunarClientCommand;
import net.mineaus.lunar.listener.ClientListener;
import net.mineaus.lunar.listener.PlayerListener;
import net.mineaus.lunar.module.border.BorderManagerImplementation;
import net.mineaus.lunar.module.hologram.HologramManagerImplementation;
import net.mineaus.lunar.module.waypoint.WaypointManagerImplementation;
import net.mineaus.lunar.utils.BufferUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.UUID;

@Getter
public class LunarClientPlugin extends JavaPlugin {

    @Getter private static LunarClientAPI api;

    /* Managers */
    private UserManager userManager;
    private HologramManager hologramManager;
    private WaypointManager waypointManager;
    private BorderManager borderManager;

    @Override
    public void onEnable() {
        //Start API implementation
        api = new LunarClientImplementation(this);

        // Construct manager classes
        this.userManager = new UserManager();
        this.hologramManager = new HologramManagerImplementation(this);
        this.waypointManager = new WaypointManagerImplementation(this);
        this.borderManager = new BorderManagerImplementation(this);

        // Setup configuration
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();

        // Add our channel listeners
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "Lunar-Client");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "Lunar-Client", (channel, player, bytes) -> {
            if (bytes[0] == 26) {
                final UUID outcoming = BufferUtils.getUUIDFromBytes(Arrays.copyOfRange(bytes, 1, 30));

                // To stop server wide spoofing.
                if (!outcoming.equals(player.getUniqueId())) {
                    return;
                }

                User user = getApi().getUserManager().getPlayerData(player);

                if (user != null && !user.isLunarClient()){
                    user.setLunarClient(true);
                    new AuthenticateEvent(player).call(this);
                }

                for (Player other : Bukkit.getOnlinePlayers()) {
                    if (getApi().isAuthenticated(other)) {
                        other.sendPluginMessage(this, channel, bytes);
                    }
                }
            }
        });

        // Register listeners
        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        this.getServer().getPluginManager().registerEvents(new ClientListener(), this);

        // Register commands
        this.getCommand("lunarclient").setExecutor(new LunarClientCommand());
        this.getCommand("emote").setExecutor(new EmoteCommand());
    }

}

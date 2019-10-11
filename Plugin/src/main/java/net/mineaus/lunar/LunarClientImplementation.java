package net.mineaus.lunar;

import net.mineaus.lunar.api.LunarClientAPI;
import net.mineaus.lunar.api.module.border.BorderManager;
import net.mineaus.lunar.api.module.hologram.HologramManager;
import net.mineaus.lunar.api.module.waypoint.WaypointManager;
import net.mineaus.lunar.api.type.Notification;
import net.mineaus.lunar.api.type.ServerRule;
import net.mineaus.lunar.api.type.StaffModule;
import net.mineaus.lunar.api.user.User;
import net.mineaus.lunar.api.user.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class LunarClientImplementation extends LunarClientAPI {

    private LunarClientPlugin plugin;

    public LunarClientImplementation(LunarClientPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void sendCooldown(Player player, String name, Material material, int seconds) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(3);
        os.write(BufferUtils.writeString(name));
        os.write(BufferUtils.writeLong(TimeUnit.SECONDS.toMillis(seconds)));
        os.write(BufferUtils.writeInt(material.getId()));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void sendCooldown(Player player, String name, Material material, long mill) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(3);
        os.write(BufferUtils.writeString(name));
        os.write(BufferUtils.writeLong(mill));
        os.write(BufferUtils.writeInt(material.getId()));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void sendTitle(Player player, boolean subTitle, String message, float size, int duration, int fadeIn, int fadeOut) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(14);

        os.write(BufferUtils.writeString(subTitle ? "subtitle" : "normal"));
        os.write(BufferUtils.writeString(message));
        os.write(BufferUtils.writeFloat(size));
        os.write(BufferUtils.writeLong(TimeUnit.SECONDS.toMillis(duration)));
        os.write(BufferUtils.writeLong(TimeUnit.SECONDS.toMillis(fadeIn)));
        os.write(BufferUtils.writeLong(TimeUnit.SECONDS.toMillis(fadeOut)));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void performEmote(Player player, int type, boolean everyone) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(26);
        os.write(BufferUtils.getBytesFromUUID(player.getUniqueId()));
        os.write(BufferUtils.writeInt(type));

        os.close();

        if (everyone) {
            for (Player other : Bukkit.getOnlinePlayers()) {
                if (isAuthenticated(other)) {
                    other.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
                }
            }
        } else {
            player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
        }
    }

    @Override
    public void sendNotification(Player player, String message, Notification level, int delay) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(9);
        os.write(BufferUtils.writeString(message));
        os.write(BufferUtils.writeLong(TimeUnit.SECONDS.toMillis(delay)));
        os.write(BufferUtils.writeString(level.name()));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void toggleStaffModule(Player player, StaffModule module, boolean enabled) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(12);
        os.write(BufferUtils.writeString(module.name()));
        os.write(BufferUtils.writeBoolean(enabled));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void updateServerRule(Player player, ServerRule rule, boolean b, int i, float f, String s) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(10);
        os.write(BufferUtils.writeString(rule.getName()));
        os.write(BufferUtils.writeBoolean(b));
        os.write(BufferUtils.writeInt(i));
        os.write(BufferUtils.writeFloat(f));
        os.write(BufferUtils.writeString(s));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void updateServerName(Player player, String name) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(11);
        os.write(BufferUtils.writeString(name));
        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void updateNameTag(Player player, Player target, String... tags) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(7);
        os.write(BufferUtils.getBytesFromUUID(target.getUniqueId()));

        os.write(BufferUtils.writeBoolean(true));
        os.write(BufferUtils.writeVarInt(tags.length));
        for (String tag : tags){
            os.write(BufferUtils.writeString(tag));
        }

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void resetNameTag(Player player, Player target) throws IOException {
        if (!this.isAuthenticated(player)){
            return;
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(7);
        os.write(BufferUtils.getBytesFromUUID(target.getUniqueId()));
        os.write(BufferUtils.writeBoolean(false));
        os.write(BufferUtils.writeVarInt(0));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void sendTeamMate(Player player, Player... targets) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(13);

        os.write(BufferUtils.writeBoolean(true));
        os.write(BufferUtils.getBytesFromUUID(player.getUniqueId()));
        os.write(BufferUtils.writeLong(10L));

        Map<UUID, Map<String, Double>> playerMap = new HashMap<>();


        for (Player member : targets) {
            Map<String, Double> posMap = new HashMap<>();

            posMap.put("x", member.getLocation().getX());
            posMap.put("y", member.getLocation().getY());
            posMap.put("z", member.getLocation().getZ());

            playerMap.put(member.getUniqueId(), posMap);
        }

        Map<String, Double> posMap = new HashMap<>();

        posMap.put("x", player.getLocation().getX());
        posMap.put("y", player.getLocation().getY());
        posMap.put("z", player.getLocation().getZ());

        playerMap.put(player.getUniqueId(), posMap);

        os.write(BufferUtils.writeVarInt(playerMap.size()));

        for (Map.Entry<UUID, Map<String, Double>> entry : playerMap.entrySet()) {
            os.write(BufferUtils.getBytesFromUUID(entry.getKey()));
            os.write(BufferUtils.writeVarInt(entry.getValue().size()));
            for (Map.Entry<String, Double> posEntry : entry.getValue().entrySet()) {
                os.write(BufferUtils.writeString(posEntry.getKey()));
                os.write(BufferUtils.writeDouble(posEntry.getValue()));
            }
        }

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public void resetTeamMates(Player player) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        os.write(13);

        os.write(BufferUtils.writeBoolean(false));
        os.write(BufferUtils.writeLong(10L));
        os.write(BufferUtils.writeVarInt(0));

        os.close();

        player.sendPluginMessage(plugin, "Lunar-Client", os.toByteArray());
    }

    @Override
    public boolean isAuthenticated(Player player) {
        User user = getUserManager().getPlayerData(player);

        if (user == null){
            return false;
        }

        return user.isLunarClient();
    }

    @Override
    public BorderManager getBorderManager() {
        return plugin.getBorderManager();
    }

    @Override
    public HologramManager getHologramManager() {
        return plugin.getHologramManager();
    }

    @Override
    public WaypointManager getWaypointManager() {
        return plugin.getWaypointManager();
    }

    @Override
    public UserManager getUserManager() {
        return plugin.getUserManager();
    }
}

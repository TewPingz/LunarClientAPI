package net.mineaus.lunar.listener;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.user.User;
import net.mineaus.lunar.utils.ReflectionUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class ClientListener implements Listener {

    @EventHandler
    public void onRegisterChannel(PlayerRegisterChannelEvent event){
        if (event.getChannel().equals("Lunar-Client")) {
            try {
                LunarClientPlugin.getApi().performEmote(event.getPlayer(), 5, false);
                LunarClientPlugin.getApi().performEmote(event.getPlayer(), -1, false);

                Object nmsPlayer = event.getPlayer().getClass().getMethod("getHandle").invoke(event.getPlayer());
                Object packet = ReflectionUtils.getNmsClass("PacketPlayOutEntityStatus")
                        .getConstructor(ReflectionUtils.getNmsClass("Entity"), byte.class)
                        .newInstance(nmsPlayer, (byte)2);
                ReflectionUtils.sendPacket(event.getPlayer(), packet);
            } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException | ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onUnregisterChannel(PlayerUnregisterChannelEvent event) {
        User user = LunarClientPlugin.getApi().getUserManager().getPlayerData(event.getPlayer());
        if (event.getChannel().equals("Lunar-Client")) {
            user.setLunarClient(false);
        }
    }

}
package net.mineaus.lunar.listener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.user.User;
import net.mineaus.lunar.utils.ReflectionUtils;
import org.bukkit.entity.Entity;
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

                Object packet = ReflectionUtils.getNmsClass("PacketPlayOutEntityStatus")
                        .newInstance();
                Object serilizer = ReflectionUtils.getNmsClass("PacketDataSerializer")
                        .getConstructor(ByteBuf.class)
                        .newInstance(Unpooled.buffer().writeInt(event.getPlayer().getEntityId()).writeByte((byte)2));
                packet.getClass().getMethod("a",
                        ReflectionUtils.getNmsClass("PacketDataSerializer")).invoke(packet, serilizer);
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
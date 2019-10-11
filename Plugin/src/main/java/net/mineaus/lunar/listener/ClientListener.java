package net.mineaus.lunar.listener;

import net.mineaus.lunar.LunarClientPlugin;
import net.mineaus.lunar.api.user.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;

import java.io.IOException;

public class ClientListener implements Listener {

    @EventHandler
    public void onRegisterChannel(PlayerRegisterChannelEvent event){
        if (event.getChannel().equals("Lunar-Client")) {
            try {
                LunarClientPlugin.getApi().performEmote(event.getPlayer(), 5, false);
                LunarClientPlugin.getApi().performEmote(event.getPlayer(), -1, false);
            } catch (IOException e) {
                //ignore
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
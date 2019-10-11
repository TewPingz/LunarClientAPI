package net.mineaus.lunar.api.event.impl;

import net.mineaus.lunar.api.event.PlayerEvent;
import org.bukkit.entity.Player;

public class AuthenticateEvent extends PlayerEvent {

    public AuthenticateEvent(Player player) {
        super(player);
    }

}

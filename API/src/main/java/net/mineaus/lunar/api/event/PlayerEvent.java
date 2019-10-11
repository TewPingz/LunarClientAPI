package net.mineaus.lunar.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class PlayerEvent extends BaseEvent {
	private Player player;

	public PlayerEvent(Player player) {
		this.player = player;
	}

	public UUID getUniqueId() {
		return player.getUniqueId();
	}
}

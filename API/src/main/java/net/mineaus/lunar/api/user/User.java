package net.mineaus.lunar.api.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.GameMode;

import java.util.UUID;

@Getter
@Setter
@RequiredArgsConstructor
public class User {

    /* Global Data */
    private final UUID uniqueId;
    private final String name;

    /* Session Data */
    private boolean lunarClient = false;
    private boolean checking = false;
    private GameMode lastGameMode = GameMode.SURVIVAL;
    private boolean wasFlying = false;
    private boolean wasAllowFlight = false;
}

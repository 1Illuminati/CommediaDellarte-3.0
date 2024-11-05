package org.red.library.game;

import org.bukkit.NamespacedKey;
import org.red.library.entity.A_Player;

import java.util.List;
import java.util.UUID;

public abstract class AbstractGame {
    protected abstract GameResultMessage startGame();

    protected abstract GameResultMessage stopGame();

    public abstract UUID getGameID();

    public abstract NamespacedKey getGameName();

    public abstract List<A_Player> getPlayers();

    public void start() {

    }

    public void stop() {

    }
}

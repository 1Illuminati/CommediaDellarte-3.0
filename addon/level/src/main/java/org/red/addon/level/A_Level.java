package org.red.addon.level;

import org.red.library.CommediaDellarte;
import org.red.library.entity.A_Player;
import org.red.library.util.DataMap;

import java.util.UUID;

public class A_Level {
    private final DataMap dataMap;
    private final A_Player player;
    private final UUID uuid;
    private int level;

    public A_Level(A_Player player, UUID uuid) {
        this(player, uuid, 0);
    }

    public A_Level(A_Player player, UUID uuid, int defaultLevel) {
        this.dataMap = player.getDataMap(CommediaDellarteLevel.getPlugin(CommediaDellarteLevel.class)).getDataMap(uuid.toString());
        this.player = player;
        this.uuid = uuid;
        this.level = defaultLevel;
    }

    public int getLevel() {
        return level;
    }

    public int setLevel(int level) {
        this.level = level;
        return this.level;
    }

    public A_Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }
}

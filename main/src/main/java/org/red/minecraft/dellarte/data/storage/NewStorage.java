package org.red.minecraft.dellarte.data.storage;

import java.util.Map;
import java.util.UUID;

import org.bukkit.plugin.Plugin;
import org.red.library.data.DataMapManager;
import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.util.PairKeyMap;

public class NewStorage {
    public PairKeyMap<String, String, A_DataMap> map = new PairKeyMap<>();
    private DataMapManager mapManager = new DataMapManager();

    public A_DataMap getPlayerDataMap(Plugin plugin, UUID uuid) {
        return map.get(plugin.getName(), "player").getDataMap(uuid.toString());
    }
}

package org.red.minecraft.dellarte.library.data.storage;

import org.red.minecraft.dellarte.library.entity.A_Player;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.library.world.A_World;

public interface StorageController {
    void savePlayer(A_Player player);

    void saveWorld(A_World world);

    void saveEntities();

    void saveDataMap(String key, A_DataMap map);

    void loadPlayer(A_Player player);

    void loadWorld(A_World world);

    void loadEntities();

    A_DataMap loadDataMap(String key);

    boolean contaionDataMap(String key);
}

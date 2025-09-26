package org.red.library.data.storage;

import org.red.library.entity.A_Player;
import org.red.library.util.DataMap;
import org.red.library.world.A_World;

public interface StorageController {
    void savePlayer(A_Player player);

    void saveWorld(A_World world);

    void saveEntities();

    void saveDataMap(String key, DataMap map);

    void loadPlayer(A_Player player);

    void loadWorld(A_World world);

    void loadEntities();

    DataMap loadDataMap(String key);

    boolean contaionDataMap(String key);
}

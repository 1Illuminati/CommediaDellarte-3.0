package org.red.minecraft.dellarte.compatibility.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.PluginConfig;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.util.A_DataMap;

public class A_PapiStorage extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "astorage";
    }

    @Override
    public @NotNull String getAuthor() {
        return "RedKiller";
    }

    @Override
    public @NotNull String getVersion() {
        return PluginConfig.VERSION.asStringValue();
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        // identifier = "plugin_name:type_uuid-or-name/data/path"

        // 1. '_' 기준으로 앞: "plugin_name:type", 뒤: "uuid-or-name/data/path"
        String[] underscoreSplit = identifier.split("_", 2);
        if (underscoreSplit.length < 2) return null;

        // 2. ':' 기준으로 namespace / type 분리
        String[] namespaceSplit = underscoreSplit[0].split(":", 2);
        if (namespaceSplit.length < 2) return null;

        String pluginName = namespaceSplit[0];
        String type       = namespaceSplit[1];

        // 3. '/' 기준으로 앞: uuid or name, 뒤: data path
        String[] pathSplit = underscoreSplit[1].split("/", 2);
        if (pathSplit.length < 2) return null;

        String key      = pathSplit[0]; // uuid 또는 name
        String dataPath = pathSplit[1]; // data/path/...

        // 4. NamespacedKey 생성 후 storage 조회
        IDataStorage storage = CommediaDellarte.getStorage(new NamespacedKey(pluginName, type));
        if (storage == null) return null;

        // 5. key로 DataMap 조회
        A_DataMap map = storage.getDataMap(key);
        if (map == null) return null;

        // 6. 경로 탐색 후 반환
        Object value = map.finder(dataPath);
        return value != null ? value.toString() : null;
    }
}

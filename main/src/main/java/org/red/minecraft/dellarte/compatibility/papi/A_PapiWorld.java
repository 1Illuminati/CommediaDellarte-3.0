package org.red.minecraft.dellarte.compatibility.papi;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.PluginConfig;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.data.IDataStorage;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.util.Arrays;

public class A_PapiWorld extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "aworld";
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
    public String onPlaceholderRequest(Player player, @NotNull String identifier) {
        // 온라인 상태 체크 (월드 위치는 온라인 플레이어만 가능)
        if (player == null) return null;

        World world = player.getWorld();

        String[] split = identifier.split("/");
        if (split.length < 2) return null;

        String pluginName = split[0];
        String dataPath = String.join("/", Arrays.copyOfRange(split, 1, split.length));

        IDataStorage storage = CommediaDellarte.getStorage(new NamespacedKey(pluginName, "world"));
        if (storage == null) return null;

        // 월드 식별자로 월드 이름 사용
        A_DataMap map = storage.getDataMap(world.getName());
        Object value = map.finder(dataPath);
        return value != null ? value.toString() : null;
    }
}

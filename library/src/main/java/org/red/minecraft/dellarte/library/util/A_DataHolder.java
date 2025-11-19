package org.red.minecraft.dellarte.library.util;

import org.bukkit.plugin.Plugin;

public interface A_DataHolder {
    A_DataMap getDataMap();

    A_DataMap getDataMap(Plugin plugin);

    CoolTimeMap getCoolTime();

    CoolTimeMap getCoolTime(Plugin plugin);
}

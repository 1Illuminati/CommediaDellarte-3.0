package org.red.library.util;

import org.bukkit.plugin.Plugin;

public interface A_DataHolder {
    DataMap getDataMap();

    DataMap getDataMap(Plugin plugin);

    CoolTimeMap getCoolTime();

    CoolTimeMap getCoolTime(Plugin plugin);
}

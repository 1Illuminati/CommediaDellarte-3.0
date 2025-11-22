package org.red.minecraft.dellarte.data;

import java.util.List;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public final class SaveConfig implements Keyed {
    private final NamespacedKey nameSpace;
    private final SaveType saveType;
    private final double autoSaveTime;
    private final List<String> saveEvent;
    private final List<String> loadEvent;
    private final boolean saveAtOnDisable;
    private final boolean loadAtOnEnable;

    private SaveConfig(NamespacedKey nameSpace, SaveType saveType, double autoSaveTime, List<String> saveEvent, List<String> loadEvent) {
        this.nameSpace = nameSpace;
        this.saveType = saveType;
        this.autoSaveTime = autoSaveTime;
        this.saveEvent = saveEvent;
        this.loadEvent = loadEvent;
        this.loadAtOnEnable = loadEvent.contains("ONENABLE");
        this.saveAtOnDisable = saveEvent.contains("ONDISABLE");
    }

    public SaveType getSaveType() {
        return this.saveType;
    }

    public double getAutoSaveTime() {
        return this.autoSaveTime;
    }

    public boolean isSaveAtOnDisable() {
        return this.saveAtOnDisable;
    }

    public boolean isLoadAtOnEnable() {
        return this.loadAtOnEnable;
    }

    public boolean isEventInSave(Event event) {
        return this.saveEvent.contains(event.getEventName().toUpperCase());
    }

    public boolean isEventInLoad(Event event) {
        return this.loadEvent.contains(event.getEventName().toUpperCase());
    }

    public static SaveConfig createSaveConfig(NamespacedKey nameSapce, ConfigurationSection section) {
        return new SaveConfig(nameSapce, SaveType.valueOf(section.getString("saveType")), section.getDouble("autoSaveTime"), 
                section.getStringList("saveEvent"), section.getStringList("loadEvent"));
    }

    public enum SaveType {
        FILE,
        MYSQL
    }

    @Override
    public NamespacedKey getKey() {
        return this.nameSpace;
    }
}

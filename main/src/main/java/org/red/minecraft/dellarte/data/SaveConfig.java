package org.red.minecraft.dellarte.data;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Event;

public final class SaveConfig implements Keyed {
    private static final SaveType DEFAULT_SAVETYPE = SaveType.FILE;
    private static final int DEFAULT_AUTOSAVETIME = 300;
    private static final List<String> DEFAULT_SAVEEVENT = List.of("ONDISABLE");
    private static final List<String> DEFAULT_LOADEVENT = List.of("FIRSTLOADEVENT");
    public static SaveConfig createDefaultConfig(NamespacedKey nameSpacedKey) {
        return new SaveConfig(nameSpacedKey, true, DEFAULT_SAVETYPE, DEFAULT_AUTOSAVETIME, DEFAULT_SAVEEVENT, DEFAULT_LOADEVENT);
    }
    
    private final NamespacedKey nameSpace;
    private final boolean enable;
    private final SaveType saveType;
    private final int autoSaveTime;
    private final List<String> saveEvent;
    private final List<String> loadEvent;
    private final boolean saveAtOnDisable;
    private final boolean loadAtOnEnable;

    private SaveConfig(NamespacedKey nameSpace, boolean enable, SaveType saveType, int autoSaveTime, List<String> saveEvent, List<String> loadEvent) {
        this.nameSpace = nameSpace;
        this.enable = enable;
        this.saveType = saveType;
        this.autoSaveTime = autoSaveTime;
        this.saveEvent = saveEvent;
        this.loadEvent = loadEvent;
        this.loadAtOnEnable = loadEvent.contains("ONENABLE");
        this.saveAtOnDisable = saveEvent.contains("ONDISABLE");
    }

    public boolean isEnable() {
        return this.enable;
    }

    public SaveType getSaveType() {
        return this.saveType;
    }

    public int getAutoSaveTime() {
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
        if (section == null) 
            return new SaveConfig(nameSapce, true, DEFAULT_SAVETYPE, DEFAULT_AUTOSAVETIME, DEFAULT_SAVEEVENT, DEFAULT_LOADEVENT);

        boolean enable = section.getBoolean("enable", true);
        SaveType saveType = section.contains("saveType") ? SaveType.valueOf(section.getString("saveType")) : DEFAULT_SAVETYPE;
        int autoSaveTime = section.getInt("autoSaveTime", DEFAULT_AUTOSAVETIME);
        List<String> saveEvent = section.contains("saveEvent") ? section.getStringList("saveEvent") : DEFAULT_SAVEEVENT;
        List<String> loadEvent = section.contains("loadEvent") ? section.getStringList("loadEvent") : DEFAULT_LOADEVENT;

        return new SaveConfig(nameSapce, enable, saveType, autoSaveTime, saveEvent, loadEvent);
    }

    public enum SaveType {
        FILE,
        MYSQL,
        NONE
    }

    @Override
    public NamespacedKey getKey() {
        return this.nameSpace;
    }
}

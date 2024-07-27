package org.red.util;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.CommediaDellartePlugin;
import org.red.library.util.A_Data;

import java.io.*;

/**
 * try catch 두르기 귀찮을때 사용 잘 안사용함
 */
public class A_YamlConfiguration extends YamlConfiguration {
    public void save(@NotNull File file)  {
        try {
            boolean delete = file.delete();
            if (!delete) {
                CommediaDellartePlugin.sendDebugLog(file.getPath() + " is Not Deleted");
            }
            super.save(file);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    public void load(@NotNull File file) {
        try {
            super.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            if (exception instanceof FileNotFoundException) {
                CommediaDellartePlugin.sendLog(file.getPath() + " is Not Found");
                return;
            }
            throw new RuntimeException(exception);
        }
    }

    public void loadFileNotFoundError(@NotNull File file) {
        try {
            super.load(file);
        } catch (IOException | InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public A_Data loadAData(String fileLoc, @Nullable String yamlLoc) {
        this.load(new A_File(fileLoc));
        return (A_Data) this.get(yamlLoc == null ? "" : yamlLoc);
    }

    public void saveAData(String fileLoc, @Nullable String yamlLoc, A_Data aData) {
        this.set(yamlLoc == null ? "" : yamlLoc, aData);
        this.save(new A_File(fileLoc));
    }
}
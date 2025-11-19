package org.red.test;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.red.library.command.AbstractCommand;

public class CommeidaDellarteTest extends AbstractCommand  {
    public void onTest(String[] args) {
        
    }

    @Override
    public @NotNull String getName() {
        return "commediatest";
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        onTest(args);
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        return List.of();
    }
}

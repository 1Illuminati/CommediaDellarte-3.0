package org.red.minecraft.dellarte.library.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.red.minecraft.dellarte.library.util.A_Util;

import java.util.List;

public abstract class AbstractCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        return onCommand(sender, label, args);
    }

    @NotNull
    public abstract String getName();

    public abstract boolean onCommand(@NotNull CommandSender sender, @NotNull String label, String[] args);

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args)  {
        return A_Util.removeStringNotStartWith(onTabComplete(sender, label, args), args[args.length - 1]);
    }

    public abstract List<String> onTabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args);
}

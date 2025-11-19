package org.red.minecraft.dellarte.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.compatibility.vault.A_Vault;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.entity.A_Player;

import java.util.List;

public class A_VaultCommand implements CommandExecutor, TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length <= 1)
            return false;

        A_Player target = CommediaDellarte.getAPlayer(strings[0]);

        if (target == null)
            commandSender.sendMessage("§c존재하지 않는 플레이어 입니다.");

        switch (strings[1]) {
            case "get" -> commandSender.sendMessage("§a" + target.getName() + "님의 보유 잔액: " + A_Vault.getEconomy().getMoney(target.getAOfflinePlayer()));
            case "set" -> {
                if (strings.length <= 3) {
                    commandSender.sendMessage("§c/" + s + " " + target.getName() +  " set " + "[ Money ]");
                    return true;
                }

                double money;

                try {
                    money = Double.valueOf(strings[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("§cPlease enter the number");
                    return true;
                }

                A_Vault.getEconomy().setMoney(target.getAOfflinePlayer(), money);
                commandSender.sendMessage("§a" + target.getName() + "님의 보유 잔액이 " + A_Vault.getEconomy().getMoney(target.getAOfflinePlayer()) + "로 설정되었습니다!");
            }
            case "add" -> {
                if (strings.length <= 3) {
                    commandSender.sendMessage("§c/" + s + " " + target.getName() +  " add " + "[ Money ]");
                    return true;
                }

                double money;

                try {
                    money = Double.valueOf(strings[2]);
                } catch (NumberFormatException e) {
                    commandSender.sendMessage("§cPlease enter the number");
                    return true;
                }

                A_Vault.getEconomy().addMoney(target.getAOfflinePlayer(), money);
                commandSender.sendMessage("§a" + target.getName() + "님의 보유 잔액이 " + A_Vault.getEconomy().getMoney(target.getAOfflinePlayer()) + "로 설정되었습니다!");
            }
            default -> {
                return false;
            }
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return List.of();
    }
}

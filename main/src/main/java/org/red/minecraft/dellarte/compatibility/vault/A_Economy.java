package org.red.minecraft.dellarte.compatibility.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.PluginConfig;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.util.A_DataMap;

import java.util.List;

public class A_Economy implements Economy {
    @Override
    public boolean isEnabled() {
        return PluginConfig.VAULT_ENABLE.asBooleanValue();
    }

    @Override
    public String getName() {
        return "A_Economy";
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public int fractionalDigits() {
        return PluginConfig.VAULT_FRACTIONAL.asIntValue();
    }

    @Override
    public String format(double amount) {
        return amount + PluginConfig.VAULT_FORMAT.asStringValue();
    }

    @Override
    public String currencyNamePlural() {
        return "";
    }

    @Override
    public String currencyNameSingular() {
        return "";
    }

    @Override
    public boolean hasAccount(String playerName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean hasAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return true;
    }

    @Override
    public double getBalance(String playerName) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return -1;
        return this.getBalance(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return CommediaDellarte.getAOfflinePlayer(player).getDataMap(CommediaDellartePlugin.instance).getDouble("vault_balance");
    }

    @Override
    public double getBalance(String playerName, String world) {
        return this.getBalance(playerName);
    }

    @Override
    public double getBalance(OfflinePlayer player, String world) {
        return this.getBalance(player);
    }

    @Override
    public boolean has(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return false;
        return this.has(player, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return amount <= CommediaDellarte.getAOfflinePlayer(player).getDataMap(CommediaDellartePlugin.instance).getDouble("vault_balance");
    }

    @Override
    public boolean has(String playerName, String worldName, double amount) {
        return has(playerName, amount);
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found: " + playerName);
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) return new EconomyResponse(0, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Cannot withdraw a negative amount");
        if (!this.has(player, amount)) return new EconomyResponse(0, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Insufficient funds");

        A_DataMap dataMap = CommediaDellarte.getAOfflinePlayer(player).getDataMap(CommediaDellartePlugin.instance);
        dataMap.addDouble("vault_balance", -amount);
        return new EconomyResponse(amount, dataMap.getDouble("vault_balance"), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse withdrawPlayer(String playerName, String worldName, double amount) {
        return this.withdrawPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, double amount) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null) return new EconomyResponse(0, 0, EconomyResponse.ResponseType.FAILURE, "Player not found: " + playerName);
        return this.depositPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) return new EconomyResponse(0, this.getBalance(player), EconomyResponse.ResponseType.FAILURE, "Cannot deposit a negative amount");

        A_DataMap dataMap = CommediaDellarte.getAOfflinePlayer(player).getDataMap(CommediaDellartePlugin.instance);
        dataMap.addDouble("vault_balance", amount);
        return new EconomyResponse(amount, dataMap.getDouble("vault_balance"), EconomyResponse.ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(String playerName, String worldName, double amount) {
        return this.depositPlayer(playerName, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return this.depositPlayer(player, amount);
    }

    private EconomyResponse bankNotSupported() {
        return new EconomyResponse(0, 0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "This economy does not support bank accounts");
    }

    @Override
    public EconomyResponse createBank(String name, String player) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse createBank(String name, OfflinePlayer player) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse isBankOwner(String name, String playerName) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse isBankOwner(String name, OfflinePlayer player) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse isBankMember(String name, String playerName) {
        return this.bankNotSupported();
    }

    @Override
    public EconomyResponse isBankMember(String name, OfflinePlayer player) {
        return this.bankNotSupported();
    }

    @Override
    public List<String> getBanks() {
        return List.of();
    }

    @Override
    public boolean createPlayerAccount(String playerName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(String playerName, String worldName) {
        return true;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return true;
    }
}

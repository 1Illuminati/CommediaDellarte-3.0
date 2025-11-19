package org.red.minecraft.dellarte.compatibility.vault;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.user.A_OfflinePlayer;
import org.red.minecraft.dellarte.library.util.A_DataMap;
import org.red.minecraft.dellarte.CommediaDellartePlugin;
import org.red.minecraft.dellarte.Config;

import java.util.ArrayList;
import java.util.List;

public class A_Vault implements Economy {
    private static A_Vault aEconomy;
    public static void setEconomy() {
        if (!Config.VAULT.asBooleanValue()) return;
        CommediaDellartePlugin.sendLog("Vault Plugin Checked");
        A_Vault aEconomy = new A_Vault();
        Bukkit.getServicesManager().register(Economy.class, aEconomy, CommediaDellartePlugin.instance, org.bukkit.plugin.ServicePriority.Highest);
        A_Vault.aEconomy = aEconomy;
    }

    public A_DataMap getVaultDatamap(A_OfflinePlayer offlinePlayer) {
        return offlinePlayer.getDataMap().getDataMap("bank");
    }

    public double getMoney(A_OfflinePlayer offlinePlayer) {
        return offlinePlayer.getWallet().getMoney();
    }

    public boolean hasMoney(A_OfflinePlayer offlinePlayer, double v) {
        return offlinePlayer.getWallet().hasMoney(v);
    }

    public void setMoney(A_OfflinePlayer offlinePlayer, double v) {
        offlinePlayer.getWallet().setMoney(v);
    }

    public void addMoney(A_OfflinePlayer offlinePlayer, double v) {
        offlinePlayer.getWallet().addMoney(v);
    }

    public static A_Vault getEconomy() {
        return aEconomy;
    }

    @Override
    public boolean isEnabled() {
        return true;
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
        return -1;
    }

    @Override
    public String format(double v) {
        return v + Config.VAULT_FORMAT.asStringValue();
    }

    @Override
    public String currencyNamePlural() {
        return Config.VAULT_FORMAT.asStringValue();
    }

    @Override
    public String currencyNameSingular() {
        return Config.VAULT_FORMAT.asStringValue();
    }

    @Override
    public boolean hasAccount(String s) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        return true;
    }

    @Override
    public boolean hasAccount(String s, String s1) {
        return true;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        return true;
    }

    @Override
    public double getBalance(String s) {
        return this.getBalance(CommediaDellarte.getAOfflinePlayer(Bukkit.getOfflinePlayer(s)));
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        return this.getBalance(CommediaDellarte.getAOfflinePlayer(offlinePlayer));
    }

    @Override
    public double getBalance(String s, String s1) {
        return this.getBalance(s);
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        return this.getBalance(offlinePlayer);
    }

    public double getBalance(A_OfflinePlayer aOfflinePlayer) {
        return getMoney(aOfflinePlayer);
    }

    @Override
    public boolean has(String s, double v) {
        return this.has(CommediaDellarte.getAOfflinePlayer(Bukkit.getOfflinePlayer(s)), v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        return this.has(CommediaDellarte.getAOfflinePlayer(offlinePlayer), v);
    }

    @Override
    public boolean has(String s, String s1, double v) {
        return this.has(s, v);
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        return this.has(offlinePlayer, v);
    }

    public boolean has(A_OfflinePlayer aOfflinePlayer, double v) {
        return hasMoney(aOfflinePlayer, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, double v) {
        return withdrawPlayer(CommediaDellarte.getAOfflinePlayer(Bukkit.getOfflinePlayer(s)), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        return withdrawPlayer(CommediaDellarte.getAOfflinePlayer(offlinePlayer), v);
    }

    @Override
    public EconomyResponse withdrawPlayer(String s, String s1, double v) {
        return withdrawPlayer(s, v);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return withdrawPlayer(offlinePlayer, v);
    }

    public EconomyResponse withdrawPlayer(A_OfflinePlayer aOfflinePlayer, double v) {
        if (this.has(aOfflinePlayer, v)) {
            addMoney(aOfflinePlayer, v);
            return new EconomyResponse(v, getMoney(aOfflinePlayer), EconomyResponse.ResponseType.SUCCESS, "test success");
        }

        return new EconomyResponse(v, getMoney(aOfflinePlayer), EconomyResponse.ResponseType.FAILURE, "test failure");
    }

    @Override
    public EconomyResponse depositPlayer(String s, double v) {
        return this.depositPlayer(CommediaDellarte.getAOfflinePlayer(Bukkit.getOfflinePlayer(s)), v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        return this.depositPlayer(CommediaDellarte.getAOfflinePlayer(offlinePlayer), v);
    }

    @Override
    public EconomyResponse depositPlayer(String s, String s1, double v) {
        return this.depositPlayer(s, v);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        return this.depositPlayer(offlinePlayer, v);
    }

    public EconomyResponse depositPlayer(A_OfflinePlayer aOfflinePlayer, double v) {
        addMoney(aOfflinePlayer, v);
        return new EconomyResponse(v, getMoney(aOfflinePlayer), EconomyResponse.ResponseType.SUCCESS, "test success");
    }

    @Override
    public EconomyResponse createBank(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankBalance(String s) {
        return null;
    }

    @Override
    public EconomyResponse bankHas(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String s, double v) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, String s1) {
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        return null;
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public boolean createPlayerAccount(String s) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(String s, String s1) {
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        return false;
    }
}

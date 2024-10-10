package org.red.library.user;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.jetbrains.annotations.NotNull;
import org.red.library.CommediaDellarte;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Wallet implements ConfigurationSerializable {
    private final A_OfflinePlayer owner;
    private double money;

    public Wallet(A_OfflinePlayer owner) {
        this(owner, 0);
    }

    public Wallet(A_OfflinePlayer owner, double money) {
        this.owner = owner;
        this.money = money;
    }

    public A_OfflinePlayer getOwner() {
        return this.owner;
    }

    public double getMoney() {
        return this.money;
    }

    public double addMoney(double addMoney) {
        this.money += addMoney;
        return getMoney();
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public boolean hasMoney(double money) {
        return this.money >= money;
    }

    @Override
    public @NotNull Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("money", this.money);
        map.put("uuid", this.owner.getUniqueId().toString());
        return map;
    }

    public static Wallet deserialize(Map<String, Object> map) {
        double money = Double.parseDouble(map.get("money").toString());
        UUID uuid = UUID.fromString(map.get("uuid").toString());
        return new Wallet(CommediaDellarte.getAOfflinePlayer(Bukkit.getOfflinePlayer(uuid)), money);
    }
}

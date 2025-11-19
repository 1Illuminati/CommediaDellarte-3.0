package org.red.minecraft.dellarte.library.item;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.lang.reflect.Field;
import java.util.*;

public class ItemBuilder {
    protected final ItemStack itemStack;
    protected ItemMeta itemMeta;
    protected PersistentDataContainer persistentDataContainer;

    public ItemBuilder(Material material) {
        this(new ItemStack(material));
    }

    public ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.itemMeta = itemStack.getItemMeta();
        this.persistentDataContainer = this.itemMeta.getPersistentDataContainer();
    }

    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation) {
        itemMeta.addAttributeModifier(attribute, new AttributeModifier(attribute.getKey().getKey(), amount, operation));
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, double amount, AttributeModifier.Operation operation, EquipmentSlot slot) {
        itemMeta.addAttributeModifier(attribute, new AttributeModifier(UUID.randomUUID(), attribute.getKey().getKey(), amount, operation, slot));
        return this;
    }

    public ItemBuilder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        itemMeta.addAttributeModifier(attribute, attributeModifier);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level) {
        itemStack.addEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addEnchantments(Map<Enchantment, Integer> enchantments) {
        itemStack.addEnchantments(enchantments);
        return this;
    }

    public ItemBuilder addUnsafeEnchantment(Enchantment ench, int level) {
        itemStack.addUnsafeEnchantment(ench, level);
        return this;
    }

    public ItemBuilder addUnsafeEnchantments(Map<Enchantment, Integer> enchantments) {
        itemStack.addUnsafeEnchantments(enchantments);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        itemStack.setAmount(amount);
        return this;
    }

    public ItemBuilder setData(MaterialData data) {
        itemStack.setData(data);
        return this;
    }

    public ItemBuilder setDurability(short durability) {
        itemStack.setDurability(durability);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta itemMeta) {
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder setType(Material type) {
        itemStack.setType(type);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment arg0, int arg1, boolean arg2) {
        itemMeta.addEnchant(arg0, arg1, arg2);
        return this;
    }

    public ItemBuilder addItemFlags(ItemFlag... arg0) {
        itemMeta.addItemFlags(arg0);
        return this;
    }

    public ItemBuilder setDisplayName(String arg0) {
        itemMeta.setDisplayName(arg0);
        return this;
    }

    public ItemBuilder setLocalizedName(String arg0) {
        itemMeta.setLocalizedName(arg0);
        return this;
    }

    public ItemBuilder setLore(List<String> arg0) {
        itemMeta.setLore(arg0);
        return this;
    }

    public ItemBuilder setLore(String... lore) {
        itemMeta.setLore(Arrays.asList(lore));
        return this;
    }

    public ItemBuilder setUnbreakable(boolean arg0) {
        itemMeta.setUnbreakable(arg0);
        return this;
    }

    public ItemBuilder setCustomModelData(Integer data) {
        itemMeta.setCustomModelData(data);
        return this;
    }

    public <T, Z> ItemBuilder setPersistentDataContainer(NamespacedKey key, PersistentDataType<T, Z> type, Z value) {
        persistentDataContainer.set(key, type, value);
        return this;
    }

    public ItemBuilder removePersistentDataContainer(NamespacedKey key) {
        persistentDataContainer.remove(key);
        return this;
    }

    public ItemStack build() {
        return this.setItemMeta(this.itemMeta).itemStack;
    }

    public static ItemStack createItem(String display, Material material, List<String> lore, int customModelData, int amount) {
        return new ItemBuilder(material).setDisplayName(display).setLore(lore).setAmount(amount).setCustomModelData(customModelData).build();
    }

    public static ItemStack createItem(String display, Material material, List<String> lore, int customModelData) {
        return createItem(display, material, lore, customModelData, 1);
    }

    public static ItemStack createItem(String display, Material material, String lore, int customModelData, int amount) {
        return new ItemBuilder(material).setDisplayName(display).setLore(lore).setAmount(amount).setCustomModelData(customModelData).build();
    }

    public static ItemStack createItem(String display, Material material, String lore, int customModelData) {
        return createItem(display, material, lore, customModelData, 1);
    }

    public static ItemStack createItem(String display, Material material, List<String> lore) {
        return new ItemBuilder(material).setDisplayName(display).setLore(lore).build();
    }

    public static ItemStack createItem(String display, Material material, String lore) {
        return new ItemBuilder(material).setDisplayName(display).setLore(lore).build();
    }

    public static ItemStack createItem(String display, Material material) {
        return new ItemBuilder(material).setDisplayName(display).build();
    }

    public static ItemStack air() {
        return new ItemStack(Material.AIR);
    }

    public static ItemStack getSkullByUrl(String url) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        if (url.isEmpty()) {
            return skull;
        }

        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();

        StringBuilder builder = new StringBuilder();
        url.chars().filter(c -> (c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')).forEach(c -> {
            if (builder.length() != 0 && builder.length() / 4 == 0) builder.append("-");
            builder.append((char) c);
        });

        try {
            Class<?> gameProfile = Class.forName("com.mojang.authlib.GameProfile");
            Class<?> property = Class.forName("com.mojang.authlib.properties.Property");
            Object profile = gameProfile.getConstructor(UUID.class, String.class).newInstance(UUID.fromString(builder.toString()), null);
            byte[] encodedData = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
            gameProfile.getMethod("getProperties").invoke(profile).getClass().getMethod("put", Object.class, Object.class).invoke(gameProfile.getMethod("getProperties").invoke(profile), "textures", property.getConstructor(String.class, String.class).newInstance("textures", new String(encodedData)));
            Field profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, profile);
        } catch (Exception e) {
            throw new RuntimeException();
        }

        skull.setItemMeta(skullMeta);
        return skull;
    }
    public static ItemStack getSkull(OfflinePlayer player) {
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        if (player == null) {
            return skull;
        }

        SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        skullMeta.setOwningPlayer(player);
        skull.setItemMeta(skullMeta);
        return skull;
    }
}

package org.red.library.util.finder;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackHandler extends FindHandler<ItemStack> implements HasNext {
    public ItemStackHandler(ItemStack data) {
        super(data);
    }


    @Override
    public FindHandler<?> getNext(String key) {
        ItemMeta meta = getData().getItemMeta();
        if (meta == null) return null;

        return switch (key) {
            case "Amount" -> new FindHandler<>(getData().getAmount());
            case "Type" -> new FindHandler<>(getData().getType());
            case "Durability" -> new FindHandler<>(getData().getDurability());
            case "Display" -> meta.hasDisplayName() ? new FindHandler<>(meta.getDisplayName()) : null;
            case "Lore" -> meta.hasLore() ? new FindHandler<>(meta.getLore()) : null;
            case "CustomModelData" -> meta.hasCustomModelData() ? new FindHandler<>(meta.getCustomModelData()) : null;
            default -> null;
        };
    }
}

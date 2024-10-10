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
            case "amount" -> new FindHandler<>(getData().getAmount());
            case "type" -> new FindHandler<>(getData().getType());
            case "durability" -> new FindHandler<>(getData().getDurability());
            case "display" -> meta.hasDisplayName() ? new FindHandler<>(meta.getDisplayName()) : null;
            case "lore" -> meta.hasLore() ? new FindHandler<>(meta.getLore()) : null;
            case "customModelData" -> meta.hasCustomModelData() ? new FindHandler<>(meta.getCustomModelData()) : null;
            default -> null;
        };
    }
}

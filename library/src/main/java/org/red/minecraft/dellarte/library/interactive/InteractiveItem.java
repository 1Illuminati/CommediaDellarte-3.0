package org.red.minecraft.dellarte.library.interactive;

import org.bukkit.Material;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.red.minecraft.dellarte.library.item.ItemBuilder;

public interface InteractiveItem extends InteractiveObj<ItemStack> {
    @InteractiveItemAct.ActAnnotation(event = PlayerInteractEvent.class)
    class LEFT_CLICK_AIR implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerInteractEvent.class)
    class RIGHT_CLICK_AIR implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerInteractEvent.class)
    class LEFT_CLICK_BLOCK implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerInteractEvent.class)
    class RIGHT_CLICK_BLOCK implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerInteractEvent.class)
    class PHYSICAL implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = BlockBreakEvent.class)
    class BREAK implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerFishEvent.class)
    class FISH implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = EntityDamageByEntityEvent.class)
    class HIT implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = EntityDamageByEntityEvent.class)
    class DAMAGED implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerDropItemEvent.class)
    class DROP implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerSwapHandItemsEvent.class)
    class SWAP_HAND implements InteractiveItemAct {}

    @InteractiveItemAct.ActAnnotation(event = PlayerDeathEvent.class)
    class DEATH implements InteractiveItemAct {}

    default void setInteractiveInObj(ItemStack stack) {
        if (stack == null || stack.getType() == Material.AIR)
            throw new RuntimeException("ItemStack Cannot be Null or AIR");

        new ItemBuilder(stack).setPersistentDataContainer(getKey(), PersistentDataType.STRING, "InteractiveItem").build();
    }

    default boolean isHasInteractive(ItemStack stack) {
        if (stack == null) return false;

        ItemMeta meta = stack.clone().getItemMeta();

        if (meta == null) return false;

        PersistentDataContainer dataContainer = meta.getPersistentDataContainer();
        return dataContainer.has(getKey(), PersistentDataType.STRING);
    }

    default void removeInteractive(ItemStack stack) {
        if (!isHasInteractive(stack)) return;

        new ItemBuilder(stack).removePersistentDataContainer(getKey()).build();
    }

    interface InteractiveItemAct extends InteractiveAct<ItemStack> {

    }
}

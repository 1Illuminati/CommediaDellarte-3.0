package org.red.minecraft.dellarte.event.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.interactive.InteractiveItem;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;

public class InteractiveItemListener extends DellarteListener {
    private final InteractiveManager<ItemStack> manager = CommediaDellarte.getInteractiveManager(ItemStack.class);

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;
        ItemStack itemStack = event.getItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;

        switch (event.getAction()) {
            case LEFT_CLICK_AIR -> manager.runHasInteractiveObj(itemStack, InteractiveItem.LEFT_CLICK_AIR.class, event);
            case LEFT_CLICK_BLOCK -> manager.runHasInteractiveObj(itemStack, InteractiveItem.LEFT_CLICK_BLOCK.class, event);
            case RIGHT_CLICK_AIR -> manager.runHasInteractiveObj(itemStack, InteractiveItem.RIGHT_CLICK_AIR.class, event);
            case RIGHT_CLICK_BLOCK -> manager.runHasInteractiveObj(itemStack, InteractiveItem.RIGHT_CLICK_BLOCK.class, event);
            case PHYSICAL -> manager.runHasInteractiveObj(itemStack, InteractiveItem.PHYSICAL.class, event);
        }
    }

    @EventHandler
    public void playerSwapHandItemsEvent(PlayerSwapHandItemsEvent event) {
        ItemStack itemStack = event.getOffHandItem();
        if (itemStack == null || itemStack.getType() == Material.AIR) return;
        manager.runHasInteractiveObj(itemStack, InteractiveItem.SWAP_HAND.class, event);
    }

    @EventHandler
    public void playerDropItemEvent(PlayerDropItemEvent event) {
        ItemStack itemStack = event.getItemDrop().getItemStack();
        manager.runHasInteractiveObj(itemStack, InteractiveItem.DROP.class, event);
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return;
        manager.runHasInteractiveObj(itemStack, InteractiveItem.BREAK.class, event);
    }

    @EventHandler
    public void playerFishEvent(PlayerFishEvent event) {
        ItemStack itemStack = event.getPlayer().getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return;
        manager.runHasInteractiveObj(itemStack, InteractiveItem.FISH.class, event);
    }

    @EventHandler
    public void entityDamageByEntityEvent(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player attacker) {
            ItemStack itemStack = attacker.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) return;
            manager.runHasInteractiveObj(itemStack, InteractiveItem.HIT.class, event);
        }

        if (event.getEntity() instanceof Player defender) {
            ItemStack itemStack = defender.getInventory().getItemInMainHand();
            if (itemStack.getType() == Material.AIR) return;
            manager.runHasInteractiveObj(itemStack, InteractiveItem.DAMAGED.class, event);
        }
    }

    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event) {
        ItemStack itemStack = event.getEntity().getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) return;
        manager.runHasInteractiveObj(itemStack, InteractiveItem.DEATH.class, event);
    }
}

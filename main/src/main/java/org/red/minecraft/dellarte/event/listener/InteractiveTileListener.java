package org.red.minecraft.dellarte.event.listener;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.red.minecraft.dellarte.library.CommediaDellarte;
import org.red.minecraft.dellarte.library.interactive.InteractiveManager;
import org.red.minecraft.dellarte.library.interactive.InteractiveTile;

public class InteractiveTileListener extends DellarteListener {
    private final InteractiveManager<TileState> manager = CommediaDellarte.getInteractiveManager(TileState.class);

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (event.getHand() == EquipmentSlot.OFF_HAND) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) return;

        BlockState state = clickedBlock.getState();
        if (!(state instanceof TileState tileState)) return;

        switch (event.getAction()) {
            case LEFT_CLICK_BLOCK -> manager.runHasInteractiveObj(tileState, InteractiveTile.LEFT_CLICK_BLOCK.class, event);
            case RIGHT_CLICK_BLOCK -> manager.runHasInteractiveObj(tileState, InteractiveTile.RIGHT_CLICK_BLOCK.class, event);
        }
    }

    @EventHandler
    public void blockBreakEvent(BlockBreakEvent event) {
        BlockState state = event.getBlock().getState();
        if (!(state instanceof TileState tileState)) return;

        manager.runHasInteractiveObj(tileState, InteractiveTile.BREAK.class, event);
    }
}

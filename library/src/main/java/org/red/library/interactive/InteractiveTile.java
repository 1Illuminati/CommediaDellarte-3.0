package org.red.library.interactive;

import org.bukkit.block.TileState;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.red.library.item.ItemBuilder;

public interface InteractiveTile extends InteractiveObj<TileState> {
    @InteractiveAct.ActAnnotation(event = BlockBreakEvent.class)
    class BREAK implements InteractiveTileAct {}
    @InteractiveAct.ActAnnotation(event = PlayerInteractEvent.class)
    class LEFT_CLICK_BLOCK implements InteractiveTileAct {}
    @InteractiveAct.ActAnnotation(event = PlayerInteractEvent.class)
    class RIGHT_CLICK_BLOCK implements InteractiveTileAct {}

    default void setInteractiveInObj(TileState tile) {
        tile.getPersistentDataContainer().set(getKey(), PersistentDataType.STRING, "InteractiveTile");
    }

    default boolean isHasInteractive(TileState tile) {
        PersistentDataContainer dataContainer = tile.getPersistentDataContainer();
        return dataContainer.has(getKey(), PersistentDataType.STRING);
    }

    default void removeInteractive(TileState tile) {
        if (!isHasInteractive(tile)) return;

        PersistentDataContainer dataContainer = tile.getPersistentDataContainer();
        dataContainer.remove(getKey());
    }

    interface InteractiveTileAct extends InteractiveAct<TileState> {

    }
}

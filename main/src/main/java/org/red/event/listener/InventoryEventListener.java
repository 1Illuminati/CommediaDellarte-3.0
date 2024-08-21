package org.red.event.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.red.library.inventory.Button;
import org.red.library.inventory.CustomGui;

public class InventoryEventListener extends DellarteListener {

    @EventHandler
    public void onClickEvent(InventoryClickEvent event){
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();

        if (!(holder instanceof CustomGui gui)) return;

        gui.onClick(event);

        Button button = gui.getButton(event.getRawSlot());

        if (button != null) button.run(event);
    }

    @EventHandler
    public void onCloseEvent(InventoryCloseEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();

        if (!(holder instanceof CustomGui gui)) return;

        gui.onClose(event);
    }

    @EventHandler
    public void onOpenEvent(InventoryOpenEvent event) {
        Inventory inv = event.getInventory();
        InventoryHolder holder = inv.getHolder();

        if (!(holder instanceof CustomGui gui)) return;

        gui.onOpen(event);
    }
}
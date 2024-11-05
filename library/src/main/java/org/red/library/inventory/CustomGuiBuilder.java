package org.red.library.inventory;

import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomGuiBuilder {

    private final CustomGui gui;
    private boolean allClickCancel = false;

    public CustomGuiBuilder(@NotNull InventoryType type) {
        gui = new CustomGui(type);
    }

    public CustomGuiBuilder(@NotNull InventoryType type, @NotNull String title) {
        gui = new CustomGui(type, title);
    }

    public CustomGuiBuilder(int size) throws IllegalArgumentException {
        gui = new CustomGui(size);
    }

    public CustomGuiBuilder(int size, @NotNull String title) throws IllegalArgumentException {
        gui = new CustomGui(size, title);
    }

    public CustomGuiBuilder setAllClickCancel(boolean allClickCancel) {
        this.gui.setAllClickCancel(true);
        return this;
    }

    public CustomGuiBuilder setItem(int slot, ItemStack itemStack) {
        gui.setItem(slot, itemStack);
        return this;
    }

    public CustomGuiBuilder setItem(int slot, ItemStack itemStack, Button button) {
        gui.setItem(slot, itemStack, button);
        return this;
    }

    public CustomGuiBuilder setItems(ItemStack itemStack, Integer... slots) {
        gui.setItems(itemStack, slots);
        return this;
    }

    public CustomGuiBuilder setItems(ItemStack itemStack, Button button, Integer... slots) {
        gui.setItems(itemStack, button, slots);
        return this;
    }

    public CustomGuiBuilder setButton(int slot, Button button) {
        gui.setButton(slot, button);
        return this;
    }

    public CustomGuiBuilder fillItem(int slot, ItemStack itemStack) {
        gui.setItem(slot, itemStack);
        return this;
    }

    public CustomGuiBuilder fillItem(int slot, ItemStack itemStack, Button button) {
        gui.setItem(slot, itemStack, button);
        return this;
    }

    public CustomGuiBuilder setContents(ItemStack[] itemStacks) {
        gui.setContents(itemStacks);
        return this;
    }

    public CustomGui build() {
        return this.gui;
    }

}

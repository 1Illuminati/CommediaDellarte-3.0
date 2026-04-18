package org.red.minecraft.dellarte.library.interactive;

public enum InteractivePriority {
    LOWEST(0),

    LOW(1),

    NORMAL(2),

    HIGH(3),

    HIGHEST(4);

    private final int slot;

    InteractivePriority(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }
}

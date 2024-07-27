
package org.red.library.event.area.block;

import org.bukkit.event.HandlerList;
import org.bukkit.event.block.NotePlayEvent;
import org.red.library.world.Area;

public class AreaNotePlayEvent extends AreaBlockEvent<NotePlayEvent> {
    public AreaNotePlayEvent(Area area, NotePlayEvent event) {
        super(area, event);
    }

    public static HandlerList getHandlerList() {
        return handler_map.computeIfAbsent(AreaNotePlayEvent.class, k -> new HandlerList());
    }
}

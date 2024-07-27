package org.red.library.interactive;

import org.bukkit.NamespacedKey;
import org.bukkit.event.Event;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface InteractiveManager<T> {
    Class<T> getManagerType();

    void registerInteractiveObj(InteractiveObj<T> obj);

    List<InteractiveObj<T>> getInteractiveObj(T obj);

    boolean hasInteractiveObj(T obj);

    @Nullable
    InteractiveObj<T> getInteractiveObj(@NotNull NamespacedKey key);

    void runInteractiveObj(@NotNull InteractiveObj<T> obj, @NotNull Class<? extends InteractiveAct<T>> act, @NotNull  Event event);

    void runHasInteractiveObj(@NotNull T obj, @NotNull Class<? extends InteractiveAct<T>> act, @NotNull Event event);
}

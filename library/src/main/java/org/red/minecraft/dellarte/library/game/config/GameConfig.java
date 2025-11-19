package org.red.minecraft.dellarte.library.game.config;

import org.red.minecraft.dellarte.library.game.GameResultMessage;

public interface GameConfig<T> {

    T getValue();

    void setValue(T value);

    Class<T> getValueClass();
}

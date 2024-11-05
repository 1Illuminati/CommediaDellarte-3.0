package org.red.library.game.config;

import org.red.library.game.GameResultMessage;

public interface GameConfig<T> {

    T getValue();

    void setValue(T value);

    Class<T> getValueClass();
}

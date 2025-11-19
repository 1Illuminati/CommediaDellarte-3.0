package org.red.minecraft.dellarte.library.util.finder;

import org.jetbrains.annotations.Nullable;
import org.red.minecraft.dellarte.library.util.CoolTimeMap;

public class CoolTimeMapHandler extends FindHandler<CoolTimeMap> implements HasNext {
    public CoolTimeMapHandler(CoolTimeMap data) {
        super(data);
    }

    @Override
    public @Nullable FindHandler<?> getNext(String key) {
        if (!this.getData().checkCoolTime(key)) {
            return new FindHandler<>(this.getData().getCoolTime(key));
        }

        return new FindHandler<>(0L);
    }
}

package org.red.minecraft.dellarte.library.util;

public record PairData<T, V>(T dataA, V dataB) {

    public boolean equalsDataA(T dataA) {
        return this.dataA.equals(dataA);
    }

    public boolean equalsDataB(V dataB) {
        return this.dataA.equals(dataB);
    }
}

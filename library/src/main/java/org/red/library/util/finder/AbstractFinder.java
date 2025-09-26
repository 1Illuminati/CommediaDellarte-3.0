package org.red.library.util.finder;

public abstract class AbstractFinder<T> {

    private final T data;
    
    protected AbstractFinder(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean hasNext() {
        return this instanceof HasNext;
    }

    public abstract String toStr();
}

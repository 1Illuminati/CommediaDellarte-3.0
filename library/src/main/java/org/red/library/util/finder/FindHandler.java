package org.red.library.util.finder;

public class FindHandler<T> {
    private final T data;
    public FindHandler(T data) {
        this.data = data;
    }

    public String toStr() {
        return this.data.toString();
    }

    public T getData() {
        return this.data;
    }
}

package com.wwt.paperservice.utils;

public class Pair<T> {

    private T value;

    private long number;

    public Pair(T value, long number) {
        this.value = value;
        this.number = number;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }
}

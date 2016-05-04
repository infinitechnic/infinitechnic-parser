package com.infinitechnic.parser.csv.event;

public interface EventHandler<T> {
    void handle(T object);
}

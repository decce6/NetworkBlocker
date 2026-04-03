package me.decce.transformingbase.core.util;

import lombok.SneakyThrows;
import me.decce.transformingbase.core.ConnectionBlockedException;

public class Thrower {
    @SneakyThrows
    public static void throwBlockedException() {
        throw new ConnectionBlockedException();
    }
}

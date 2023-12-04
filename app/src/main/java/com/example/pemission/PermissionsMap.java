package com.example.pemission;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PermissionsMap {

    private static final AtomicInteger atomicInteger = new AtomicInteger(100);
    private static final Map<Integer, PermissionCallbacks> map = new ConcurrentHashMap<>();

    public static int put(PermissionCallbacks callbacks) {
        int requestCode = atomicInteger.getAndIncrement();
        map.put(requestCode, callbacks);
        return requestCode;
    }

    public static PermissionCallbacks get(int requestCode) {
        PermissionCallbacks callbacks = map.get(requestCode);
        map.remove(requestCode);
        return callbacks;
    }
}

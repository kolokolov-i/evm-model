package superbro.evm.core;

import superbro.evm.core.device.*;
import superbro.evm.core.device.System;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {

    private static Map<String, Device.DeviceBuilder> builders;

    static {
        builders = new HashMap<>();
        builders.put("System", new System.Builder());
        builders.put("Keyboard", new Keyboard.Builder());
        builders.put("Empty", new Empty.Builder());
    }

    static Device get(String type) {
        Device.DeviceBuilder builder = builders.get(type);
        if (builder == null) {
            return null;
        }
        return builder.create();
    }

    public static void registrate(String name, Device.DeviceBuilder builder) {
        builders.put(name, builder);
    }
}

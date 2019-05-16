package superbro.evm.core;

import superbro.evm.core.device.Keyboard;
import superbro.evm.core.device.System;

import java.util.Map;

public class DeviceManager {

    private static Map<String, Device.DeviceBuilder> builders;

    static {
        builders.put("System", new System.Builder());
        builders.put("Keyboard", new Keyboard.Builder());
    }

    static Device get(String type) {
        Device result = null;
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

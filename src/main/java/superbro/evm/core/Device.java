package superbro.evm.core;

import javax.swing.*;

public abstract class Device {

    public abstract void launch();

    public abstract JPanel getOptionsPane();

    public abstract String getName();

    public abstract static class DeviceBuilder {

        public abstract Device create();
    }
}

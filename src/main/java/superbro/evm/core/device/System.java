package superbro.evm.core.device;

import superbro.evm.core.Device;

import javax.swing.*;

public class System extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create() {
            return new System();
        }
    }

    public System() {
    }

    @Override
    public void launch() {

    }

    @Override
    public JPanel getOptionsPane() {
        return null;
    }

    @Override
    public String getName() {
        return "System";
    }
}

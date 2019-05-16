package superbro.evm.core.device;

import superbro.evm.core.Device;

import javax.swing.*;

public class System extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create() {
            return null;
        }
    }

    @Override
    public void launch() {

    }

    @Override
    public JPanel getOptionsPane() {
        return null;
    }
}

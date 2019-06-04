package superbro.evm.core.device;

import superbro.evm.core.Device;

import javax.swing.*;

public class Keyboard extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create() {
            return new Keyboard();
        }
    }

    public Keyboard() {
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
        return "Keyboard";
    }
}

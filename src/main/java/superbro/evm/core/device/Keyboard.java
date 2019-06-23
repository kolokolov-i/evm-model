package superbro.evm.core.device;

import superbro.evm.core.Device;
import superbro.evm.core.Machine;

import javax.swing.*;

public class Keyboard extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create(Machine m) {
            return new Keyboard(m);
        }
    }

    private Keyboard(Machine m) {
        super(m);
        autostart = true;
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

    @Override
    protected void handle(DeviceCall call) {
        System.out.println("Device Keyboard is called");
    }
}

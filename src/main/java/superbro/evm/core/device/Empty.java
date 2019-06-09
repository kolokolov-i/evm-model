package superbro.evm.core.device;

import superbro.evm.core.Device;
import superbro.evm.core.Machine;

import javax.swing.*;

public class Empty extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create(Machine m) {
            return new Empty(m);
        }
    }

    private Empty(Machine m) {
        super(m);
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
        return "Empty";
    }

    @Override
    protected void handle(DeviceCall call) {
        System.out.println("Device empty is called");
    }
}

package superbro.evm.core.device;

import superbro.evm.core.Device;
import superbro.evm.core.Machine;

import javax.swing.*;

public class Chipset extends Device {

    public static class Builder extends DeviceBuilder {
        @Override
        public Device create(Machine parent) {
            return new Chipset(parent);
        }
    }

    private Chipset(Machine m) {
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
        return "System";
    }

    @Override
    public void handle(DeviceCall call) {
        //System.out.println("Device System is called");
        switch(call.p0){
            case (byte)0xFF:
                //System.out.println("System shutdown");
                holder.poweroff();
                break;
            default:

        }
    }
}

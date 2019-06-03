package superbro.evm.core.device;

import superbro.evm.core.Device;

import javax.swing.*;

public class Empty extends Device {

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
}

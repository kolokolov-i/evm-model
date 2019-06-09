package superbro.evm.core.cpu;

import superbro.evm.core.CPU;

public class IntEvent {
    public boolean external;
    public CPU.Interrupt target;

    public IntEvent(boolean external, CPU.Interrupt target) {
        this.external = external;
        this.target = target;
    }
}

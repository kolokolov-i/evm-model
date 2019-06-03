package superbro.evm.core.cpu;

import superbro.evm.core.CPU;

public abstract class Executor {

    public static Executor
            NOP = new NOP(),
            ADD = new ADD();

    public abstract void execute(CPU cpu, short command);
}

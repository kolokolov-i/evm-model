package superbro.evm.core;

import superbro.evm.core.cpu.*;

import java.util.HashMap;
import java.util.Map;

public class CPU {

    private Machine machine;
    public Reg8[] regs8;
    public Reg16U[] regs16;
    public Reg8[] ports;
    public Reg8 RI, RF;
    public Reg16 PC, IR, SP, BP;

    CPU(Machine holder) {
        machine = holder;
        regs8 = new Reg8[8];
        regs16 = new Reg16U[4];
        for (int i = 0; i < 8; i++) {
            regs8[i] = new Reg8();
        }
        for (int i = 0; i < 4; i++) {
            regs16[i] = new Reg16U(regs8[2 * i], regs8[2 * i + 1]);
        }
        PC = new Reg16();
        IR = new Reg16();
        SP = new Reg16();
        BP = new Reg16();
        RI = new Reg8();
        RF = new Reg8();
        ports = new Reg8[32];
        for (int i = 0; i < 32; i++) {
            ports[i] = new Reg8();
        }
    }

    public void reset() {
        PC.value = 0;
    }

    public void execute(short command) {
        Executor exe = parse(command);
        exe.execute(this, command);
    }

    private Executor parse(short command) {
        System.out.printf("execute %X\n", command);
        Executor result = Executor.NOP;
        int h1 = (command & 0xf000) >> 12;
        switch (h1) {
            case 0:
                result = parseX0(command);
                break;
            case 1:

                break;
        }
        return result;
    }

    private Executor parseX0(short command) {
        Executor result = Executor.NOP;
        int h2 = (command & 0x0f00) >> 8;
        int h3 = (command & 0x00f0) >> 4;
        switch (h2) {
            case 0:
                result = Executor.NOP;
                break;
            case 1:
                switch (h3) {
                    case 4:
                        //result = Executor.CALL;
                }
                break;
            case 2:
                break;
        }
        return result;
    }
}

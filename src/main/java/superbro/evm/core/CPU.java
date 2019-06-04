package superbro.evm.core;

import superbro.evm.core.cpu.*;

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
        System.out.printf("%04X\n",command);
    }

    public void pushToStack(short v) {
        int sp = 0 | SP.value;
        if (sp <= 0x0001) {
            // TODO interrupt Stack Overflow
        }
        machine.memoryStack.data[sp] = (byte) (v >> 8);
        machine.memoryStack.data[sp + 1] = (byte) (v);
        SP.value = (short) (sp - 2);
    }

    public void pushToStack(byte v) {
        int sp = 0 | SP.value;
        if (sp == 0x0000) {
            // TODO interrupt Stack Overflow
        }
        machine.memoryStack.data[sp] = v;
        SP.value = (short) (sp - 1);
    }

    public short pop16FromStack() {
        int sp = 0 | SP.value;
        if (sp >= 0xFFFE) {
            // TODO interrupt Stack empty
        }
        short r;
        r = machine.memoryStack.data[sp];
        r |= machine.memoryStack.data[sp - 1] << 8;
        SP.value += 2;
        return r;
    }
    public byte pop8FromStack() {
        int sp = 0 | SP.value;
        if (sp == 0xFFFF) {
            // TODO interrupt Stack empty
        }
        byte r;
        r = machine.memoryStack.data[sp];
        SP.value += 1;
        return r;
    }

    public void interrupt(int n){
        boolean f = false;
        // TODO check RI
        if(!f){
            PC.value++;
            return;
        }
        pushToStack(PC.value);
        pushToStack(RF.value);
    }

    private Executor parse(short command) {
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
        int h2 = (command >> 8) & 0x0f;
        int h3 = (command >> 4) & 0x0f;
        switch (h2) {
            case 0:
                return Executor.NOP;
            case 1:
                switch (h3) {
                    case 4:
                        return Executor.CALL;
                    case 5:
                        return Executor.RET;
                    case 6:
                        return Executor.INT;
                    case 7:
                        return Executor.IRET;
                    case 8:
                        return Executor.JUMP;
                }
                break;
            case 2:
                return Executor.RCALL;
            case 3:
                return Executor.RETN;
            case 4:
                return Executor.RJUMP;
            case 5:
                return Executor.CMPr8;
            case 6:
                return Executor.CMPr16;
            case 7:
                switch (h3) {
                    case 4:
                        return Executor.TSTr8;
                    case 5:
                        return Executor.TSTr16;
                    case 0:
                        return Executor.FS;
                    case 1:
                        return Executor.FC;
                }
                break;
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
                return Executor.JMPF;
        }
        return result;
    }
}

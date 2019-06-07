package superbro.evm.core;

import superbro.evm.core.cpu.*;

public class CPU {

    private Machine machine;
    public Reg8[] regs8;
    public Reg16U[] regs16;
    public Reg8[] ports;
    public Reg8 RI, RF;
    public Reg16 PC, IR, SP, BP;
    public Reg16[] interrupts;

    CPU(Machine holder) {
        machine = holder;
        regs8 = new Reg8[8];
        regs16 = new Reg16U[4];
        interrupts = new Reg16[16];
        for (int i = 0; i < 8; i++) {
            regs8[i] = new Reg8();
        }
        for (int i = 0; i < 4; i++) {
            regs16[i] = new Reg16U(regs8[2 * i], regs8[2 * i + 1]);
        }
        for (int i = 0; i < 16; i++) {
            interrupts[i] = new Reg16();
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
        //System.out.printf("%04X\n", command);
    }

    public void stackPushWord(short v) {
        int sp = 0 | SP.value;
        if (sp <= 0x0001) {
            // TODO interrupt Stack Overflow
        }
        machine.memoryStack.data[sp] = (byte) (v >> 8);
        machine.memoryStack.data[sp + 1] = (byte) (v);
        SP.value = (short) (sp - 2);
    }

    public void stackPushByte(byte v) {
        int sp = 0 | SP.value;
        if (sp == 0x0000) {
            // TODO interrupt Stack Overflow
        }
        machine.memoryStack.data[sp] = v;
        SP.value = (short) (sp - 1);
    }

    public short stackPopWord() {
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

    public byte stackPopByte() {
        int sp = 0 | SP.value;
        if (sp == 0xFFFF) {
            // TODO interrupt Stack empty
        }
        byte r;
        r = machine.memoryStack.data[sp];
        SP.value += 1;
        return r;
    }

    public byte readByteData(int adr){
        if(adr >= 0x10000){
            // TODO interrupt Memory error
        }
        return machine.memoryData.data[adr];
    }

    public short readWordData(int adr){
        if(adr >= 0xFFFF){
            // TODO interrupt Memory error
        }
        byte t1 = machine.memoryData.data[adr];
        byte t2 = machine.memoryData.data[adr+1];
        return (short) ((t1<<8) | t2);
    }

    public void writeByteData(int adr, byte v){
        if(adr >= 0x10000){
            // TODO interrupt Memory error
        }
        machine.memoryData.data[adr] = v;
    }

    public void writeWordData(int adr, short v){
        if(adr >= 0xFFFF){
            // TODO interrupt Memory error
        }
        machine.memoryData.data[adr] = (byte) (v >> 8);
        machine.memoryData.data[adr+1] = (byte) v;
    }

    public byte stackReadByte(int offset){
        int adr = BP.value - offset;
        if(adr < 0 || adr >0xFFFF){
            // TODO interrupt Memory error
        }
        return machine.memoryStack.data[adr];
    }

    public short stackReadWord(int offset){
        int adr = BP.value - offset;
        if(adr < 0 || adr >0xFFFE){
            // TODO interrupt Memory error
        }
        byte t1 = machine.memoryData.data[adr];
        byte t2 = machine.memoryData.data[adr+1];
        return (short) ((t1<<8) | t2);
    }

    public void interrupt(int n) {
        boolean f = false;
        // TODO check RI
        if (!f) {
            PC.value++;
            return;
        }
        stackPushWord(PC.value);
        stackPushByte(RF.value);
    }

    private Executor parse(short command) {
        int h1 = (command & 0xf000) >> 12;
        switch (h1) {
            case 0:
                return parseX0(command);
            case 1:
                return Executor.AND;
            case 2:
                return Executor.OR;
            case 3:
                return Executor.XOR;
            case 4:
                return parseX4(command);
            case 5:
                return Executor.ADD;
            case 6:
                return Executor.SUB;
            case 7:
                return parseX7(command);
            case 8:
                return Executor.MOV_SWAP;
            case 9:
                return parseX9(command);
            case 10:
                return parseXA(command);
            case 11:
                return Executor.POPN;
            case 12:
                return Executor.LOADX_PLUS_I;
            case 13:
                return Executor.STORX_PLUS_I;
            case 14:
                return Executor.STOR_I;
        }
        return Executor.NOP;
    }

    private Executor parseX0(short command) {
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
            default:
                return Executor.JMPF;
        }
        return Executor.NOP;
    }

    private Executor parseX4(short command) {
        Executor result = Executor.NOP;
        int h2 = (command >> 8) & 0x0f;
        int h3 = (command >> 4) & 0x0f;
        switch (h2) {
            case 0:
                switch (h3) {
                    case 0:
                        return Executor.NOTr8;
                    case 1:
                        return Executor.NOTr16;
                    case 4:
                        return Executor.NEGr8;
                    case 5:
                        return Executor.NEGr16;
                    case 8:
                        return Executor.CLRr8;
                    case 9:
                        return Executor.CLRr16;
                    case 12:
                        return Executor.SWPH;
                }
            case 2:
                return Executor.NOP;
            case 3:
                return Executor.NOP;
            case 4:
                return Executor.NOP;
            case 5:
                return Executor.NOP;
            case 6:
                return Executor.NOP;
            case 7:
                return Executor.NOP;
            case 8:
                return Executor.NOP;
            case 9:
                return Executor.NOP;
            case 10:
                return Executor.NOP;
            case 11:
                return Executor.NOP;
        }
        return result;
    }

    private Executor parseX7(short command) {
        int h2 = (command >> 8) & 0x0f;
        switch (h2) {
            case 0:
            case 1:
                return Executor.NOP;
            case 2:
                return Executor.MULr8;
            case 3:
                return Executor.MULr16;
            case 4:
                return Executor.DIVr8;
            case 5:
                return Executor.DIVr16;
            case 6:
                return Executor.MODr8;
            case 7:
                return Executor.MODr16;
            default:
                return Executor.CMPi;
        }
    }

    private Executor parseX9(short command) {
        int h2 = (command >> 8) & 0x0f;
        switch (h2) {
            case 0:
                return Executor.LOAD;
            case 2:
                return Executor.LOADX;
            case 3:
                return Executor.LOADX_PLUS;
            case 4:
            case 5:
            case 6:
            case 7:
                return Executor.LOAD_PLUS;
            case 8:
                return Executor.STOR;
            case 9:
                return Executor.STORX;
            case 10:
                return Executor.STORX_PLUS;
            case 12:
            case 13:
            case 14:
            case 15:
                return Executor.STOR_PLUS;
            default:
                return Executor.NOP;
        }
    }

    private Executor parseXA(short command) {
        int h2 = (command >> 8) & 0x0f;
        int h3 = (command >> 4) & 0x0f;
        switch (h2) {
            case 1:
                switch(h3){
                    case 0: return Executor.LOADF;
                    case 1: return Executor.STORF;
                }
                return Executor.NOP;
            case 4:
                return Executor.PUT;
            case 6:
                return Executor.GET;
            case 8:
                switch(h3){
                    case 0: return Executor.PUSHr8;
                    case 1: return Executor.PUSHr16;
                }
                return Executor.NOP;
            case 9:
                return Executor.PUSHi;
            case 10:
                switch(h3){
                    case 0: return Executor.POPr8;
                    case 1: return Executor.POPr16;
                }
                return Executor.NOP;
            default:
                return Executor.NOP;
        }
    }
}

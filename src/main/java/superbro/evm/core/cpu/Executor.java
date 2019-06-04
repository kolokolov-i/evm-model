package superbro.evm.core.cpu;

import superbro.evm.core.CPU;

public abstract class Executor {

    public abstract void execute(CPU cpu, short command);

    public static Executor
            NOP = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value++;
                }
            },
            CALL = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.pushToStack(cpu.PC.value);
                    cpu.PC.value = cpu.regs16[Code.extRM(com, 0)].getValue();
                    // TODO CALL regBP=?
                }
            },
            RCALL = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.pushToStack(cpu.PC.value);
                    cpu.PC.value += Code.extN8(com);
                    // TODO RCALL regBP=?
                }
            },
            RET = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value = cpu.pop16FromStack();
                    // TODO RET regBP=?
                }
            },
            RETN = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value = cpu.pop16FromStack();
                    int t = Code.extN8(com);
                    if(cpu.SP.value+t > 0xFFFF){
                        // TODO interrupt Stack empty
                    }
                    cpu.SP.value += t;
                    // TODO RETN regBP=?
                }
            },
            INT = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.interrupt(Code.extN4(com, 0));
                }
            },
            IRET = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.RF.value = cpu.pop8FromStack();
                    cpu.PC.value = cpu.pop16FromStack();
                }
            },
            JUMP = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value = cpu.regs16[Code.extRM(com, 0)].getValue();
                }
            },
            RJUMP = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value += Code.extN8(com);
                }
            },
            JMPF = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = Code.extN4(com, 8);
                    byte f = cpu.RF.value;
                    boolean r = false;
                    switch(m){
                        case 8:
                            r = (f & 0x01) != 0;
                            break;
                        case 9:
                            r = (f & 0x02) != 0;
                            break;
                        case 10:
                            r = (f & 0x04) != 0;
                            break;
                        case 11:
                            r = (f & 0x08) != 0;
                            break;
                        case 12:
                            r = (f & 0x01) == 0;
                            break;
                        case 13:
                            r = (f & 0x02) == 0;
                            break;
                        case 14:
                            r = (f & 0x04) == 0;
                            break;
                        case 15:
                            r = (f & 0x08) == 0;
                            break;
                    }
                    if(r){
                        cpu.PC.value += Code.extN8(com);
                    }
                    else{
                        cpu.PC.value++;
                    }
                }
            },
            CMPr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    byte a = cpu.regs8[Code.extR8(com, 4)].value;
                    byte b = cpu.regs8[Code.extR8(com, 0)].value;
                    byte c = (byte) (a - b);
                    if(c==0){
                        cpu.RF.value |= 0x01;
                    }
                    if(c<0){
                        cpu.RF.value |= 0x02;
                    }
                    // TODO flag C=?
                    // TODO flag O=?
                    cpu.PC.value++;
                }
            },
            CMPr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    short a = cpu.regs16[Code.extRM(com, 4)].getValue();
                    short b = cpu.regs16[Code.extRM(com, 0)].getValue();
                    short c = (short) (a - b);
                    if(c==0){
                        cpu.RF.value |= 0x01;
                    }
                    if(c<0){
                        cpu.RF.value |= 0x02;
                    }
                    // TODO flag C=?
                    // TODO flag O=?
                    cpu.PC.value++;
                }
            },
            TSTr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    byte a = cpu.regs8[Code.extR8(com, 0)].value;
                    if(a==0){
                        cpu.RF.value |= 0x01;
                    }
                    if(a<0){
                        cpu.RF.value |= 0x02;
                    }
                    cpu.PC.value++;
                }
            },
            TSTr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    short a = cpu.regs16[Code.extRM(com, 0)].getValue();
                    if(a==0){
                        cpu.RF.value |= 0x01;
                    }
                    if(a<0){
                        cpu.RF.value |= 0x02;
                    }
                    cpu.PC.value++;
                }
            },
            FS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = Code.extN4(com, 0)-1;
                    cpu.RF.value |= (1 << m);
                    cpu.PC.value++;
                }
            },
            FC = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = Code.extN4(com, 0)-1;
                    int f = 0xFF;
                    switch(m){
                        case 1: f = 0xFE; break;
                        case 2: f = 0xFD; break;
                        case 3: f = 0xFB; break;
                        case 4: f = 0xF7; break;
                        case 5: f = 0xEF; break;
                        case 6: f = 0xDF; break;
                    }
                    cpu.RF.value &= f;
                    cpu.PC.value++;
                }
            };
}

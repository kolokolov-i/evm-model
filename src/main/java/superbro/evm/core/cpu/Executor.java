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
                    cpu.stackPushWord(cpu.PC.value);
                    cpu.PC.value = cpu.regs16[Code.extRM(com, 0)].getValue();
                    // TODO CALL regBP=?
                }
            },
            RCALL = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.stackPushWord(cpu.PC.value);
                    cpu.PC.value += Code.extN8(com);
                    // TODO RCALL regBP=?
                }
            },
            RET = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value = cpu.stackPopWord();
                    // TODO RET regBP=?
                }
            },
            RETN = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.PC.value = cpu.stackPopWord();
                    int t = Code.extN8(com);
                    if (cpu.SP.value + t > 0xFFFF) {
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
                    cpu.RF.value = cpu.stackPopByte();
                    cpu.PC.value = cpu.stackPopWord();
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
                    switch (m) {
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
                    if (r) {
                        cpu.PC.value += Code.extN8(com);
                    } else {
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
                    if (c == 0) {
                        cpu.RF.value |= 0x01;
                    }
                    if (c < 0) {
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
                    if (c == 0) {
                        cpu.RF.value |= 0x01;
                    }
                    if (c < 0) {
                        cpu.RF.value |= 0x02;
                    }
                    // TODO flag C=?
                    // TODO flag O=?
                    cpu.PC.value++;
                }
            },
            CMPi = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    byte a = cpu.regs8[Code.extR8(com, 8)].value;
                    byte b = Code.extN8(com);
                    byte c = (byte) (a - b);
                    if (c == 0) {
                        cpu.RF.value |= 0x01;
                    }
                    if (c < 0) {
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
                    if (a == 0) {
                        cpu.RF.value |= 0x01;
                    }
                    if (a < 0) {
                        cpu.RF.value |= 0x02;
                    }
                    cpu.PC.value++;
                }
            },
            TSTr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    short a = cpu.regs16[Code.extRM(com, 0)].getValue();
                    if (a == 0) {
                        cpu.RF.value |= 0x01;
                    }
                    if (a < 0) {
                        cpu.RF.value |= 0x02;
                    }
                    cpu.PC.value++;
                }
            },
            FS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = Code.extN4(com, 0) - 1;
                    cpu.RF.value |= (1 << m);
                    cpu.PC.value++;
                }
            },
            FC = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = Code.extN4(com, 0) - 1;
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
            },
            AND = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int t = (com >> 8) & 0x0f;
                    if((t & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        byte n = Code.extN8(com);
                        cpu.regs8[r8].value &= n;
                    }
                    else if(t == 0){
                        int r1 = Code.extR8(com, 4);
                        int r2 = Code.extR8(com, 0);
                        cpu.regs8[r1].value &= cpu.regs8[r2].value;
                    }
                    else if(t == 1){
                        int r1 = Code.extRM(com, 4);
                        int r2 = Code.extRM(com, 0);
                        cpu.regs16[r1].rH.value &= cpu.regs16[r2].rH.value;
                        cpu.regs16[r1].rL.value &= cpu.regs16[r2].rL.value;
                    }
                    cpu.PC.value++;
                }
            },
            OR = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int t = (com >> 8) & 0x0f;
                    if((t & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        byte n = Code.extN8(com);
                        cpu.regs8[r8].value |= n;
                    }
                    else if(t == 0){
                        int r1 = Code.extR8(com, 4);
                        int r2 = Code.extR8(com, 0);
                        cpu.regs8[r1].value |= cpu.regs8[r2].value;
                    }
                    else if(t == 1){
                        int r1 = Code.extRM(com, 4);
                        int r2 = Code.extRM(com, 0);
                        cpu.regs16[r1].rH.value |= cpu.regs16[r2].rH.value;
                        cpu.regs16[r1].rL.value |= cpu.regs16[r2].rL.value;
                    }
                    cpu.PC.value++;
                }
            },
            XOR = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int t = (com >> 8) & 0x0f;
                    if((t & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        byte n = Code.extN8(com);
                        cpu.regs8[r8].value ^= n;
                    }
                    else if(t == 0){
                        int r1 = Code.extR8(com, 4);
                        int r2 = Code.extR8(com, 0);
                        cpu.regs8[r1].value ^= cpu.regs8[r2].value;
                    }
                    else if(t == 1){
                        int r1 = Code.extRM(com, 4);
                        int r2 = Code.extRM(com, 0);
                        cpu.regs16[r1].rH.value ^= cpu.regs16[r2].rH.value;
                        cpu.regs16[r1].rL.value ^= cpu.regs16[r2].rL.value;
                    }
                    cpu.PC.value++;
                }
            },
            NOTr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r8 = Code.extR8(com, 0);
                    cpu.regs8[r8].value = (byte) ~cpu.regs8[r8].value;
                    cpu.PC.value++;
                }
            },
            NOTr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int rn = Code.extRM(com, 0);
                    cpu.regs16[rn].rH.value = (byte) ~cpu.regs16[rn].rH.value;
                    cpu.regs16[rn].rL.value = (byte) ~cpu.regs16[rn].rL.value;
                    cpu.PC.value++;
                }
            },
            NEGr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r8 = Code.extR8(com, 0);
                    cpu.regs8[r8].value = (byte) -cpu.regs8[r8].value;
                    cpu.PC.value++;
                }
            },
            NEGr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int rn = Code.extRM(com, 0);
                    cpu.regs16[rn].rH.value = (byte) -cpu.regs16[rn].rH.value;
                    cpu.regs16[rn].rL.value = (byte) -cpu.regs16[rn].rL.value;
                    cpu.PC.value++;
                }
            },
            CLRr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r8 = Code.extR8(com, 0);
                    cpu.regs8[r8].value = (byte) 0;
                    cpu.PC.value++;
                }
            },
            CLRr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int rn = Code.extRM(com, 0);
                    cpu.regs16[rn].rH.value = (byte) 0;
                    cpu.regs16[rn].rL.value = (byte) 0;
                    cpu.PC.value++;
                }
            },
            SWPH = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r8 = Code.extR8(com, 0);
                    int t = cpu.regs8[r8].value;
                    cpu.regs8[r8].value = (byte) (((t >> 4) & 0xf) | ((t & 0x0f) << 4));
                    cpu.PC.value++;
                }
            },
            ADD = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int t = (com >> 8) & 0x0f;
                    if((t & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        byte n = Code.extN8(com);
                        cpu.regs8[r8].value += n;
                    }
                    else {
                        int r1, r2;
                        switch(t){
                            case 0:
                                r1 = Code.extR8(com, 4);
                                r2 = Code.extR8(com, 0);
                                cpu.regs8[r1].value += cpu.regs8[r2].value;
                                break;
                            case 1:
                                r1 = Code.extRM(com, 4);
                                r2 = Code.extRM(com, 0);
                                cpu.regs8[r1].value += cpu.regs8[r2].value;
                                break;
                            case 2:
                                r1 = Code.extR8(com, 0);
                                cpu.regs8[r1].value++;
                                break;
                            case 3:
                                r1 = Code.extRM(com, 0);
                                short value = cpu.regs16[r1].getValue();
                                value++;
                                cpu.regs16[r1].setValue(value);
                                break;
                        }
                    }
                    cpu.PC.value++;
                }
            },
            SUB = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int t = (com >> 8) & 0x0f;
                    if((t & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        byte n = Code.extN8(com);
                        cpu.regs8[r8].value -= n;
                    }
                    else {
                        int r1, r2;
                        switch(t){
                            case 0:
                                r1 = Code.extR8(com, 4);
                                r2 = Code.extR8(com, 0);
                                cpu.regs8[r1].value -= cpu.regs8[r2].value;
                                break;
                            case 1:
                                r1 = Code.extRM(com, 4);
                                r2 = Code.extRM(com, 0);
                                cpu.regs8[r1].value -= cpu.regs8[r2].value;
                                break;
                            case 2:
                                r1 = Code.extR8(com, 0);
                                cpu.regs8[r1].value--;
                                break;
                            case 3:
                                r1 = Code.extRM(com, 0);
                                short value = cpu.regs16[r1].getValue();
                                value--;
                                cpu.regs16[r1].setValue(value);
                                break;
                        }
                    }
                    cpu.PC.value++;
                }
            },
            MULr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extR8(com, 0);
                    cpu.regs8[r1].value *= cpu.regs8[r2].value;
                    cpu.PC.value++;
                }
            },
            MULr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 4);
                    int r2 = Code.extRM(com, 0);
                    short a = cpu.regs16[r1].getValue();
                    short b = cpu.regs16[r2].getValue();
                    cpu.regs16[r1].setValue(a*b);
                    cpu.PC.value++;
                }
            },
            DIVr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extR8(com, 0);
                    cpu.regs8[r1].value /= cpu.regs8[r2].value;
                    cpu.PC.value++;
                }
            },
            DIVr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 4);
                    int r2 = Code.extRM(com, 0);
                    short a = cpu.regs16[r1].getValue();
                    short b = cpu.regs16[r2].getValue();
                    cpu.regs16[r1].setValue(a/b);
                    cpu.PC.value++;
                }
            },
            MODr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extR8(com, 0);
                    cpu.regs8[r1].value %= cpu.regs8[r2].value;
                    cpu.PC.value++;
                }
            },
            MODr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 4);
                    int r2 = Code.extRM(com, 0);
                    short a = cpu.regs16[r1].getValue();
                    short b = cpu.regs16[r2].getValue();
                    cpu.regs16[r1].setValue(a%b);
                    cpu.PC.value++;
                }
            },
            MOV_SWAP = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = (com >> 8) & 0x0f;
                    if((m & 0x8) != 0){
                        int r8 = Code.extR8(com, 8);
                        cpu.regs8[r8].value = Code.extN8(com);
                    }
                    else {
                        int r1, r2;
                        switch(m){
                            case 0:
                                r1 = Code.extR8(com, 4);
                                r2 = Code.extR8(com, 0);
                                cpu.regs8[r1].value = cpu.regs8[r2].value;
                                break;
                            case 1:
                                r1 = Code.extRM(com, 4);
                                r2 = Code.extRM(com, 0);
                                cpu.regs8[r1].value = cpu.regs8[r2].value;
                                break;
                            case 2:
                                r1 = Code.extR8(com, 4);
                                r2 = Code.extR8(com, 0);
                                byte t1 = cpu.regs8[r1].value;
                                cpu.regs8[r1].value = cpu.regs8[r2].value;
                                cpu.regs8[r2].value = t1;
                                break;
                            case 3:
                                r1 = Code.extRM(com, 4);
                                r2 = Code.extRM(com, 0);
                                t1 = cpu.regs16[r1].rH.value;
                                cpu.regs16[r1].rH.value = cpu.regs16[r2].rH.value;
                                cpu.regs16[r2].rH.value = t1;
                                t1 = cpu.regs16[r1].rL.value;
                                cpu.regs16[r1].rL.value = cpu.regs16[r2].rL.value;
                                cpu.regs16[r2].rL.value = t1;
                                break;
                        }
                    }
                    cpu.PC.value++;
                }
            },
            LOAD = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extRM(com, 0);
                    int adr = cpu.regs16[r2].getValue();
                    cpu.regs8[r1].value = cpu.readByteData(adr);
                    cpu.PC.value++;
                }
            },
            LOADX = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 4);
                    int r2 = Code.extRM(com, 0);
                    int adr = cpu.regs16[r2].getValue();
                    cpu.regs16[r1].setValue(cpu.readWordData(adr));
                    cpu.PC.value++;
                }
            },
            LOADX_PLUS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 2);
                    int r2 = Code.extRM(com, 0);
                    int r3 = Code.extR8(com, 4);
                    int adr = cpu.regs16[r2].getValue() + cpu.regs8[r3].value;
                    cpu.regs16[r1].setValue(cpu.readWordData(adr));
                    cpu.PC.value++;
                }
            },
            LOAD_PLUS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extRM(com, 8);
                    int r3 = Code.extR8(com, 0);
                    int adr = cpu.regs16[r2].getValue() + cpu.regs8[r3].value;
                    cpu.regs8[r1].value = cpu.readByteData(adr);
                    cpu.PC.value++;
                }
            },
            STOR = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    int r2 = Code.extRM(com, 4);
                    int adr = cpu.regs16[r2].getValue();
                    cpu.writeByteData(adr, cpu.regs8[r1].value);
                    cpu.PC.value++;
                }
            },
            STORX = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 4);
                    int r2 = Code.extRM(com, 0);
                    int adr = cpu.regs16[r1].getValue();
                    cpu.writeWordData(adr, cpu.regs16[r2].getValue());
                    cpu.PC.value++;
                }
            },
            STORX_PLUS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 4);
                    int r2 = Code.extRM(com, 2);
                    int r3 = Code.extRM(com, 0);
                    int adr = cpu.regs16[r2].getValue() + cpu.regs8[r1].value;
                    cpu.writeWordData(adr, cpu.regs16[r3].getValue());
                    cpu.PC.value++;
                }
            },
            STOR_PLUS = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 8);
                    int r2 = Code.extR8(com, 4);
                    int r3 = Code.extR8(com, 0);
                    int adr = cpu.regs16[r1].getValue() + cpu.regs8[r2].value;
                    cpu.writeByteData(adr, cpu.regs8[r3].value);
                    cpu.PC.value++;
                }
            },
            LOADF = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    cpu.regs8[r1].value = cpu.RF.value;
                    cpu.PC.value++;
                }
            },
            STORF = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    cpu.RF.value = cpu.regs8[r1].value;
                    cpu.PC.value++;
                }
            },
            PUT = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    int r2 = Code.extN5(com, 3);
                    cpu.ports[r2].value = cpu.regs8[r1].value;
                    cpu.PC.value++;
                }
            },
            GET = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    int r2 = Code.extN5(com, 3);
                    cpu.regs8[r1].value = cpu.ports[r2].value;
                    cpu.PC.value++;
                }
            },
            PUSHr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    cpu.stackPushByte(cpu.regs8[r1].value);
                    cpu.PC.value++;
                }
            },
            PUSHr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 0);
                    cpu.stackPushByte(cpu.regs16[r1].rL.value);
                    cpu.stackPushByte(cpu.regs16[r1].rH.value);
                    cpu.PC.value++;
                }
            },
            PUSHi = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    cpu.stackPushByte(Code.extN8(com));
                    cpu.PC.value++;
                }
            },
            POPr8 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extR8(com, 0);
                    cpu.regs8[r1].value = cpu.stackPopByte();
                    cpu.PC.value++;
                }
            },
            POPr16 = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 0);
                    cpu.regs16[r1].rH.value = cpu.stackPopByte();
                    cpu.regs16[r1].rL.value = cpu.stackPopByte();
                    cpu.PC.value++;
                }
            },
            POPN = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int m = com & 0x0800;
                    if(m==0){
                        int r1 = Code.extR8(com, 8);
                        int n = Code.extN8(com);
                        cpu.regs8[r1].value = cpu.stackReadByte(n);
                    }
                    else{
                        int r1 = Code.extRM(com, 8);
                        int n = Code.extN8(com);
                        cpu.regs16[r1].setValue(cpu.stackReadWord(n));
                    }
                    cpu.PC.value++;
                }
            },
            LOADX_PLUS_I = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 10);
                    int r2 = Code.extRM(com, 8);
                    int adr = cpu.regs16[r2].getValue() + Code.extN8(com);
                    cpu.regs16[r1].setValue(cpu.readWordData(adr));
                    cpu.PC.value++;
                }
            },
            STORX_PLUS_I = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 10);
                    int r2 = Code.extRM(com, 8);
                    int adr = cpu.regs16[r1].getValue() + Code.extN8(com);
                    cpu.writeWordData(adr, cpu.regs16[r2].getValue());
                    cpu.PC.value++;
                }
            },
            STOR_I = new Executor() {
                @Override
                public void execute(CPU cpu, short com) {
                    int r1 = Code.extRM(com, 8);
                    int adr = cpu.regs16[r1].getValue();
                    cpu.writeByteData(adr, Code.extN8(com));
                    cpu.PC.value++;
                }
            };
}

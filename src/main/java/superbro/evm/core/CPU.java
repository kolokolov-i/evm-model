package superbro.evm.core;

import superbro.evm.core.cpu.*;

import java.util.HashMap;
import java.util.Map;

public class CPU {

    private Machine machine;
    public Map<Register, Reg> regs;
    public Reg8[] ports;

    CPU(Machine holder){
        machine = holder;
        regs = new HashMap<>();
        Reg8 r0 = new Reg8();
        Reg8 r1 = new Reg8();
        Reg8 r2 = new Reg8();
        Reg8 r3 = new Reg8();
        Reg8 r4 = new Reg8();
        Reg8 r5 = new Reg8();
        Reg8 r6 = new Reg8();
        Reg8 r7 = new Reg8();
        regs.put(Register.R0, r0);
        regs.put(Register.R1, r1);
        regs.put(Register.R2, r2);
        regs.put(Register.R3, r3);
        regs.put(Register.R4, r4);
        regs.put(Register.R5, r5);
        regs.put(Register.R6, r6);
        regs.put(Register.R7, r7);
        regs.put(Register.RM0, new Reg16U(r0, r1));
        regs.put(Register.RM1, new Reg16U(r2, r3));
        regs.put(Register.RM2, new Reg16U(r4, r5));
        regs.put(Register.RM3, new Reg16U(r6, r7));
        regs.put(Register.PC, new Reg16());
        regs.put(Register.IR, new Reg16());
        regs.put(Register.SP, new Reg16());
        regs.put(Register.BP, new Reg16());
        regs.put(Register.RI, new Reg8());
        regs.put(Register.RF, new Reg8());
        ports = new Reg8[32];
        for(int i = 0; i<32; i++){
            ports[i] = new Reg8();
        }
    }

    public void reset(){
        ((Reg16)regs.get(Register.PC)).value = 0;
    }

    public void execute(short command){
        Executor exe = parse(command);
        exe.execute(this, command);
    }

    private Executor parse(short command) {
        Executor result = Executor.NOP;
        int h1 = (command & 0xf000) >> 12;
        switch(h1){
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
        switch(h2){
            case 0:
                result = Executor.NOP;
                break;
            case 1:
                switch(h3){
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

package superbro.evm.translator.asm;

import superbro.evm.translator.asm.cmd.*;

import java.util.ArrayList;
import java.util.HashMap;

class Instruct {

    Command command;
    Argument argument1, argument2;

    private static HashMap<String, Command> commandMap;

    static {
        commandMap = new HashMap<>();
        commandMap.put("NOP", new NOP());
        commandMap.put("CALL", new CALL());
        commandMap.put("RCALL", new RCALL());
        commandMap.put("RET", new RET());
        commandMap.put("RETN", new RETN());
        commandMap.put("INT", new INT());
        commandMap.put("IRET", new IRET());
        commandMap.put("JUMP", new JUMP());
        commandMap.put("RJMP", new RJMP());
        commandMap.put("RJMPZ", new RJMPZ());
        commandMap.put("RJMPS", new RJMPS());
        commandMap.put("RJMPC", new RJMPC());
        commandMap.put("RJMPO", new RJMPO());
        commandMap.put("RJMPNZ", new RJMPNZ());
        commandMap.put("RJMPNS", new RJMPNS());
        commandMap.put("RJMPNC", new RJMPNC());
        commandMap.put("RJMPNO", new RJMPNO());
        commandMap.put("CMP", new CMP());
        commandMap.put("CMPI", new CMPI());
        commandMap.put("TST", new TST());
        commandMap.put("FSZ", new FSZ());
        commandMap.put("FSS", new FSS());
        commandMap.put("FSC", new FSC());
        commandMap.put("FSO", new FSO());
        commandMap.put("FST", new FST());
        commandMap.put("FSI", new FSI());
        commandMap.put("FCZ", new FCZ());
        commandMap.put("FCS", new FCS());
        commandMap.put("FCC", new FCC());
        commandMap.put("FCO", new FCO());
        commandMap.put("FCT", new FCT());
        commandMap.put("FCI", new FCI());
        commandMap.put("AND", new AND());
        commandMap.put("ANDI", new ANDI());
        commandMap.put("OR", new OR());
        commandMap.put("ORI", new ORI());
        commandMap.put("XOR", new XOR());
        commandMap.put("XORI", new XORI());
        commandMap.put("NOT", new NOT());
        commandMap.put("NEG", new NEG());
        commandMap.put("CLR", new CLR());
        commandMap.put("SWPH", new SWPH());
        commandMap.put("LSL", new LSL());
        commandMap.put("LSR", new LSR());
        commandMap.put("ROL", new ROL());
        commandMap.put("ROR", new ROR());
        commandMap.put("ASR", new ASR());
        commandMap.put("ADD", new ADD());
        commandMap.put("ADDI", new ADDI());
        commandMap.put("INC", new INC());
        commandMap.put("SUB", new SUB());
        commandMap.put("SUBI", new SUBI());
        commandMap.put("DEC", new DEC());
        commandMap.put("MUL", new MUL());
        commandMap.put("DIV", new DIV());
        commandMap.put("MOD", new MOD());
        commandMap.put("MOV", new MOV());
        commandMap.put("MOVI", new MOVI());
        commandMap.put("SWAP", new SWAP());
    }

    private static HashMap<String, Argument> words;

    static {
        words = new HashMap<>();
        for(int i=0; i<8; i++){
            words.put("R"+i, new Argument(Type.REG8, i));
        }
        for(int i=0; i<4; i++){
            words.put("RM"+i, new Argument(Type.REG16, i));
        }
        for(int i=0; i<32; i++){
            words.put("P"+i, new Argument(Type.PORT, i));
        }
    }

    private Instruct(String name) throws ParserException {
        String cname = name.toUpperCase();
        if (!commandMap.containsKey(cname)) {
            throw new ParserException("Unknown command \'" + name + "\'");
        }
        this.command = commandMap.get(cname);
    }

    void generate(ArrayList<Short> r) throws ParserException {
        this.command.generate(r, this.argument1, this.argument2);
    }

    static Instruct create(String com) throws ParserException {
        Instruct r = new Instruct(com);
        r.argument1 = Argument.voidArg;
        r.argument2 = Argument.voidArg;
        return r;
    }

    static Instruct create(String com, String arg) throws ParserException {
        Instruct r = new Instruct(com);
        Argument arg1;
        arg1 = words.get(arg.toUpperCase());
        if(arg1!=null){
            r.argument1 = arg1;
        }
        else{
            throw new ParserException("Unknown variable " + arg);
        }
        r.argument2 = Argument.voidArg;
        return r;
    }

    static Instruct create(String com, int arg) throws ParserException {
        Instruct r = new Instruct(com);
        r.argument1 = new Argument(Type.NUMBER, arg);
        r.argument2 = Argument.voidArg;
        return r;
    }

    static Instruct create(String com, String arg1s, String arg2s) throws ParserException {
        Instruct r = new Instruct(com);
        Argument arg1, arg2;
        arg1 = words.get(arg1s.toUpperCase());
        if(arg1!=null){
            r.argument1 = arg1;
        }
        else{
            throw new ParserException("Unknown variable " + arg1s);
        }
        arg2 = words.get(arg2s.toUpperCase());
        if(arg2!=null){
            r.argument2 = arg2;
        }
        else{
            throw new ParserException("Unknown variable " + arg2s);
        }
        return r;
    }

    static Instruct create(String com, String arg1s, int arg2n) throws ParserException {
        Instruct r = new Instruct(com);
        Argument arg1;
        arg1 = words.get(arg1s.toUpperCase());
        if(arg1!=null){
            r.argument1 = arg1;
        }
        else{
            throw new ParserException("Unknown variable " + arg1s);
        }
        r.argument2 = new Argument(Type.NUMBER, arg2n);
        return r;
    }

    static Instruct create(String com, int arg1n, String arg2s) throws ParserException {
        Instruct r = new Instruct(com);
        r.argument2 = new Argument(Type.NUMBER, arg1n);
        Argument arg2;
        arg2 = words.get(arg2s.toUpperCase());
        if(arg2!=null){
            r.argument2 = arg2;
        }
        else{
            throw new ParserException("Unknown variable " + arg2s);
        }
        return r;
    }

    static Instruct create(String com, int arg1n, int arg2n) throws ParserException {
        Instruct r = new Instruct(com);
        r.argument1 = new Argument(Type.NUMBER, arg1n);
        r.argument2 = new Argument(Type.NUMBER, arg2n);
        return r;
    }
}

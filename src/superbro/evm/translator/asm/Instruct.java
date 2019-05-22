package superbro.evm.translator.asm;

import superbro.evm.translator.Messager;
import superbro.evm.translator.asm.cmd.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Instruct {

    private static HashMap<String, Command> commandMap;
    private static List<Instruct> instructs;

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
    private static Map<String, Label> labels;
    private static Map<String, Argument.Address> vars;

    static {
        words = new HashMap<>();
        for (int i = 0; i < 8; i++) {
            words.put("R" + i, new Argument.Reg8(i));
        }
        for (int i = 0; i < 4; i++) {
            words.put("RM" + i, new Argument.Reg16(i));
        }
        for (int i = 0; i < 32; i++) {
            words.put("P" + i, new Argument.Port(i));
        }
        labels = new HashMap<>();
    }

    static void reset(Map<String, Argument.Address> v) {
        labels.clear();
        vars = v;
    }

    int line;
    int offset;
    private Command command;
    private Token argument1, argument2;

    Instruct(Word label, Word name, Token arg1, Token arg2) throws ParserException {
        line = name.line;
        String cname = name.lexeme.toUpperCase();
        if (!commandMap.containsKey(cname)) {
            throw new ParserException("Unknown command \'" + name.lexeme + "\'");
        }
        this.command = commandMap.get(cname);
        if (label != null) {
            String s = label.lexeme.toUpperCase();
            if (labels.containsKey(s)) {
                throw new ParserException("Label " + s + "is already defined");
            }
            labels.put(s, new Label(label, this));
        }
        argument1 = arg1;
        argument2 = arg2;
    }

    public static void generate(List<Instruct> instructs, ArrayList<Short> raw, Messager messager) {
        Instruct.instructs = instructs;
        calculateOffsets();
        for (Instruct j : instructs) {
            try {
                j.generate(raw);
            } catch (ParserException ex) {
                switch(ex.type){
                    case ERROR:
                        messager.error(j.line, 0, ex.getMessage());
                        break;
                    case WARNING:
                        messager.warning(j.line, 0, ex.getMessage());
                }
            }
        }
    }

    private static void calculateOffsets() {
        int t = 0;
        for (Instruct ins : instructs) {
            ins.offset = t;
            t += ins.command.getSize(ins.argument1, ins.argument2);
        }
    }

    void generate(ArrayList<Short> r) throws ParserException {
        Argument arg1 = resoluteArgument(argument1);
        Argument arg2 = resoluteArgument(argument2);
        this.command.generate(r, arg1, arg2);
    }

    private Argument resoluteArgument(Token token) throws ParserException {
        if (token == null) {
            return Argument.voidArg;
        }
        if (token.tag == Tag.NUMBER) {
            return new Argument.Number(((Number) token).value);
        }
        if (token.tag == Tag.ID) {
            String s = ((Word) token).lexeme.toUpperCase();
            if (words.containsKey(s)) {
                return words.get(s);
            }
            if (labels.containsKey(s)) {
                int offset = this.offset - labels.get(s).target.offset;
                if (offset > 127 || offset < -128) {
                    throw new ParserException("Label is unreachable");
                }
                return new Argument.RelAddress((byte) offset);
            }
            if (vars.containsKey(s)) {
                return vars.get(s);
            }
            throw new ParserException("Unknown identifier " + ((Word) token).lexeme);
        }
        if (token.tag == Tag.INDEX) {
            IndexToken tok = (IndexToken) token;
            Token mToken = tok.master;
            Argument mArg1;
            if (mToken.tag == Tag.ID) {
                String s = ((Word) mToken).lexeme.toUpperCase();
                if (words.containsKey(s)) {
                    mArg1 = words.get(s);
                } else {
                    throw new ParserException("Unknown identifier " + ((Word) mToken).lexeme);
                }
            } else {
                throw new ParserException("Invalid index type");
            }
            if (tok.slave == null) {
                return new Argument.Index(mArg1);
            }
            mToken = tok.slave;
            Argument mArg2;
            if (mToken.tag == Tag.ID) {
                String s = ((Word) mToken).lexeme.toUpperCase();
                if (words.containsKey(s)) {
                    mArg2 = words.get(s);
                } else {
                    throw new ParserException("Unknown identifier " + ((Word) mToken).lexeme);
                }
            } else if (mToken.tag == Tag.NUMBER) {
                mArg2 = new Argument.Number(((Number) mToken).value);
            } else {
                throw new ParserException("Invalid index type");
            }
            return new Argument.IndexPlus(mArg1, mArg2);
        }
        throw new ParserException("Invalid argument");
    }
}

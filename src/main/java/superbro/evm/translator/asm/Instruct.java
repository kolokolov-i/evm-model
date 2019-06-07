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
        commandMap.put("JMPZ", new RJMPZ());
        commandMap.put("JMPS", new RJMPS());
        commandMap.put("JMPC", new RJMPC());
        commandMap.put("JMPO", new RJMPO());
        commandMap.put("JMPNZ", new RJMPNZ());
        commandMap.put("JMPNS", new RJMPNS());
        commandMap.put("JMPNC", new RJMPNC());
        commandMap.put("JMPNO", new RJMPNO());
        commandMap.put("CMP", new CMP());
        commandMap.put("CMPi", new CMPI());
        commandMap.put("TST", new TST());
        commandMap.put("FSZ", new FXX(true, 1));
        commandMap.put("FSS", new FXX(true, 2));
        commandMap.put("FXX", new FXX(true, 3));
        commandMap.put("FSO", new FXX(true, 4));
        commandMap.put("FST", new FXX(true, 5));
        commandMap.put("FSI", new FXX(true, 6));
        commandMap.put("FCZ", new FXX(false, 1));
        commandMap.put("FCS", new FXX(false, 2));
        commandMap.put("FCC", new FXX(false, 3));
        commandMap.put("FCO", new FXX(false, 4));
        commandMap.put("FCT", new FXX(false, 5));
        commandMap.put("FCI", new FXX(false, 6));
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
        commandMap.put("LOAD", new LOAD());
        commandMap.put("LOADX", new LOADX());
        commandMap.put("STOR", new STOR());
        commandMap.put("STORX", new STORX());
        commandMap.put("STORI", new STORI());
        commandMap.put("LOADF", new LOADF());
        commandMap.put("STORF", new STORF());
        commandMap.put("PUT", new PUT());
        commandMap.put("GET", new GET());
        commandMap.put("PUSH", new PUSH());
        commandMap.put("PUSHI", new PUSHI());
        commandMap.put("POP", new POP());
    }

    private static HashMap<String, Argument> words;
    private static Map<String, Label> labels;
    private static List<Argument.RelAddress> unresolvedLinks;
    private static Map<String, Argument.Address> vars;

    static {
        words = new HashMap<>();
        Argument.Reg8[] rr8 = new Argument.Reg8[8];
        for (int i = 0; i < 8; i++) {
            rr8[i] = new Argument.Reg8(i);
            words.put("R" + i, rr8[i]);
        }
        for (int i = 0; i < 4; i++) {
            words.put("RM" + i, new Argument.Reg16(i, rr8[2 * i], rr8[2 * i + 1]));
        }
        for (int i = 0; i < 32; i++) {
            words.put("P" + i, new Argument.Port(i));
        }
        labels = new HashMap<>();
        unresolvedLinks = new ArrayList<>();
    }

    static void reset(Map<String, Argument.Address> v) {
        labels.clear();
        unresolvedLinks.clear();
        vars = v;
    }

    int line, offset, size;
    private Command command;
    private Argument argument1, argument2;
    private Word opcode;
    private Token arg1Token, arg2Token;

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
        opcode = name;
        arg1Token = arg1;
        arg2Token = arg2;
        argument1 = resoluteArgument(arg1Token);
        argument2 = resoluteArgument(arg2Token);
        size = command.getSize(argument1, argument2);
    }

    public static void generate(List<Instruct> instructs, ArrayList<Short> raw, StringBuilder listiner, Messager messager) {
        Instruct.instructs = instructs;
        calculateOffsets(messager);
        int i = 0;
        for (Instruct j : instructs) {
            try {
                j.generate(raw);
                int tt = j.size;
                listiner.append(String.format("%04X : %04X : %s ", j.offset, raw.get(i), j.opcode.toString()));
                if (j.arg1Token != null) {
                    listiner.append(j.arg1Token.toString());
                    if (j.arg2Token != null) {
                        listiner.append(", ");
                        listiner.append(j.arg2Token.toString());
                    }
                }
                listiner.append('\n');
                for (int n = 1; n < tt; n++) {
                    listiner.append(String.format("%04X : %04X\n", j.offset + n, raw.get(i + n)));
                }
                i += tt;
            } catch (ParserException ex) {
                switch (ex.type) {
                    case ERROR:
                        messager.error(j.line, 0, ex.getMessage());
                        break;
                    case WARNING:
                        messager.warning(j.line, 0, ex.getMessage());
                }
            }
        }
    }

    private static void calculateOffsets(Messager messager) {
        int t = 0;
        for (Instruct ins : instructs) {
            ins.offset = t;
            t += ins.size;
        }
        for (Argument.RelAddress link : unresolvedLinks) {
            int r = link.target.target.offset - link.holder.offset;
            if (r > 127 || r < -128) {
                messager.error(link.holder.line, 0, "Label is unreachable");
                r = 0;
            }
            link.offset = r;
        }
    }

    private void generate(List<Short> raw) throws ParserException {
        command.generate(raw, argument1, argument2);
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
                Argument.RelAddress link = new Argument.RelAddress(this, labels.get(s));
                unresolvedLinks.add(link);
                return link;
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

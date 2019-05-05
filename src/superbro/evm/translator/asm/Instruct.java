package superbro.evm.translator.asm;

import java.util.ArrayList;
import java.util.HashMap;

class Instruct {

    Command command;
    Argument argument1, argument2;

    private static HashMap<String, Command> commandMap;

    static {
        commandMap = new HashMap<>();
        commandMap.put("MOV", new Command.MOV());
        commandMap.put("MOVI", new Command.MOVI());
        commandMap.put("INT", new Command.INT());
        commandMap.put("ADD", new Command.ADD());
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

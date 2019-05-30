package superbro.evm.translator.asm;

public class ParserException extends Exception {

    Type type;

    public ParserException(String message) {
        super(message);
        type = Type.ERROR;
    }

    public ParserException(Type type, String message){
        super(message);
        this.type = type;
    }

    public enum Type {
        WARNING, ERROR
    }

    public static ParserException redundantArgument(){
        return  new ParserException(ParserException.Type.WARNING, "Redundant argument");
    }

    public static ParserException invalidArgumentType(){
        return new ParserException("Invalid argument type");
    }

    public static ParserException numberMustBeUnder8(){
        return new ParserException("Number must be <8");
    }

    public static ParserException numberMustBeUnder16(){
        return new ParserException("Number must be <16");
    }
}

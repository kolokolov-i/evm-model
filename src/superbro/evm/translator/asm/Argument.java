package superbro.evm.translator.asm;

public class Argument {
    public Type type;
    //public int value;

    Argument(Type type) {
        this.type = type;
        //value = v;
    }

    static Argument voidArg = new Argument(Type.NONE);

    static class Number extends Argument {
        int value;
        Number(int number) {
            super(Type.NUMBER);
            value = number;
        }
    }

    static class Reg8 extends Argument {
        int value;
        Reg8(int index){
            super(Type.REG8);
            value = index;
        }
    }

    static class Reg16 extends Argument {
        int value;
        Reg16(int index){
            super(Type.REG16);
            value = index;
        }
    }

    static class Port extends Argument {
        int value;
        Port(int index){
            super(Type.PORT);
            value = index;
        }
    }

    static class RelAddress extends Argument {
        int offset;
        RelAddress(byte ofst){
            super(Type.NUMBER);
            offset = ofst;
        }
    }

    static class Address extends Argument {
        int address;
        Address(int a){
            super(Type.NUMBER);
            address = a;
        }
    }

    static class Index extends Argument {
        Argument argument;
        Index(Argument a){
            super(Type.INDEX);
            argument = a;
        }
    }

    static class IndexPlus extends Argument {
        Argument arg1, arg2;
        IndexPlus(Argument a1, Argument a2){
            super(Type.INDEXPLUS);
            arg1 = a1;
            arg2 = a2;
        }
    }
}

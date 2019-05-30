package superbro.evm.translator.asm;

public class Argument {
    public Type type;
    //public int value;

    Argument(Type type) {
        this.type = type;
        //value = v;
    }

    public static Argument voidArg = new Argument(Type.NONE);

    public static class Number extends Argument {
        public int value;
        Number(int number) {
            super(Type.NUMBER);
            value = number;
        }
    }

    public static class Reg8 extends Argument {
        public int value;
        Reg8(int index){
            super(Type.REG8);
            value = index;
        }
    }

    public static class Reg16 extends Argument {
        public int value;
        Reg16(int index){
            super(Type.REG16);
            value = index;
        }
    }

    public static class Port extends Argument {
        public int value;
        Port(int index){
            super(Type.PORT);
            value = index;
        }
    }

    public static class RelAddress extends Argument {
        public int offset;
        RelAddress(byte ofst){
            super(Type.NUMBER);
            offset = ofst;
        }
    }

    public static class Address extends Argument {
        public int address;
        Address(int a){
            super(Type.NUMBER);
            address = a;
        }
    }

    public static class Index extends Argument {
        public Argument argument;
        Index(Argument a){
            super(Type.INDEX);
            argument = a;
        }
    }

    public static class IndexPlus extends Argument {
        public Argument arg1, arg2;
        IndexPlus(Argument a1, Argument a2){
            super(Type.INDEXPLUS);
            arg1 = a1;
            arg2 = a2;
        }
    }
}
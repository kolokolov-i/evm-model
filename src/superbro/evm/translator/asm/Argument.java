package superbro.evm.translator.asm;

class Argument {
    Type type;
    int value;

    Argument(Type type, int v) {
        this.type = type;
        value = v;
    }

    static Argument voidArg = new Argument(Type.NONE, 0);
}

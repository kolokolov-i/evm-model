package superbro.evm.translator.asm;

public class Argument {
    public Type type;
    public int value;

    Argument(Type type, int v) {
        this.type = type;
        value = v;
    }

    static Argument voidArg = new Argument(Type.NONE, 0);
}

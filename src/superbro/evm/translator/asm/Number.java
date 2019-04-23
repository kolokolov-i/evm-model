package superbro.evm.translator.asm;

public class Number extends Token {

    final int value;

    public Number(int v){
        super(Tag.NUMBER);
        value = v;
    }
}

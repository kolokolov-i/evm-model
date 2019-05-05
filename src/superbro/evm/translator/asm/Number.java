package superbro.evm.translator.asm;

class Number extends Token {

    final int value;

    Number(int v, int n){
        super(Tag.NUMBER, n);
        value = v;
    }
}

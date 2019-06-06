package superbro.evm.translator.asm;

class Number extends Token {

    final int value;

    Number(int v, int n, int c){
        super(Tag.NUMBER, n, c);
        value = v;
    }

    @Override
    public String toString() {
        return String.format("0x%X", value);
    }
}

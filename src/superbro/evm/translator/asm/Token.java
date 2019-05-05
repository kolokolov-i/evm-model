package superbro.evm.translator.asm;

class Token {

    Tag tag;
    int line;

    Token(Tag tag, int n) {
        this.tag = tag;
        line = n;
    }
}

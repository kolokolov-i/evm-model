package superbro.evm.translator.asm;

public class Token {

    Tag tag;
    int line, col;

    Token(Tag tag, int n, int c) {
        this.tag = tag;
        line = n;
        col = c;
    }
}

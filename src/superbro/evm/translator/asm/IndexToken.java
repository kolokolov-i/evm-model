package superbro.evm.translator.asm;

class IndexToken extends Token {

    Token master, slave;

    IndexToken(Token tok){
        super(Tag.INDEX, tok.line, tok.col);
        master = tok;
        slave = null;
    }

    void plus(Token tok) {
        slave = tok;
    }
}

package superbro.evm.translator.asm;

public class ComplexArg extends Token {

    Token master, slave;

    ComplexArg(Token tok){
        super(Tag.INDEX, tok.line, tok.col);
        master = tok;
        slave = null;
    }

    void plus(Token tok) {
        slave = tok;
    }
}

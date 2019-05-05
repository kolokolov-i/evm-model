package superbro.evm.translator.asm;

class Word extends Token {

    final String lexeme;

    Word(Tag tag, String s, int n) {
        super(tag, n);
        lexeme = s;
    }
}

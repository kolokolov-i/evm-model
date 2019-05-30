package superbro.evm.translator.asm;

class Word extends Token {

    final String lexeme;

    Word(Tag tag, String s, int n, int c) {
        super(tag, n, c);
        lexeme = s;
    }
}

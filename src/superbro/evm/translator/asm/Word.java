package superbro.evm.translator.asm;

public class Word extends Token {

    final String lexeme;

    public Word(Tag tag, String s) {
        super(tag);
        lexeme = s;
    }
}

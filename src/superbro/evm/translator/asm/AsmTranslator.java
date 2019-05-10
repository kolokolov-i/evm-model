package superbro.evm.translator.asm;

import superbro.evm.translator.MessageRecord;
import superbro.evm.translator.Translator;

import java.util.ArrayList;
import java.util.List;

public class AsmTranslator extends Translator {

    public AsmTranslator() {
        super();
    }

    public AsmTranslator(String srcCode, String srcData) {
        super(srcCode, srcData);
    }

    @Override
    public boolean translate() {
        success = true;
        translateCode();
        return success;
    }

    private boolean success;

    private void translateCode() {
        Lexer lexer = new Lexer(sourceCode, messager);
        success = lexer.scan();
        if(!success){
            return;
        }
        List<Token> tokens = lexer.getResult();
        Syntaxer syntaxer = new Syntaxer(tokens, messager);
        success = syntaxer.parse();
        if(!success){
            return;
        }
        List<Instruct> instructs = syntaxer.getResult();
        ArrayList<Short> r = new ArrayList<>(1000);
        StringBuilder listingBuilder = new StringBuilder();
        for (Instruct j : instructs) {
            try {
                j.generate(r);
            } catch (ParserException ex) {
                messager.error(0, 0, ex.getMessage());
            }
        }
        for(Short t : r){
            listingBuilder.append(String.format("%04x\n", t));
        }
        listingCode = listingBuilder.toString();
    }
}

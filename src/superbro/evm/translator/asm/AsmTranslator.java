package superbro.evm.translator.asm;

import superbro.evm.translator.ErrorRecord;
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
        init();
        translateCode();
        return success;
    }

    private boolean success;

    private void init() {
        messages.clear();
        success = true;
    }

    private void translateCode() {
        List<ErrorRecord> eList = new ArrayList<>();
        Lexer lexer = new Lexer(sourceCode, eList);
        success = lexer.scan();
        List<Token> tokens = lexer.getResult();
        Syntaxer syntaxer = new Syntaxer(tokens, eList);
        success = syntaxer.parse();
        List<Instruct> instructs = syntaxer.getResult();
        ArrayList<Short> r = new ArrayList<>(1000);
        StringBuilder listingBuilder = new StringBuilder();
        for (Instruct j : instructs) {
            try {
                j.generate(r);
            } catch (ParserException ex) {
                eList.add(new ErrorRecord(0, 0, ex.getMessage()));
            }
        }
        for(Short t : r){
            listingBuilder.append(String.format("%04x\n", t));
        }
        listingCode = listingBuilder.toString();
        for (ErrorRecord e : eList) {
            messages.add(e.toString());
        }
    }
}

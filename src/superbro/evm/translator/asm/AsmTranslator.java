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
        List<Token> lexems = lexer.getResult();

        for(ErrorRecord e : eList){
            messages.add(e.toString());
        }
    }


}

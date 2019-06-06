package superbro.evm.translator.asm;

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
        if (!success) {
            return;
        }
        List<Token> tokens = lexer.getResult();
        DataTranslator dTranslator = new DataTranslator(sourceData, messager);
        dTranslator.parse();
        Instruct.reset(dTranslator.vars);
        Syntaxer syntaxer = new Syntaxer(tokens, messager);
        success = syntaxer.parse();
        if (!success) {
            return;
        }
        List<Instruct> instructs = syntaxer.getResult();
        ArrayList<Short> raw = new ArrayList<>(1000);
        StringBuilder listiner = new StringBuilder();
        Instruct.generate(instructs, raw, listiner, messager);
//        for(Short t : raw){
//            listiner.append(String.format("%04x\n", t));
//        }
        rawCode = new short[raw.size()];
        int[] ints = raw.stream().mapToInt(t -> t).toArray();
        for(int i = 0; i<ints.length; i++){
            rawCode[i] = (short) ints[i];
        }
        listingCode = listiner.toString();
    }
}

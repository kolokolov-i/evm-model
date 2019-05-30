package superbro.evm.translator.asm;

import superbro.evm.translator.Messager;

import java.util.HashMap;
import java.util.Map;

class DataTranslator {

    private String source;
    private Messager messager;
    Map<String,Argument.Address> vars;

    DataTranslator(String src, Messager mes) {
        source = src;
        messager = mes;
    }

    void parse(){
        vars = new HashMap<>();
    }
}

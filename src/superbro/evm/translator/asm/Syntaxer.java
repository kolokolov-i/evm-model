package superbro.evm.translator.asm;

import superbro.evm.translator.Messager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

class Syntaxer {

    private List<Token> tokens;
    private Messager messager;
    private List<Instruct> result;

    Syntaxer(List<Token> tokens, Messager mes) {
        this.tokens = tokens;
        this.messager = mes;
        result = new ArrayList<>();
    }

    /*boolean parse(){
        boolean success = true;

        return success;
    }*/

    boolean parse() {
        boolean r = true;
        Iterator<Token> iter = tokens.iterator();
        State state = State.S0;
        String comName = null, arg1s = null, arg2s = null;
        int arg1n = 0, arg2n = 0;
        boolean arg1N = false, arg2N = false;
        while (iter.hasNext()) {
            Token token = iter.next();
            switch (state) {
                case S0:
                    arg1N = false;
                    arg2N = false;
                    comName = null;
                    arg1s = null;
                    arg2s = null;
                    arg1n = 0;
                    arg2n = 0;
                    switch (token.tag) {
                        case END:
                            break;
                        case ID:
                            comName = ((Word) token).lexeme;
                            state = State.S1;
                            break;
                        case NUMBER:
                            messager.error(token.line, 0, "Unexpected number");
                            r = false;
                            break;
                        case COMMA:
                            messager.error(token.line, 0, "Unexpected comma");
                            r = false;
                            break;
                    }
                    break;
                case S1:
                    switch (token.tag) {
                        case END:
                            try {
                                result.add(Instruct.create(comName));
                            } catch (ParserException ex) {
                                messager.error(token.line, 0, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                            arg1s = ((Word) token).lexeme;
                            arg1N = false;
                            state = State.S2;
                            break;
                        case NUMBER:
                            arg1s = null;
                            arg1n = ((Number) token).value;
                            arg1N = true;
                            state = State.S2;
                            break;
                        case COMMA:
                            messager.error(token.line, 0, "Unexpected comma");
                            r = false;
                            break;
                    }
                    break;
                case S2:
                    switch (token.tag) {
                        case END:
                            try {
                                result.add(arg1N ?
                                        Instruct.create(comName, arg1n) :
                                        Instruct.create(comName, arg1s));
                            } catch (ParserException ex) {
                                messager.error(token.line, 0, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                            messager.error(token.line, 0, "Comma is omitted");
                            r = false;
                            break;
                        case NUMBER:
                            messager.error(token.line, 0, "Unexpected number");
                            r = false;
                            break;
                        case COMMA:
                            state = State.S3;
                            break;
                    }
                    break;
                case S3:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, 0, "Unexpected end of instruction");
                            r = false;
                            break;
                        case ID:
                            arg2s = ((Word) token).lexeme;
                            arg2N = false;
                            state = State.S4;
                            break;
                        case NUMBER:
                            arg2s = null;
                            arg2n = ((Number) token).value;
                            arg2N = true;
                            state = State.S4;
                            break;
                        case COMMA:
                            messager.error(token.line, 0, "Unexpected comma");
                            r = false;
                            break;
                    }
                    break;
                case S4:
                    switch (token.tag) {
                        case END:
                            try {
                                if (arg1N) {
                                    result.add(arg2N ?
                                            Instruct.create(comName, arg1n, arg2n) :
                                            Instruct.create(comName, arg1n, arg2s));
                                } else {
                                    result.add(arg2N ?
                                            Instruct.create(comName, arg1s, arg2n) :
                                            Instruct.create(comName, arg1s, arg2s));
                                }
                            } catch (ParserException ex) {
                                messager.error(token.line, 0, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                            messager.error(token.line, 0, "Unexpected identifier");
                            r = false;
                            break;
                        case NUMBER:
                            messager.error(token.line, 0, "Unexpected number");
                            r = false;
                            break;
                        case COMMA:
                            messager.error(token.line, 0, "Unexpected comma");
                            r = false;
                            break;
                    }
                    break;
            }
        }
        return r;
    }

    List<Instruct> getResult() {
        return result;
    }

    private enum State {
        S0, S1, S2, S3, S4
    }
}

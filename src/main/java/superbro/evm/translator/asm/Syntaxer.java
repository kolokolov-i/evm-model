package superbro.evm.translator.asm;

import superbro.evm.translator.Messager;

import java.util.*;

class Syntaxer {

    private List<Token> tokens;
    private Messager messager;
    private List<Instruct> result;
    private Iterator<Token> toks;
    private Word tLabel, tCommand;
    private Token arg1, arg2;
    private IndexToken carg;
    private int curArg;

    Syntaxer(List<Token> tokens, Messager mes) {
        this.tokens = tokens;
        toks = tokens.iterator();
        this.messager = mes;
        result = new ArrayList<>();
    }

    boolean parse() {
        boolean r = true;
        State state = State.S0;
        while (toks.hasNext()) {
            Token token = toks.next();
            switch (state) {
                case S0:
                    tLabel = null;
                    tCommand = null;
                    arg1 = null;
                    arg2 = null;
                    carg = null;
                    curArg = 1;
                    switch (token.tag) {
                        case END:
                            break;
                        case ID:
                            tCommand = ((Word) token);
                            state = State.S1;
                            break;
                        case NUMBER:
                            messager.error(token.line, token.col, "Unexpected number");
                            r = false;
                            state = State.S0;
                            break;
                        case COMMA:
                        case COLON:
                        case BRAK_A:
                        case BRAK_B:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S1:
                    switch (token.tag) {
                        case END:
                            try {
                                createInstruction();
                            } catch (ParserException ex) {
                                messager.error(token.line, token.col, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case COLON:
                            tLabel = tCommand;
                            state = State.S2;
                            break;
                        case ID:
                        case NUMBER:
                            arg1 = token;
                            state = State.S3;
                            break;
                        case BRAK_A:
                            state = State.S4;
                            break;
                        case COMMA:
                        case BRAK_B:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S2:
                    switch (token.tag) {
                        case END:
                            state = State.S0;
                            break;
                        case ID:
                            tCommand = (Word) token;
                            state = State.S5;
                            break;
                        case NUMBER:
                            messager.error(token.line, token.col, "Unexpected number");
                            r = false;
                            state = State.S0;
                            break;
                        case COMMA:
                        case BRAK_A:
                        case BRAK_B:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S3:
                    switch (token.tag) {
                        case END:
                            try {
                                createInstruction();
                            } catch (ParserException ex) {
                                messager.error(token.line, token.col, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                            messager.error(token.line, token.col, "Missed comma");
                            r = false;
                            state = State.S0;
                            break;
                        case COMMA:
                            curArg++;
                            state = State.S6;
                            break;
                        case BRAK_A:
                        case BRAK_B:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S4:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, token.col, "Unexpected end of command");
                            r = false;
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                            carg = new IndexToken(token);
                            arg1 = carg;
                            state = State.S8;
                            break;
                        case BRAK_B:
                            messager.error(token.line, token.col, "Missed argument");
                            r = false;
                            state = State.S0;
                            break;
                        case BRAK_A:
                        case COMMA:
                        case COLON:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S5:
                    switch (token.tag) {
                        case END:
                            try {
                                createInstruction();
                            } catch (ParserException ex) {
                                messager.error(token.line, token.col, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                            arg1 = token;
                            state = State.S3;
                            break;
                        case BRAK_A:
                            state = State.S4;
                            break;
                        case BRAK_B:
                        case COMMA:
                        case COLON:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S6:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, token.col, "Unexpected end of command");
                            r = false;
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                            arg2 = token;
                            state = State.S7;
                            break;
                        case BRAK_A:
                            state = State.S4;
                            break;
                        case BRAK_B:
                        case COMMA:
                        case COLON:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S7:
                    switch (token.tag) {
                        case END:
                            try {
                                createInstruction();
                            } catch (ParserException ex) {
                                messager.error(token.line, token.col, ex.getMessage());
                            }
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                        case BRAK_A:
                        case BRAK_B:
                        case COMMA:
                        case COLON:
                        case PLUS:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S8:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, token.col, "Unexpected end of command");
                            r = false;
                            state = State.S0;
                            break;
                        case BRAK_B:
                            switch (curArg) {
                                case 1:
                                    arg1 = carg;
                                    state = State.S3;
                                    break;
                                case 2:
                                    arg2 = carg;
                                    state = State.S7;
                                    break;
                            }
                            break;
                        case PLUS:
                            state = State.S9;
                            break;
                        case ID:
                        case NUMBER:
                            messager.error(token.line, token.col, "Missed plus sign");
                            r = false;
                            state = State.S0;
                            break;
                        case BRAK_A:
                        case COMMA:
                        case COLON:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S9:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, token.col, "Unexpected end of command");
                            r = false;
                            state = State.S0;
                            break;
                        case PLUS:
                            state = State.S9;
                            break;
                        case ID:
                        case NUMBER:
                            carg.plus(token);
                            state = State.S10;
                            break;
                        case BRAK_A:
                        case BRAK_B:
                        case COMMA:
                        case COLON:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
                case S10:
                    switch (token.tag) {
                        case END:
                            messager.error(token.line, token.col, "Unexpected end of command");
                            r = false;
                            state = State.S0;
                            break;
                        case ID:
                        case NUMBER:
                            messager.error(token.line, token.col, "Unexpected argument");
                            r = false;
                            state = State.S0;
                            break;
                        case BRAK_B:
                            switch (curArg) {
                                case 1:
                                    arg1 = carg;
                                    state = State.S3;
                                    break;
                                case 2:
                                    arg2 = carg;
                                    state = State.S7;
                                    break;
                            }
                            break;
                        case BRAK_A:
                        case PLUS:
                        case COMMA:
                        case COLON:
                            messager.error(token.line, token.col, "Unexpected symbol");
                            r = false;
                            state = State.S0;
                            break;
                    }
                    break;
            }
        }
        return r;
    }

    private void createInstruction() throws ParserException {
        Instruct instruction = new Instruct(tLabel, tCommand, arg1, arg2);
        result.add(instruction);
    }

    List<Instruct> getResult() {
        return result;
    }

//    public Map<String, Label> getLabels() {
//        return labels;
//    }

    private enum State {
        S0, S1, S2, S3, S4, S5,
        S6, S7, S8, S9, S10
    }
}

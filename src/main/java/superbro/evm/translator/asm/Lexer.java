package superbro.evm.translator.asm;

import superbro.evm.translator.Messager;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private List<Token> result;
    private Messager messager;
    private String[] lines;

    public Lexer() {
    }

    Lexer(String src, Messager mes) {
        lines = src.split("\n");
        result = new ArrayList<>(100);
        messager = mes;
    }

    boolean scan() {
        boolean r = true;
        for (int j = 0; j < lines.length; j++) {
            String line = lines[j];
            boolean tr = scan(line, j, result, messager);
            r &= tr;
        }
        return r;
    }

    public boolean scan(String line, int j, List<Token> result, Messager messager){
        StringBuilder sbuf = new StringBuilder();
        boolean r = true;
        char c;
        State state = State.S0;
        int i = 0;
        boolean lineLoopFlag = true;
        while (lineLoopFlag) {
            if (i >= line.length()) {
                c = '\n';
                lineLoopFlag = false;
            } else {
                c = line.charAt(i);
            }
            CharCategory charCat = CharCategory.get(c);
            switch (state) {
                case S0:
                    switch (charCat) {
                        case p:
                            break;
                        case n:
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case a:
                            state = State.S1;
                            sbuf.append(c);
                            break;
                        case d:
                            if (c == '0') {
                                state = State.S2;
                            } else {
                                sbuf.append(c);
                                state = State.S3;
                            }
                            break;
                        case m:
                            result.add(new Token(Tag.COMMA, j, i));
                            break;
                        case s:
                            result.add(new Token(Tag.COLON, j, i));
                            break;
                        case u:
                            result.add(new Token(Tag.PLUS, j, i));
                            break;
                        case ba:
                            result.add(new Token(Tag.BRAK_A, j, i));
                            break;
                        case bb:
                            result.add(new Token(Tag.BRAK_B, j, i));
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
                case S1:
                    switch (charCat) {
                        case p:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            state = State.S0;
                            break;
                        case n:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case a:
                        case d:
                            sbuf.append(c);
                            break;
                        case m:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COMMA, j, i));
                            state = State.S0;
                            break;
                        case s:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COLON, j, i));
                            state = State.S0;
                            break;
                        case ba:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_A, j, i));
                            state = State.S0;
                            break;
                        case bb:
                            result.add(new Word(Tag.ID, sbuf.toString(), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_B, j, i));
                            state = State.S0;
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
                case S2:
                    switch (charCat) {
                        case p:
                            result.add(new Number(0, j, i));
                            state = State.S0;
                            break;
                        case n:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.END, j, i));
                            state = State.S0;
                            lineLoopFlag = false;
                            break;
                        case a:
                            if (c == 'b') {
                                state = State.S4;
                            } else if (c == 'x') {
                                state = State.S5;
                            } else {
                                messager.error(j, i, "Invalid identifier");
                                r = false;
                                result.add(new Token(Tag.END, j, i));
                                state = State.S0;
                                lineLoopFlag = false;
                                break;
                            }
                            break;
                        case d:
                            sbuf.append(c);
                            state = State.S3;
                            break;
                        case m:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.COMMA, j, i));
                            state = State.S0;
                            break;
                        case s:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.COLON, j, i));
                            state = State.S0;
                            break;
                        case u:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.PLUS, j, i));
                            state = State.S0;
                            break;
                        case ba:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.BRAK_A, j, i));
                            state = State.S0;
                            break;
                        case bb:
                            result.add(new Number(0, j, i));
                            result.add(new Token(Tag.BRAK_B, j, i));
                            state = State.S0;
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
                case S3:
                    switch (charCat) {
                        case p:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            state = State.S0;
                            break;
                        case n:
                            try{
                                result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            }
                            catch(NumberFormatException ex){
                                messager.error(j, i, "Invalid number " + sbuf.toString());
                            }
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case a:
                            sbuf.append(c);
                            messager.error(j, i, "Invalid number " + sbuf.toString());
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case d:
                            sbuf.append(c);
                            break;
                        case m:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COMMA, j, i));
                            state = State.S0;
                            break;
                        case s:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COLON, j, i));
                            state = State.S0;
                            break;
                        case u:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.PLUS, j, i));
                            state = State.S0;
                            break;
                        case ba:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_A, j, i));
                            state = State.S0;
                            break;
                        case bb:
                            result.add(new Number(Integer.parseInt(sbuf.toString()), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_B, j, i));
                            state = State.S0;
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
                case S4:
                    switch (charCat) {
                        case p:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            state = State.S0;
                            break;
                        case n:
                            try{
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            }
                            catch(NumberFormatException ex){
                                messager.error(j, i, "Invalid number " + sbuf.toString());
                            }
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case a:
                            sbuf.append(c);
                            messager.error(j, i, "Invalid number " + sbuf.toString());
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case d:
                            sbuf.append(c);
                            if (c != '0' && c != '1') {
                                messager.error(j, i, "Invalid number " + sbuf.toString());
                                r = false;
                                result.add(new Token(Tag.END, j, i));
                                lineLoopFlag = false;
                            }
                            break;
                        case m:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COMMA, j, i));
                            state = State.S0;
                            break;
                        case s:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COLON, j, i));
                            state = State.S0;
                            break;
                        case u:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.PLUS, j, i));
                            state = State.S0;
                            break;
                        case ba:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_A, j, i));
                            state = State.S0;
                            break;
                        case bb:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_B, j, i));
                            state = State.S0;
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
                case S5:
                    switch (charCat) {
                        case p:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            state = State.S0;
                            break;
                        case n:
                            try{
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            }
                            catch(NumberFormatException ex){
                                messager.error(j, i, "Invalid number " + sbuf.toString());
                            }
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                        case a:
                            switch (c) {
                                case 'a':
                                case 'A':
                                case 'b':
                                case 'B':
                                case 'c':
                                case 'C':
                                case 'd':
                                case 'D':
                                case 'e':
                                case 'E':
                                case 'f':
                                case 'F':
                                    sbuf.append(c);
                                    break;
                                default:
                                    sbuf.append(c);
                                    messager.error(j, i, "Invalid number " + sbuf.toString());
                                    r = false;
                                    result.add(new Token(Tag.END, j, i));
                                    lineLoopFlag = false;
                            }
                            break;
                        case d:
                            sbuf.append(c);
                            break;
                        case m:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COMMA, j, i));
                            state = State.S0;
                            break;
                        case s:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.COLON, j, i));
                            state = State.S0;
                            break;
                        case u:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.PLUS, j, i));
                            state = State.S0;
                            break;
                        case ba:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_A, j, i));
                            state = State.S0;
                            break;
                        case bb:
                            result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j, i));
                            sbuf.delete(0, sbuf.length());
                            result.add(new Token(Tag.BRAK_B, j, i));
                            state = State.S0;
                            break;
                        case other:
                            messager.error(j, i, "Unknown symbol");
                            r = false;
                            result.add(new Token(Tag.END, j, i));
                            lineLoopFlag = false;
                            break;
                    }
                    break;
            }
            i++;
        }
        return r;
    }

    List<Token> getResult() {
        return result;
    }

    private enum State {
        S0, S1, S2, S3, S4, S5
    }
}

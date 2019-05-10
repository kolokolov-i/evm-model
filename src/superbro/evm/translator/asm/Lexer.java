package superbro.evm.translator.asm;

import superbro.evm.translator.ErrorRecord;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

class Lexer {

    private StringReader reader;
    private List<Token> result;
    private List<ErrorRecord> eList;
    private String[] lines;

    Lexer(String src, List<ErrorRecord> errList) {
        reader = new StringReader(src);
        lines = src.split("\n");
        result = new ArrayList<>(100);
        eList = errList;
    }

    boolean scan() {
        boolean r = true;
        char c;
        StringBuffer sbuf = new StringBuffer();
        for (int j = 0; j < lines.length; j++) {
            String line = lines[j];
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
                                result.add(new Token(Tag.END, j));
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
                                result.add(new Token(Tag.COMMA, j));
                                break;
                            case s:
                                result.add(new Token(Tag.SEMICOLON, j));
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknow symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S1:
                        switch (charCat) {
                            case p:
                                result.add(new Word(Tag.ID, sbuf.toString(), j));
                                sbuf.delete(0, sbuf.length());
                                state = State.S0;
                                break;
                            case n:
                                result.add(new Word(Tag.ID, sbuf.toString(), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                            case a:
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Word(Tag.ID, sbuf.toString(), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA, j));
                                state = State.S0;
                                break;
                            case s:
                                result.add(new Word(Tag.ID, sbuf.toString(), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.SEMICOLON, j));
                                state = State.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S2:
                        switch (charCat) {
                            case p:
                                result.add(new Number(0, j));
                                state = State.S0;
                                break;
                            case n:
                                result.add(new Number(0, j));
                                result.add(new Token(Tag.END, j));
                                state = State.S0;
                                lineLoopFlag = false;
                                break;
                            case a:
                                if (c == 'b') {
                                    state = State.S4;
                                } else if (c == 'x') {
                                    state = State.S5;
                                } else {
                                    eList.add(new ErrorRecord(j, i, "Invalid identifier"));
                                    r = false;
                                    result.add(new Token(Tag.END, j));
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
                                result.add(new Number(0, j));
                                result.add(new Token(Tag.COMMA, j));
                                state = State.S0;
                                break;
                            case s:
                                result.add(new Number(0, j));
                                result.add(new Token(Tag.SEMICOLON, j));
                                state = State.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S3:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString()), j));
                                sbuf.delete(0, sbuf.length());
                                state = State.S0;
                                break;
                            case n:
                                try{
                                    result.add(new Number(Integer.parseInt(sbuf.toString()), j));
                                }
                                catch(NumberFormatException ex){
                                    eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                }
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                            case a:
                                sbuf.append(c);
                                eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString()), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA, j));
                                state = State.S0;
                                break;
                            case s:
                                result.add(new Number(Integer.parseInt(sbuf.toString()), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.SEMICOLON, j));
                                state = State.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S4:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j));
                                sbuf.delete(0, sbuf.length());
                                state = State.S0;
                                break;
                            case n:
                                try{
                                    result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j));
                                }
                                catch(NumberFormatException ex){
                                    eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                }
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                            case a:
                                sbuf.append(c);
                                eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                            case d:
                                sbuf.append(c);
                                if (c != '0' && c != '1') {
                                    eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                    r = false;
                                    result.add(new Token(Tag.END, j));
                                    lineLoopFlag = false;
                                }
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA, j));
                                state = State.S0;
                                break;
                            case s:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.SEMICOLON, j));
                                state = State.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S5:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j));
                                sbuf.delete(0, sbuf.length());
                                state = State.S0;
                                break;
                            case n:
                                try{
                                    result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j));
                                }
                                catch(NumberFormatException ex){
                                    eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                }
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END, j));
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
                                        eList.add(new ErrorRecord(j, i, "Invalid number " + sbuf.toString()));
                                        r = false;
                                        result.add(new Token(Tag.END, j));
                                        lineLoopFlag = false;
                                }
                                break;
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA, j));
                                state = State.S0;
                                break;
                            case s:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16), j));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.SEMICOLON, j));
                                state = State.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(j, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END, j));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                }
                i++;
            }
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

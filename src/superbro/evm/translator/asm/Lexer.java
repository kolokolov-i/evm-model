package superbro.evm.translator.asm;

import superbro.evm.translator.ErrorRecord;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Lexer {

    StringReader reader;
    List<Token> result;
    List<ErrorRecord> eList;
    String[] lines;

    public Lexer(String src, List<ErrorRecord> errList) {
        reader = new StringReader(src);
        lines = src.split("\n");
        result = new ArrayList<>(100);
        eList = errList;
    }

    public boolean scan() {
        boolean r = true;
        int curLineNumber = 0;
        char c;
        StringBuffer sbuf = new StringBuffer();
        for (int j = 0; j<lines.length; j++) {
            String line = lines[j];
            ParserState state = ParserState.S0;
            int i = 0;
            curLineNumber++;
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
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case a:
                                state = ParserState.S1;
                                sbuf.append(c);
                                break;
                            case d:
                                if (c == '0') {
                                    state = ParserState.S2;
                                } else {
                                    sbuf.append(c);
                                    state = ParserState.S3;
                                }
                                break;
                            case m:
                                result.add(new Token(Tag.COMMA));
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknow symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S1:
                        switch (charCat) {
                            case p:
                                result.add(new Word(Tag.ID, sbuf.toString()));
                                sbuf.delete(0, sbuf.length());
                                state = ParserState.S0;
                                break;
                            case n:
                                result.add(new Word(Tag.ID, sbuf.toString()));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case a:
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                sbuf.append(c);
                                result.add(new Word(Tag.ID, sbuf.toString()));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA));
                                state = ParserState.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S2:
                        switch (charCat) {
                            case p:
                                result.add(new Number(0));
                                state = ParserState.S0;
                                break;
                            case n:
                                result.add(new Number(0));
                                result.add(new Token(Tag.END));
                                state = ParserState.S0;
                                lineLoopFlag = false;
                                break;
                            case a:
                                if (c == 'b') {
                                    state = ParserState.S4;
                                } else if (c == 'x') {
                                    state = ParserState.S5;
                                } else {
                                    eList.add(new ErrorRecord(curLineNumber, i, "Invalid identificator"));
                                    r = false;
                                    result.add(new Token(Tag.END));
                                    state = ParserState.S0;
                                    lineLoopFlag = false;
                                    break;
                                }
                                break;
                            case d:
                                sbuf.append(c);
                                state = ParserState.S3;
                                break;
                            case m:
                                result.add(new Number(0));
                                result.add(new Token(Tag.COMMA));
                                state = ParserState.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S3:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString())));
                                sbuf.delete(0, sbuf.length());
                                state = ParserState.S0;
                                break;
                            case n:
                                result.add(new Number(Integer.parseInt(sbuf.toString())));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case a:
                                eList.add(new ErrorRecord(curLineNumber, i, "Invalid number"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString())));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA));
                                state = ParserState.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S4:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2)));
                                sbuf.delete(0, sbuf.length());
                                state = ParserState.S0;
                                break;
                            case n:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2)));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case a:
                                eList.add(new ErrorRecord(curLineNumber, i, "Invalid number"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 2)));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA));
                                state = ParserState.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case S5:
                        switch (charCat) {
                            case p:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16)));
                                sbuf.delete(0, sbuf.length());
                                state = ParserState.S0;
                                break;
                            case n:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16)));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                            case a:
                                switch(c){
                                    case 'a':    case 'A':
                                    case 'b':    case 'B':
                                    case 'c':    case 'C':
                                    case 'd':    case 'D':
                                    case 'e':    case 'E':
                                    case 'f':    case 'F':
                                        sbuf.append(c);
                                        break;
                                    default:
                                        eList.add(new ErrorRecord(curLineNumber, i, "Invalid number"));
                                        r = false;
                                        result.add(new Token(Tag.END));
                                        lineLoopFlag = false;
                                }
                                break;
                            case d:
                                sbuf.append(c);
                                break;
                            case m:
                                result.add(new Number(Integer.parseInt(sbuf.toString(), 16)));
                                sbuf.delete(0, sbuf.length());
                                result.add(new Token(Tag.COMMA));
                                state = ParserState.S0;
                                break;
                            case other:
                                eList.add(new ErrorRecord(curLineNumber, i, "Unknown symbol"));
                                r = false;
                                result.add(new Token(Tag.END));
                                lineLoopFlag = false;
                                break;
                        }
                        break;
                    case F1:
                        break;
                }
                i++;
            }
        }
        return r;
    }

    public List<Token> getResult() {
        return result;
    }

    private enum ParserState {
        S0, S1, S2, S3, S4, S5, F1;
    }
}

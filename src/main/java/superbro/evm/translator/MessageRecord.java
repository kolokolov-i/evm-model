package superbro.evm.translator;

public class MessageRecord {
    public int nLine, column;
    public String message;

    public MessageRecord(int nLine, int column, String message) {
        this.nLine = nLine;
        this.column = column;
        this.message = message;
    }

    public String toString(String title) {
        return String.format("%s: (%d:%d) %s",title, nLine, column, message);
    }
}

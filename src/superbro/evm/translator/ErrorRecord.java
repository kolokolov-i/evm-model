package superbro.evm.translator;

public class ErrorRecord {
    public int nLine, column;
    public String message;

    public ErrorRecord(int nLine, int column, String message) {
        this.nLine = nLine;
        this.column = column;
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error at " + nLine + ":" + column +
                ", " + message;
    }
}

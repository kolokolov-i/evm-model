package superbro.evm.translator;

import java.util.ArrayList;
import java.util.List;

public class Messager {

    private List<MessageRecord> eList, wList;

    public Messager() {
        eList = new ArrayList<>();
        wList = new ArrayList<>();
    }

    public void warning(int line, int column, String message){
        wList.add(new MessageRecord(line, column, message));
    }

    public void error(int line, int column, String message){
        eList.add(new MessageRecord(line, column, message));
    }

    public List<MessageRecord> getErrors() {
        return eList;
    }

    public List<MessageRecord> getWarnings() {
        return wList;
    }

    public void reset(){
        eList.clear();
        wList.clear();
    }
}

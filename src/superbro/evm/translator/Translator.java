package superbro.evm.translator;

import java.util.ArrayList;
import java.util.List;

public abstract class Translator {

    protected String sourceCode, sourceData;
    protected List<String> messages;
    protected String listingCode, listingData;
    protected byte[] rawCode, rawData;

    public Translator() {
    }

    public Translator(String srcCode, String srcData) {
        sourceCode = srcCode;
        sourceData = srcData;
        messages = new ArrayList<>();
    }

    public abstract boolean translate();

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public List<String> getMessages(){
        return messages;
    }

    public String getListingCode() {
        return listingCode;
    }

    public String getListingData() {
        return listingData;
    }

    public byte[] getRawCode() {
        return rawCode;
    }

    public byte[] getRawData() {
        return rawData;
    }
}

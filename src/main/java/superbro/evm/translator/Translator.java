package superbro.evm.translator;

public abstract class Translator {

    protected String sourceCode, sourceData;
    protected Messager messager;
    protected String listingCode, listingData;
    protected byte[] rawCode, rawData;

    public Translator() {
    }

    public Translator(String srcCode, String srcData) {
        sourceCode = srcCode;
        sourceData = srcData;
        messager = new Messager();
    }

    public abstract boolean translate();

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }

    public void setSourceData(String sourceData) {
        this.sourceData = sourceData;
    }

    public Messager getMessager(){
        return messager;
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

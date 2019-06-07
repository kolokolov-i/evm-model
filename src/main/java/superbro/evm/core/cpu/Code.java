package superbro.evm.core.cpu;

public class Code {

    public static byte extN8(short com){
        return (byte) (com & 0xff);
    }

    public static int extN4(short com, int s){
        return (com >> s) & 0x0f;
    }

    public static int extN5(short com, int s){
        return (com >> s) & 0x1f;
    }

    public static int extRM(short com, int s){
        return (com >> s) & 0x03;
    }

    public static int extR8(short com, int s){
        return (com >> s) & 0x07;
    }
}

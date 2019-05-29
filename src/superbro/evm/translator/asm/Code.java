package superbro.evm.translator.asm;

public class Code {

    public static short gen_R8_N(int base, Argument a1, Argument a2) {
        short r = (short) base;
        r |= (((Argument.Reg8) a1).value & 0b00000111) << 8;
        r |= ((Argument.Number) a2).value & 0xff;
        return r;
    }

    public static short gen_R8_R8(int base, Argument a1, Argument a2) {
        short r = (short) base;
        r |= (((Argument.Reg8) a1).value & 0b00000111) << 4;
        r |= ((Argument.Reg8) a2).value & 0b00000111;
        return r;
    }

    public static short gen_R16_R16(int base, Argument a1, Argument a2) {
        short r = (short) base;
        r |= (((Argument.Reg16) a1).value & 0b00000011) << 4;
        r |= ((Argument.Reg16) a2).value & 0b00000011;
        return r;
    }

    public static short gen_R8(int base, Argument a1) {
        short r = (short) base;
        r |= ((Argument.Reg8) a1).value & 0b00000111;
        return r;
    }

    public static short gen_R16(int base, Argument a1) {
        short r = (short) base;
        r |= ((Argument.Reg16) a1).value & 0b00000011;
        return r;
    }

    public static short gen_R8_N3(int base, Argument a1, Argument a2){
        short r = (short) base;
        r |= (((Argument.Reg8) a1).value & 0b00000111) << 4;
        r |= ((Argument.Number) a2).value & 0b00000111;
        return r;
    }

    public static short gen_R16_N4(int base, Argument a1, Argument a2){
        short r = (short) base;
        r |= (((Argument.Reg16) a1).value & 0b00000011) << 4;
        r |= ((Argument.Number) a2).value & 0b00001111;
        return r;
    }

    public static short gen_N8(int base, Argument a1){
        short r = (short) base;
        r |= ((Argument.Number) a1).value & 0xff;
        return r;
    }

    public static short form_R8(int base, Argument a, int shifting){
        return (short)(base | (((Argument.Reg8)a).value << shifting));
    }

    public static short form_R16(int base, Argument a, int shifting){
        return (short)(base | (((Argument.Reg16)a).value << shifting));
    }

    public static short form_N8(int base, Argument a, int shifting){
        return (short)(base | (((Argument.Number)a).value << shifting));
    }

    public static short form_P5(int base, Argument a, int shifting){
        return (short)(base | ((((Argument.Port)a).value & 0x1f)<< shifting));
    }
}

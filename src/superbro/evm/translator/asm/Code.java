package superbro.evm.translator.asm;

public class Code {

    public static short gen_R8_N(int base, Argument a1, Argument a2){
        short r = (short) base;
        r |= (((Argument.Reg8)a1).value & 0b00000111) << 8;
        r |= ((Argument.Number)a2).value & 0xff;
        return r;
    }

    public static short gen_R8_R8(int base, Argument a1, Argument a2){
        short r = (short) base;
        r |= (((Argument.Reg8)a1).value & 0b00000111) << 4;
        r |= ((Argument.Reg8)a2).value & 0b00000111;
        return r;
    }

    public static short gen_R16_R16(int base, Argument a1, Argument a2){
        short r = (short) base;
        r |= (((Argument.Reg16)a1).value & 0b00000011) << 4;
        r |= ((Argument.Reg16)a2).value & 0b00000011;
        return r;
    }
}

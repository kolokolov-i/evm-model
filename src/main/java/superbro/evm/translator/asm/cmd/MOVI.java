package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class MOVI extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.NUMBER) {
                rr.add(Code.gen_R8_N(0x8800, arg1, arg2));
                return;
            }
        }
        else if(arg1.type == Type.REG16){
            if (arg2.type == Type.NUMBER) {
                Argument.Reg16 a1 = (Argument.Reg16)arg1;
                Argument.Number a2 = (Argument.Number) arg2;
                rr.add(Code.gen_R8_N(0x8800, a1.rL, a2.value>>8));
                rr.add(Code.gen_R8_N(0x8800, a1.rH, a2.value));
                return;
            }
        }
        throw ParserException.invalidArgumentType();
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.NUMBER) {
                return 1;
            }
        }
        else if(arg1.type == Type.REG16){
            if (arg2.type == Type.NUMBER) {
                return 2;
            }
        }
        return 0;
    }
}

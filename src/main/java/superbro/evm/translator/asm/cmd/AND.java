package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class AND extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.REG8) {
                rr.add(Code.gen_R8_R8(0x1000, arg1, arg2));
                return;
            } else if (arg2.type == Type.NUMBER) {
                rr.add(Code.gen_R8_N(0x1800, arg1, arg2));
                return;
            } else {
                throw ParserException.invalidArgumentType();
            }
        }
        if (arg1.type == Type.REG16) {
            if (arg2.type == Type.REG16) {
                rr.add(Code.gen_R16_R16(0x1100, arg1, arg2));
                return;
            }
        } else {
            throw ParserException.invalidArgumentType();
        }
        throw ParserException.invalidArgumentType();
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

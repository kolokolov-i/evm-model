package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class ADD extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.REG8) {
                short r = (short) 0x5000;
                rr.add(Code.gen_R8_R8(0x5000, arg1, arg2));
                return;
            } else if (arg2.type == Type.NUMBER) {
                rr.add(Code.gen_R8_N(0x5800, arg1, arg2));
                return;
            } else {
                throw new ParserException("Invalid argument type");
            }
        }
        if (arg1.type == Type.REG16) {
            if (arg2.type == Type.REG16) {
                rr.add(Code.gen_R16_R16(0x5100, arg1, arg2));
                return;
            }
        } else {
            throw new ParserException("Invalid argument type");
        }
        throw new ParserException("Invalid argument type");
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

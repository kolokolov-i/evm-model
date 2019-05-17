package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Token;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class ANDI extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
//        if (arg1.type == Type.REG8) {
//            if (arg2.type == Type.NUMBER) {
//                short r = (short) 0x1800;
//                r |= (arg1.value & 0b00000111) << 8;
//                r |= arg2.value & 0xff;
//                rr.add(r);
//                return;
//            }
//        }
//        throw new ParserException("Invalid argument type");
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

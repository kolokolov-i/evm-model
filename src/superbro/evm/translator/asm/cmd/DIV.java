package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class DIV extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.REG8) {
                short r = (short) 0x7400;
                r |= (arg1.value & 0b00000111) << 4;
                r |= arg2.value & 0b00000111;
                rr.add(r);
                return;
            } else {
                throw new ParserException("Invalid argument type");
            }
        }
        if (arg1.type == Type.REG16) {
            if (arg2.type == Type.REG16) {
                short r = (short) 0x7500;
                r |= (arg1.value & 0b00000011) << 4;
                r |= arg2.value & 0b00000011;
                rr.add(r);
                return;
            }
        } else {
            throw new ParserException("Invalid argument type");
        }
        throw new ParserException("Invalid argument type");
    }
}

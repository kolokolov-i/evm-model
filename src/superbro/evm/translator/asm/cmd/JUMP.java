package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class JUMP extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type != Type.REG16) {
            throw new ParserException("Invalid argument type");
        }
        short r = (short) 0x0180;
        r |= arg1.value & 0b00000011;
        rr.add(r);
        if (arg2.type != Type.NONE) {
            throw new ParserException("Redundant argument");
        }
    }
}
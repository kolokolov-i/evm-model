package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class FSZ extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        rr.add((short) 0x0701);
        if (arg1.type != Type.NONE) {
            throw new ParserException("Redundant argument");
        }
    }
}
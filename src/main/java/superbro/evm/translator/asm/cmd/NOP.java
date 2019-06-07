package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Token;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class NOP extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        rr.add((short) 0);
        if (arg1.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }

}

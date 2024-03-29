package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class STORF extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type != Type.REG8) {
            throw ParserException.invalidArgumentType();
        }
        short t;
        t = Code.gen_R8(0xA110, arg1);
        rr.add(t);
        if (arg2.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class JUMP extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type != Type.REG16) {
            throw ParserException.invalidArgumentType();
        }
        rr.add(Code.gen_R16(0x0180, arg1));
        if (arg2.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

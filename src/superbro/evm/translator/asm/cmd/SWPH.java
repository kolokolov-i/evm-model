package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class SWPH extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            rr.add(Code.gen_R8(0x40C0, arg1));
            if (arg2.type != Type.NONE) {
                throw new ParserException(ParserException.Type.WARNING,"Redundant argument");
            }
            return;
        }
        throw ParserException.invalidArgumentType();
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

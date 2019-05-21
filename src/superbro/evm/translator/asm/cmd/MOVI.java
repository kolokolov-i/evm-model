package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class MOVI extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if (arg2.type == Type.NUMBER) {
                rr.add(Code.gen_R8_N(0x8800, arg1, arg2));
                return;
            }
        }
        throw new ParserException("Invalid argument type");
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

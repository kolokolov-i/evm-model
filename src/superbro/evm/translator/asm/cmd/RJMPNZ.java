package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class RJMPNZ extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type != Type.NUMBER) {
            throw ParserException.invalidArgumentType();
        }
        rr.add(Code.gen_N8(0x0C00, arg1));
        if (arg2.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

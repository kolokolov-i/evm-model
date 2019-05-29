package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class PUSH extends Command {

    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            rr.add(Code.gen_R8(0xA800, arg1));
        } else if (arg1.type == Type.REG16) {
            rr.add(Code.gen_R16(0xA810, arg1));
        } else if (arg1.type == Type.NUMBER) {
            rr.add(Code.gen_N8(0xA900, arg1));
        } else {
            throw ParserException.invalidArgumentType();
        }
        if (arg2.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

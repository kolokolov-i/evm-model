package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Token;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class RJMPNS extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
//        if (arg1.type != Type.NUMBER) {
//            throw ParserException.invalidArgumentType();
//        }
//        short r = (short) 0x0D00;
//        r |= arg1.value & 0xff;
//        rr.add(r);
//        if (arg2.type != Type.NONE) {
//            throw ParserException.redundantArgument();
//        }
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

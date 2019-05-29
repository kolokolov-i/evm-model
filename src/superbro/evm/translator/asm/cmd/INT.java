package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Token;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class INT extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.NUMBER) {
            short r = (short) 0x0160;
            int t = ((Argument.Number)arg1).value;
            if (t >= 16) {
                throw ParserException.numberMustBeUnder16();
            }
            r |= (t & 0x0f);
            if (arg2.type != Type.NONE) {
                throw ParserException.redundantArgument();
            }
            rr.add(r);
            return;
        }
        throw ParserException.invalidArgumentType();
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

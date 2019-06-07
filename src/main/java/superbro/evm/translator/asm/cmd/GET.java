package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class GET extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg2.type != Type.PORT) {
            throw ParserException.invalidArgumentType();
        }
        if (arg1.type != Type.REG8) {
            throw ParserException.invalidArgumentType();
        }
        short t;
        t = Code.form_R8(0xA600, arg1, 0);
        t = Code.form_P5(t, arg2, 3);
        rr.add(t);
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

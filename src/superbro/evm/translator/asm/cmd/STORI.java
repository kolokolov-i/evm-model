package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class STORI extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg2.type != Type.NUMBER) {
            throw ParserException.invalidArgumentType();
        }
        if (arg1.type != Type.INDEX) {
            throw ParserException.invalidArgumentType();
        }
        Argument.Index arg = (Argument.Index) arg1;
        if(arg.argument.type != Type.REG16){
            throw ParserException.invalidArgumentType();
        }
        short t;
        t = Code.form_N8(0xE000, arg2, 0);
        t = Code.form_R16(t, arg.argument, 8);
        rr.add(t);
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

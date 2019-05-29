package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class STOR extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg2.type != Type.REG8) {
            throw ParserException.invalidArgumentType();
        }
        if (arg1.type == Type.INDEX) {
            Argument.Index arg = (Argument.Index) arg1;
            if(arg.argument.type != Type.REG16){
                throw ParserException.invalidArgumentType();
            }
            short t;
            t = Code.form_R8(0x9800, arg2, 0);
            t = Code.form_R16(t, arg.argument, 4);
            rr.add(t);
        }
        if (arg1.type == Type.INDEXPLUS){
            Argument.IndexPlus arg = (Argument.IndexPlus) arg1;
            if(arg.arg1.type != Type.REG16){
                throw ParserException.invalidArgumentType();
            }
            if(arg.arg2.type!=Type.REG8){
                throw ParserException.invalidArgumentType();
            }
            short t;
            t = Code.form_R8(0x9C00, arg2, 0);
            t = Code.form_R16(t, (arg.arg1), 8);
            t = Code.form_R8(t, (arg.arg2), 4);
            rr.add(t);
        }
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

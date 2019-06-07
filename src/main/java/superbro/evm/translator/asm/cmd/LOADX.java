package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class LOADX extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type != Type.REG16) {
            throw ParserException.invalidArgumentType();
        }
        if (arg2.type == Type.INDEX) {
            Argument.Index arg = (Argument.Index) arg2;
            if (arg.argument.type != Type.REG16) {
                throw ParserException.invalidArgumentType();
            }
            short t;
            t = Code.form_R16(0x9200, arg1, 4);
            t = Code.form_R16(t, arg.argument, 0);
            rr.add(t);
            return;
        }
        if (arg2.type == Type.INDEXPLUS) {
            Argument.IndexPlus arg = (Argument.IndexPlus) arg2;
            if (arg.arg1.type != Type.REG16) {
                throw ParserException.invalidArgumentType();
            }
            if (arg.arg2.type == Type.REG8) {
                short t;
                t = Code.form_R8(0x9300, arg.arg2, 4);
                t = Code.form_R16(t, arg1, 2);
                t = Code.form_R16(t, arg.arg1, 0);
                rr.add(t);
                return;
            } else if (arg.arg2.type == Type.NUMBER) {
                short t;
                t = Code.form_R16(0xC000, arg1, 8);
                t = Code.form_R16(t, arg.arg1, 10);
                t = (short) (t | (((Argument.Number) arg.arg2).value & 0xff));
                rr.add(t);
            } else {
                throw ParserException.invalidArgumentType();
            }
            throw ParserException.invalidArgumentType();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}


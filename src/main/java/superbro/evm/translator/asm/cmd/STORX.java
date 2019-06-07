package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class STORX extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg2.type != Type.REG16) {
            throw ParserException.invalidArgumentType();
        }
        if (arg1.type == Type.INDEX) {
            Argument.Index arg = (Argument.Index) arg1;
            if (arg.argument.type != Type.REG16) {
                throw ParserException.invalidArgumentType();
            }
            short t;
            t = Code.form_R16(0x9900, arg2, 0);
            t = Code.form_R16(t, arg.argument, 4);
            rr.add(t);
            return;
        }
        if (arg1.type == Type.INDEXPLUS) {
            Argument.IndexPlus arg = (Argument.IndexPlus) arg1;
            if (arg.arg1.type != Type.REG16) {
                throw ParserException.invalidArgumentType();
            }
            if (arg.arg2.type == Type.REG8) {
                short t;
                t = Code.form_R8(0x9A00, arg.arg2, 4);
                t = Code.form_R16(t, arg2, 0);
                t = Code.form_R16(t, arg.arg1, 2);
                rr.add(t);
                return;
            } else if (arg.arg2.type == Type.NUMBER) {
                short t;
                t = Code.form_R16(0xD000, arg2, 8);
                t = Code.form_R16(t, arg.arg1, 10);
                t = (short) (t | (((Argument.Number) arg.arg1).value & 0xff));
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

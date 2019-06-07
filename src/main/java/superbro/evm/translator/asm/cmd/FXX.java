package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Token;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class FXX extends Command {

    boolean setter;
    int flag;

    public FXX(boolean setter, int flag) {
        this.setter = setter;
        this.flag = flag;
    }

    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        int r = setter ? 0x0700 : 0x0710;
        rr.add((short) (r + flag));
        if (arg1.type != Type.NONE) {
            throw ParserException.redundantArgument();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

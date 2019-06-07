package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class RJMP extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.NUMBER) {
            rr.add(Code.gen_N8(0x0400, arg1));
            return;
        }
        else if(arg1.type == Type.REL_ADDRESS){
            rr.add(Code.gen_N8(0x0400, ((Argument.RelAddress)arg1).offset));
            return;
        }
        throw ParserException.redundantArgument();
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

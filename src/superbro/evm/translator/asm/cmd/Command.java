package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;

import java.util.List;

public abstract class Command {

    public abstract void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException;
}

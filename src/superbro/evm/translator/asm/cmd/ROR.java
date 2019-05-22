package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class ROR extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if (arg1.type == Type.REG8) {
            if(arg2.type==Type.NUMBER){
                if(((Argument.Number)arg2).value >= 8){
                    throw ParserException.numberMustBeUnder8();
                }
                rr.add(Code.gen_R8_N3(0x4800, arg1, arg2));
                return;
            }
            else{
                throw ParserException.invalidArgumentType();
            }
        }
        if (arg1.type == Type.REG16) {
            if(arg2.type==Type.NUMBER){
                if(((Argument.Number)arg2).value >= 16){
                    throw ParserException.numberMustBeUnder16();
                }
                rr.add(Code.gen_R16_N4(0x4900, arg1, arg2));
                return;
            }
            else{
                throw ParserException.invalidArgumentType();
            }
        }
        throw ParserException.invalidArgumentType();
    }

    @Override
    public int getSize(Token arg1, Token arg2) {
        return 1;
    }
}

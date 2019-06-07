package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.*;

import java.util.List;

public class POP extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
        if(arg2.type == Type.NUMBER){
            if(arg1.type == Type.REG8){
                short t;
                t = Code.form_R8(0xB000, arg1, 8);
                t = Code.form_N8(t, arg2, 0);
                rr.add(t);
            }
            else if(arg1.type == Type.REG16){
                short t;
                t = Code.form_R16(0xB800, arg1, 8);
                t = Code.form_N8(t, arg2, 0);
                rr.add(t);
            }
            else{
                throw ParserException.invalidArgumentType();
            }
        }
        else if(arg2.type == Type.NONE){
            if(arg1.type == Type.REG8){
                rr.add(Code.gen_R8(0xAA00, arg1));
            }
            else if(arg1.type == Type.REG16){
                rr.add(Code.gen_R16(0xAA10, arg1));
            }
            else{
                throw ParserException.invalidArgumentType();
            }
        }
        else{
            throw ParserException.invalidArgumentType();
        }
    }

    @Override
    public int getSize(Argument arg1, Argument arg2) {
        return 1;
    }
}

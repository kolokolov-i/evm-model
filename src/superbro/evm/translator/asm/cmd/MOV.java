package superbro.evm.translator.asm.cmd;

import superbro.evm.translator.asm.Argument;
import superbro.evm.translator.asm.ParserException;
import superbro.evm.translator.asm.Type;

import java.util.List;

public class MOV extends Command {
    @Override
    public void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
//        if (arg1.type == Type.REG8) {
//            if (arg2.type == Type.REG8) {
//                short r = (short) 0x8000;
//                r |= (arg1.value & 0b00000111) << 4;
//                r |= arg2.value & 0b00000111;
//                rr.add(r);
//                return;
//            } else if (arg2.type == Type.NUMBER) {
//                short r = (short) 0x8800;
//                r |= (arg1.value & 0b00000111) << 8;
//                r |= arg2.value & 0xff;
//                rr.add(r);
//                return;
//            } else {
//                throw new ParserException("Invalid argument type");
//            }
//        }
//        if (arg1.type == Type.REG16) {
//            if (arg2.type == Type.REG16) {
//                short r = (short) 0x8100;
//                r |= (arg1.value & 0b00000011) << 4;
//                r |= arg2.value & 0b00000011;
//                rr.add(r);
//                return;
//            }
//        } else {
//            throw new ParserException("Invalid argument type");
//        }
//        throw new ParserException("Invalid argument type");
    }
}

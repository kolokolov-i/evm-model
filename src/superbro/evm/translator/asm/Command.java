package superbro.evm.translator.asm;

import java.util.List;

abstract class Command {

    //abstract void check(Argument arg1, Argument arg2) throws ParserException;
    abstract void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException;

    static class MOV extends Command {
        @Override
        void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
            if (arg1.type == Type.REG8) {
                if (arg2.type == Type.REG8) {
                    short r = (short) 0x8000;
                    r |= (arg1.value & 0b00000111) << 4;
                    r |= arg2.value & 0b00000111;
                    rr.add(r);
                    return;
                } else if (arg2.type == Type.NUMBER) {
                    short r = (short) 0x8800;
                    r |= (arg1.value & 0b00000111) << 8;
                    r |= arg2.value & 0xff;
                    rr.add(r);
                    return;
                } else {
                    throw new ParserException("Invalid argument type");
                }
            }
            if (arg1.type == Type.REG16) {
                if (arg2.type == Type.REG16) {
                    short r = (short) 0x8100;
                    r |= (arg1.value & 0b00000011) << 4;
                    r |= arg2.value & 0b00000011;
                    rr.add(r);
                    return;
                }
            } else {
                throw new ParserException("Invalid argument type");
            }
            throw new ParserException("Invalid argument type");
        }
    }

    static class MOVI extends Command {
        @Override
        void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
            if (arg1.type == Type.REG8) {
                if (arg2.type == Type.NUMBER) {
                    short r = (short) 0x8800;
                    r |= (arg1.value & 0b00000111) << 8;
                    r |= arg2.value & 0xff;
                    rr.add(r);
                    return;
                }
            }
            throw new ParserException("Invalid argument type");
        }
    }

    static class INT extends Command {
        @Override
        void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
            if (arg1.type == Type.NUMBER) {
                short r = (short) 0x0160;
                if (arg1.value >= 16) {
                    throw new ParserException("Number must be <16");
                }
                r |= (arg1.value & 0x0f) << 4;
                if (arg2.type != Type.NONE) {
                    throw new ParserException("Redundant argument");
                }
                rr.add(r);
                return;
            }
            throw new ParserException("Invalid argument type");
        }
    }

    static class ADD extends Command {

        @Override
        void generate(List<Short> rr, Argument arg1, Argument arg2) throws ParserException {
            if (arg1.type == Type.REG8) {
                if (arg2.type == Type.REG8) {
                    short r = (short) 0x5000;
                    r |= (arg1.value & 0b00000111) << 4;
                    r |= arg2.value & 0b00000111;
                    rr.add(r);
                    return;
                } else if (arg2.type == Type.NUMBER) {
                    short r = (short) 0x5800;
                    r |= (arg1.value & 0b00000111) << 8;
                    r |= arg2.value & 0xff;
                    rr.add(r);
                    return;
                } else {
                    throw new ParserException("Invalid argument type");
                }
            }
            if (arg1.type == Type.REG16) {
                if (arg2.type == Type.REG16) {
                    short r = (short) 0x5100;
                    r |= (arg1.value & 0b00000011) << 4;
                    r |= arg2.value & 0b00000011;
                    rr.add(r);
                    return;
                }
            } else {
                throw new ParserException("Invalid argument type");
            }
            throw new ParserException("Invalid argument type");
        }
    }
}

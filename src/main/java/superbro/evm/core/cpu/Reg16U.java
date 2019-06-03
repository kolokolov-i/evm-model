package superbro.evm.core.cpu;

public class Reg16U extends Reg {

    public Reg8 rH, rL;

    public Reg16U(Reg8 a, Reg8 b){
        rH = a;
        rL = b;
    }

}

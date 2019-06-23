package superbro.evm.core.device;

import superbro.evm.core.cpu.Reg8;

public class DeviceCall {

    byte p0, p1, p2, p3;

    public DeviceCall(Reg8 r0, Reg8 r1, Reg8 r2, Reg8 r3) {
        p0 = r0.value;
        p1 = r1.value;
        p2 = r2.value;
        p3 = r3.value;
    }

    @Override
    public String toString() {
        return "DeviceCall{" +
                "p0=" + p0 +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                '}';
    }
}

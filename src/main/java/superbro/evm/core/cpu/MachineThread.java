package superbro.evm.core.cpu;

import superbro.evm.core.Machine;

public class MachineThread extends Thread {

    private Machine machine;

    public MachineThread(Machine m){
        machine = m;
    }

    @Override
    public void run(){
        boolean flag = true;
        while(flag){
            machine.step();
            try{
                Thread.sleep(20);
            }
            catch(InterruptedException ex){
                flag = false;
            }
        }
    }
}

package superbro.evm.core.cpu;

import superbro.evm.core.Machine;

public class MachineThread extends Thread {

    private Machine machine;

    public MachineThread(Machine m){
        machine = m;
        this.setDaemon(true);
    }

    @Override
    public void run(){
        boolean flag = true;
        while(flag){
            machine.step();
            machine.handleDevices();
            try{
                Thread.sleep(20);
            }
            catch(InterruptedException ex){
                flag = false;
            }
        }
    }
}

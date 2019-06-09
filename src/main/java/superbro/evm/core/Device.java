package superbro.evm.core;

import superbro.evm.core.device.DeviceCall;

import javax.swing.*;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public abstract class Device {

    private Queue<DeviceCall> callQueue;
    protected boolean autostart;
    protected Machine holder;

    public Device(Machine m) {
        holder = m;
        callQueue = new ConcurrentLinkedQueue<>();
    }

    public abstract void launch();

    public abstract JPanel getOptionsPane();

    public abstract String getName();

    public boolean isAutostart() {
        return autostart;
    }

    protected abstract void handle(DeviceCall call);

    public void handleCall(){
        DeviceCall call = callQueue.poll();
        if(call!=null){
            handle(call);
        }
    }

    public void call(DeviceCall call){
        callQueue.offer(call);
    }

    public abstract static class DeviceBuilder {

        public abstract Device create(Machine holder);
    }
}

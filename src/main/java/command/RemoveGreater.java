package command;

import utility.Receiver;

import java.net.SocketAddress;

public class RemoveGreater extends CommandAbstract{
    private final Receiver receiver;
    public RemoveGreater(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.removeGreater(name, socketAddress);
    }
}

package command;

import utility.Receiver;

import java.net.SocketAddress;

public class RemoveById extends CommandAbstract{
    private final Receiver receiver;
    public RemoveById(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.removeById(arg, name, socketAddress);
    }
}

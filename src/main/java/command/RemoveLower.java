package command;

import utility.Receiver;

import java.net.SocketAddress;

public class RemoveLower extends CommandAbstract{
    private final Receiver receiver;
    public RemoveLower(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.removeLower(name, socketAddress);
    }
}

package command;

import utility.Receiver;

import java.net.SocketAddress;

public class AddIfMax extends CommandAbstract{
    private final Receiver receiver;
    public AddIfMax(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.addIfMax(name, socketAddress);
    }
}

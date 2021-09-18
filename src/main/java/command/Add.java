package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Add extends CommandAbstract{
    private final Receiver receiver;
    public Add(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.add(name, socketAddress);
    }

}

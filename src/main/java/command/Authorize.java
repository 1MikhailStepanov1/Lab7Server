package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Authorize extends CommandAbstract{
    private final Receiver receiver;
    public Authorize(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.authorize(name, password, socketAddress);
    }

}

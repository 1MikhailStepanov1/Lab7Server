package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Show extends CommandAbstract{
    private final Receiver receiver;
    public Show(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.show(socketAddress);
    }
}

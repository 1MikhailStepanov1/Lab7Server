package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Info extends CommandAbstract{
    private final Receiver receiver;
    public Info(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.info(socketAddress);
    }
}

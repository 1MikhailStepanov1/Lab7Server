package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Clear extends CommandAbstract {
    private final Receiver receiver;
    public Clear(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.clear(name, socketAddress);
    }
}

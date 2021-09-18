package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Update extends CommandAbstract{
    private final Receiver receiver;
    public Update(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.update(arg, name, socketAddress);
    }
}

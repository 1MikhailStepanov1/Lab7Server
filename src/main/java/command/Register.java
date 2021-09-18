package command;

import utility.Receiver;

import java.net.SocketAddress;

public class Register extends CommandAbstract {
    private final Receiver receiver;
    public Register(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.register(name, password, socketAddress);
    }
}

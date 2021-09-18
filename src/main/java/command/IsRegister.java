package command;

import utility.Receiver;

import java.net.SocketAddress;

public class IsRegister extends CommandAbstract {
    private final Receiver receiver;
    public IsRegister(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.isRegister(name, socketAddress);
    }
}

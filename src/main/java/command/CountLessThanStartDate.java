package command;

import utility.Receiver;

import java.net.SocketAddress;

public class CountLessThanStartDate extends CommandAbstract{
    private final Receiver receiver;
    public CountLessThanStartDate(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.countLessThanStartDate(arg, socketAddress);
    }
}

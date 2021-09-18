package command;

import utility.Receiver;

import java.net.SocketAddress;

public class FilterGreaterThanStartDate extends CommandAbstract{
    private final Receiver receiver;
    public FilterGreaterThanStartDate(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.filterGreaterThanStartDate(arg, socketAddress);
    }
}

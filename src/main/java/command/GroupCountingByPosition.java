package command;

import utility.Receiver;

import java.net.SocketAddress;

public class GroupCountingByPosition extends CommandAbstract{
    private final Receiver receiver;
    public GroupCountingByPosition(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress){
        receiver.groupCountingByPosition(socketAddress);
    }
}

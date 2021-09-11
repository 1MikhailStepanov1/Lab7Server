package command;

import utility.Receiver;

public class GroupCountingByPosition extends CommandAbstract{
    private final Receiver receiver;
    public GroupCountingByPosition(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.groupCountingByPosition();
    }
}

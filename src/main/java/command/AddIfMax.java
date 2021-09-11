package command;

import utility.Receiver;

public class AddIfMax extends CommandAbstract{
    private final Receiver receiver;
    public AddIfMax(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.addIfMax();
    }
}

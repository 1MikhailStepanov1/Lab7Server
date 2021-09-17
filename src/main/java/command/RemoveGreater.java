package command;

import utility.Receiver;

public class RemoveGreater extends CommandAbstract{
    private final Receiver receiver;
    public RemoveGreater(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.removeGreater(name);
    }
}

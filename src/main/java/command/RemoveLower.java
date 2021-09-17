package command;

import utility.Receiver;

public class RemoveLower extends CommandAbstract{
    private final Receiver receiver;
    public RemoveLower(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.removeLower(name);
    }
}

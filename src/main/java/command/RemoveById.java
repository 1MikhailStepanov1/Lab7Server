package command;

import utility.Receiver;

public class RemoveById extends CommandAbstract{
    private final Receiver receiver;
    public RemoveById(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.removeById(arg, name);
    }
}

package command;

import utility.Receiver;

public class Update extends CommandAbstract{
    private final Receiver receiver;
    public Update(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.update(arg, name);
    }
}

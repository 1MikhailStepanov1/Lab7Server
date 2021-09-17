package command;

import utility.Receiver;

public class Add extends CommandAbstract{
    private final Receiver receiver;
    public Add(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.add(name);
    }

}

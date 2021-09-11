package command;

import utility.Receiver;

public class Authorize extends CommandAbstract{
    private final Receiver receiver;
    public Authorize(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.authorize(arg);
    }

}

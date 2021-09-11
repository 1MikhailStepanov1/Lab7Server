package command;

import utility.Receiver;

public class Info extends CommandAbstract{
    private final Receiver receiver;
    public Info(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.info();
    }
}

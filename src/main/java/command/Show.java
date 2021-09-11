package command;

import utility.Receiver;

public class Show extends CommandAbstract{
    private final Receiver receiver;
    public Show(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.show();
    }
}

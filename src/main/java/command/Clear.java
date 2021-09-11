package command;

import utility.Receiver;

public class Clear extends CommandAbstract {
    private final Receiver receiver;
    public Clear(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.clear();
    }
}

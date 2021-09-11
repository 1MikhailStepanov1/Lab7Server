package command;

import utility.Receiver;

public class Register extends CommandAbstract {
    private final Receiver receiver;
    public Register(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.register(arg);
    }
}

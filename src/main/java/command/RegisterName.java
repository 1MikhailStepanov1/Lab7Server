package command;

import utility.Receiver;

public class RegisterName extends CommandAbstract{
    private final Receiver receiver;
    public RegisterName(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.registerName(arg);
    }
}

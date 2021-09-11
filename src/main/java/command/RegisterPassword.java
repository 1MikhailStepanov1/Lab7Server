package command;

import utility.Receiver;

public class RegisterPassword extends CommandAbstract{
    private final Receiver receiver;
    public RegisterPassword(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.registerPassword(arg);
    }
}
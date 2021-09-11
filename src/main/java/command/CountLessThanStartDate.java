package command;

import utility.Receiver;

public class CountLessThanStartDate extends CommandAbstract{
    private final Receiver receiver;
    public CountLessThanStartDate(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg){
        receiver.countLessThanStartDate(arg);
    }
}

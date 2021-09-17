package command;

import utility.Receiver;

public class FilterGreaterThanStartDate extends CommandAbstract{
    private final Receiver receiver;
    public FilterGreaterThanStartDate(Receiver receiver){
        this.receiver = receiver;
    }
    @Override
    public void exe(String arg, String name, String password){
        receiver.filterGreaterThanStartDate(arg);
    }
}

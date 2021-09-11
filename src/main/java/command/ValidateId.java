package command;

import utility.Receiver;

public class ValidateId extends CommandAbstract {
    private final Receiver receiver;

    public ValidateId(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exe(String arg) {
        receiver.validateId(arg);
    }
}

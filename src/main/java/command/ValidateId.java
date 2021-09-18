package command;

import utility.Receiver;

import java.net.SocketAddress;

public class ValidateId extends CommandAbstract {
    private final Receiver receiver;

    public ValidateId(Receiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress) {
        receiver.validateId(arg, name, socketAddress);
    }
}

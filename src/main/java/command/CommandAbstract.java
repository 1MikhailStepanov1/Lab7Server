package command;

import java.net.SocketAddress;

/**
 * Abstract command class contains name and description
 */
public class CommandAbstract implements CommandInterface {

    public CommandAbstract() {

    }

    @Override
    public void exe(String arg, String name, String password, SocketAddress socketAddress) {

    }
}

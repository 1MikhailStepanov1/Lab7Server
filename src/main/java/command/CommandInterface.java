package command;

import java.net.SocketAddress;

/**
 * Interface for commands
 */
public interface CommandInterface {

    void exe(String arg, String name, String password, SocketAddress socketAddress);
}

package utility;

import command.*;
import utility.database.DatabaseManager;

import java.net.SocketAddress;
import java.util.HashMap;


/**
 * This class contains map with commands which can be execute
 */
public class Invoker {
    private final HashMap<String, CommandInterface> commands;
    private final Receiver receiver;
    private final DatabaseManager databaseManager;

    public Invoker(Receiver receiver, DatabaseManager databaseManager) {
        commands = new HashMap<>();
        this.receiver = receiver;
        this.databaseManager = databaseManager;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    /**
     * Initialize commands map
     */
    public void initMap() {
        commands.put("info", new Info(receiver));
        commands.put("show", new Show(receiver));
        commands.put("add", new Add(receiver));
        commands.put("update", new Update(receiver));
        commands.put("remove_by_id", new RemoveById(receiver));
        commands.put("clear", new Clear(receiver));
        commands.put("add_if_max", new AddIfMax(receiver));
        commands.put("remove_greater", new RemoveGreater(receiver));
        commands.put("remove_lower", new RemoveLower(receiver));
        commands.put("group_counting_by_position", new GroupCountingByPosition(receiver));
        commands.put("count_less_than_start_date", new CountLessThanStartDate(receiver));
        commands.put("filter_greater_than_start_date", new FilterGreaterThanStartDate(receiver));
        commands.put("validate_id", new ValidateId(receiver));
        commands.put("isRegister", new IsRegister(receiver));
        commands.put("register", new Register(receiver));
        commands.put("authorize", new Authorize(receiver));
    }

    public void exe(String command, String arg, String name, String password, SocketAddress socketAddress) {
        if (command.equals("isRegister") || command.equals("registerName") || command.equals("registerPassword") || command.equals("register") || databaseManager.checkUser(name, password)) {
            if (commands.containsKey(command)) {
                commands.get(command).exe(arg, name, password, socketAddress);
            } else {
                System.out.println("Input is incorrect.");
            }
        } else receiver.wrongSession(socketAddress);
    }

}

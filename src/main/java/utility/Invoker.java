package utility;

import command.*;
import utility.database.DatabaseManager;

import java.util.HashMap;
import java.util.concurrent.locks.ReentrantLock;


/**
 * This class contains map with commands which can be execute
 */
public class Invoker {
    private final HashMap<String, CommandInterface> commands;
    private final Receiver receiver;
    private ReentrantLock lock = new ReentrantLock();
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
        commands.put("registerPassword", new RegisterPassword(receiver));
        commands.put("register", new Register(receiver));
    }

    private class RunRequest implements Runnable {
        String command;
        String arg;
        HashMap<String, CommandInterface> commands;
        ReentrantLock locker;

        private RunRequest(String command, String arg, HashMap commands, ReentrantLock locker) {
            this.command = command;
            this.arg = arg;
            this.commands = commands;
            this.locker = locker;
        }

        @Override
        public void run() {
            locker.lock();
            if (command.equals("isRegister") || command.equals("registerName") || command.equals("registerPassword") || command.equals("register") || databaseManager.checkUser()) {
                if (commands.containsKey(command)) {
                    commands.get(command).exe(arg);
                    locker.unlock();
                } else {
                    System.out.println("Input is incorrect.");
                }
            } else receiver.wrongSession();
        }
    }

    public void exe(String command, String arg) {
        RunRequest runRequest = new RunRequest(command, arg, commands, lock);
        new Thread(runRequest).start();
    }
}

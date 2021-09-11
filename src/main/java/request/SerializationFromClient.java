package request;

import data.Worker;

import java.io.Serializable;

public class SerializationFromClient implements Serializable {
    private String command;
    private String arg;
    private Worker worker;
    private String name;

    public SerializationFromClient(String command, String arg, Worker worker, String name) {
        this.command = command;
        this.arg = arg;
        this.worker = worker;
        this.name = name;
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }

    public Worker getWorker() {
        return worker;
    }

    public String getName() {
        return name;
    }


}

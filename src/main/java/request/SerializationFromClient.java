package request;

import data.Worker;

import java.io.Serializable;

public class SerializationFromClient implements Serializable {
    private String command;
    private String arg;
    private Worker worker;
    private String name;
    private String password;

    public SerializationFromClient(String command, String arg, Worker worker, String name, String password) {
        this.command = command;
        this.arg = arg;
        this.worker = worker;
        this.name = name;
        this.password = password;
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
    public String getPassword(){
        return password;
    }

}

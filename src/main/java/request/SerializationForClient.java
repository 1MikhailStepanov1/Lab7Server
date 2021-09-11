package request;

import data.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class SerializationForClient implements Serializable {
    private boolean status;
    private String message;
    private Long count;
    private LinkedList<Worker> workers;

    public SerializationForClient(boolean st, String mes, Long co, LinkedList<Worker> w) {
        setStatus(st);
        setMessage(mes);
        setCount(co);
        setWorkers(w);
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Long getCount() {
        return count;
    }
    public LinkedList<Worker> getWorkers(){
        return workers;
    }

    public void setWorkers(LinkedList<Worker> workers) {
        this.workers = workers;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

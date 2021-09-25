package request;

import org.slf4j.Logger;
import utility.Invoker;
import utility.WorkerFactory;
import java.net.DatagramPacket;
import java.net.SocketAddress;

public class RequestExecutor {
    private final WorkerFactory workerFactory;
    private final Logger logger;
    private final Invoker invoker;
    private DatagramPacket data;

    public RequestExecutor(WorkerFactory workerFactory, Logger logger, Invoker invoker) {
        this.workerFactory = workerFactory;
        this.logger = logger;
        this.invoker = invoker;
    }

    public DatagramPacket getData() {
        return data;
    }

    public void setData(DatagramPacket data) {
        this.data = data;
    }

    public void execute(SerializationFromClient serializationFromClient, SocketAddress socketAddress) {
        RequestExecutorRunnable requestExecutorRunnable = new RequestExecutorRunnable(serializationFromClient, socketAddress);
        new Thread(requestExecutorRunnable).start();
    }

    private class RequestExecutorRunnable implements Runnable {
        private SerializationFromClient clientRequest;
        private SocketAddress socketAddress;

        private RequestExecutorRunnable(SerializationFromClient serializationFromClient, SocketAddress socketAddress) {
            clientRequest = serializationFromClient;
            this.socketAddress = socketAddress;
        }

        public void run() {
            String command;
            String arg;
            String name;
            String password;
            if (clientRequest != null) {
                command = clientRequest.getCommand();
                arg = clientRequest.getArg();
                if (clientRequest.getWorker() != null) {
                    workerFactory.setLoadObject(clientRequest.getWorker());
                }
                name = clientRequest.getName();
                password = clientRequest.getPassword();
                invoker.exe(command, arg, name, password, socketAddress);
            }

        }
    }
}




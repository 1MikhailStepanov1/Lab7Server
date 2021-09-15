package request;

import org.slf4j.Logger;
import utility.Invoker;
import utility.WorkerFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;

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

    public RequestExecutorRunnable execute(DatagramPacket datagramPacket) {
        return new RequestExecutorRunnable(datagramPacket);
    }

    private class RequestExecutorRunnable implements Runnable {
        private DatagramPacket datagramPacket;

        private RequestExecutorRunnable(DatagramPacket datagramPacket) {
            this.datagramPacket = datagramPacket;
        }

        public void run() {
            Object raw;
            String command;
            String arg;
            String name;
            String password;
            SerializationFromClient clientRequest = new SerializationFromClient("", "", null, null, null);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
            ObjectInputStream objectInputStream = null;
            try {
                objectInputStream = new ObjectInputStream(byteArrayInputStream);
                raw = objectInputStream.readObject();
                if (raw instanceof SerializationFromClient) {
                    clientRequest = (SerializationFromClient) raw;
                    logger.info("Received command: " + clientRequest.getCommand() + " " + clientRequest.getArg());
                }
                if (clientRequest != null) {
                    command = clientRequest.getCommand();
                    arg = clientRequest.getArg();
                    if (clientRequest.getWorker() != null) {
                        workerFactory.setLoadObject(clientRequest.getWorker());
                    }
                    name = clientRequest.getName();
                    password = clientRequest.getPassword();
                    invoker.getReceiver().getDatabaseManager().setName(name);
                    invoker.getReceiver().getDatabaseManager().setPassword(password);
                    invoker.exe(command, arg);
                }
            } catch (IOException | ClassNotFoundException exception) {
                exception.printStackTrace();
            }

        }
    }
}
//    public void run(DatagramPacket packet){
//        Object raw;
//        String command;
//        String arg;
//        String name;
//        SerializationFromClient clientRequest = new SerializationFromClient("", "", null,null);
//        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(packet.getData());
//        ObjectInputStream objectInputStream = null;
//        try {
//            objectInputStream = new ObjectInputStream(byteArrayInputStream);
//            raw = objectInputStream.readObject();
//            if (raw instanceof SerializationFromClient) {
//                clientRequest = (SerializationFromClient) raw;
//                logger.info("Received command: " + clientRequest.getCommand() + " " + clientRequest.getArg());
//            }
//            if (clientRequest != null) {
//                command = clientRequest.getCommand();
//                arg = clientRequest.getArg();
//                if (clientRequest.getWorker() != null) {
//                    workerFactory.setLoadObject(clientRequest.getWorker());
//                }
//                name = clientRequest.getName();
//                invoker.getReceiver().setName(name);
//                invoker.exe(command, arg);
//            }
//        } catch (IOException | ClassNotFoundException exception) {
//            exception.printStackTrace();
//        }
//
//    }





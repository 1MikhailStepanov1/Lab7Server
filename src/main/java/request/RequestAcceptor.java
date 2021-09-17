package request;

import org.slf4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestAcceptor extends Thread {
    private final AnswerSender answerSender;
    private final RequestExecutor requestExecutor;
    private Logger logger;
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() / 3);


    public RequestAcceptor(AnswerSender answerSender, RequestExecutor requestExecutor, Logger logger) {
        this.answerSender = answerSender;
        this.requestExecutor = requestExecutor;
        this.logger = logger;
    }

    public RequestAcceptorRunnable execute(DatagramSocket datagramSocket) {
        return new RequestAcceptorRunnable(datagramSocket);
    }
    public void acceptRequest(DatagramSocket datagramSocket) {
        executor.execute(execute(datagramSocket));
    }

    private class RequestAcceptorRunnable implements Runnable {
        private final DatagramSocket datagramSocket;

        private RequestAcceptorRunnable(DatagramSocket datagramSocket) {
            this.datagramSocket = datagramSocket;
        }

        @Override
        public void run() {
            while (!isInterrupted()) {
                byte[] acceptedRequest = new byte[2048];
                Object raw;
                try {
                    DatagramPacket datagramPacket = new DatagramPacket(acceptedRequest, acceptedRequest.length);
                    datagramSocket.receive(datagramPacket);
                    answerSender.setSocketAddress(datagramPacket.getSocketAddress());
                    SerializationFromClient clientRequest;
                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                    ObjectInputStream objectInputStream;
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);
                    raw = objectInputStream.readObject();
                    if (raw instanceof SerializationFromClient) {
                        clientRequest = (SerializationFromClient) raw;
                        logger.info("Received command: " + clientRequest.getCommand() + " " + clientRequest.getArg());
                        requestExecutor.execute(clientRequest);
                        interrupt();
                    }
                } catch (IOException | ClassNotFoundException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }
}

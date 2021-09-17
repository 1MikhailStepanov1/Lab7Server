package request;

import data.Worker;
import org.slf4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AnswerSender {
    private final Logger logger;
    private SocketAddress socketAddress;
    private final Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/3);

    public AnswerSender(Logger logger) {
        this.logger = logger;
    }

    public void setSocketAddress(SocketAddress socketAddress) {
        this.socketAddress = socketAddress;
    }

    private class SendingAnswer implements Runnable{
        private SerializationForClient answer;
        private SendingAnswer(SerializationForClient answer){
            this.answer = answer;
        }
        @Override
        public void run() {
            if (answer == null) {
                return;
            }
            try {
                DatagramSocket datagramSocket = new DatagramSocket();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                datagramSocket.connect(socketAddress);
                objectOutputStream.writeObject(answer);
                objectOutputStream.flush();
                DatagramPacket datagramPacket = new DatagramPacket(byteArrayOutputStream.toByteArray(), byteArrayOutputStream.toByteArray().length);
                datagramSocket.send(datagramPacket);
                logger.info("Answer has been sent to " + datagramSocket.getRemoteSocketAddress());
            } catch (IOException exception) {
                logger.info("Failed sending answer." + exception.getMessage() + exception.getCause());
                exception.printStackTrace();
            }
            answer = null;
        }
    }
    public void sendAnswer(SerializationForClient serializationForClient) {
        executor.execute(new SendingAnswer(serializationForClient));
    }
}

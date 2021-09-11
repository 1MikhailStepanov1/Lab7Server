package request;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RequestAcceptor extends Thread{
    private final AnswerSender answerSender;
    private final RequestExecutor requestExecutor;


    public RequestAcceptor(AnswerSender answerSender, RequestExecutor requestExecutor){
        this.answerSender = answerSender;
        this.requestExecutor = requestExecutor;
    }

    public void acceptRequest(DatagramSocket datagramSocket) {
        Executor executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()/3);
        while (!isInterrupted()) {
            byte[] acceptedRequest = new byte[2048];
            try {
                DatagramPacket datagramPacket = new DatagramPacket(acceptedRequest, acceptedRequest.length);
                datagramSocket.receive(datagramPacket);
                answerSender.setSocketAddress(datagramPacket.getSocketAddress());
                requestExecutor.setData(datagramPacket);
                executor.execute(requestExecutor.execute(datagramPacket));
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }
        }
    }
}

import exceptions.NoDatabaseDataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import request.AnswerSender;
import request.RequestAcceptor;
import request.RequestExecutor;
import utility.*;
import utility.database.DatabaseConnector;
import utility.database.DatabaseInitializer;
import utility.database.DatabaseManager;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {


    public static void main(String[] args) {
        DatagramSocket datagramSocket;
        CollectionManager collectionManager = new CollectionManager();
        int port = 9898;
        try {
            if (args.length > 0) {
                port = Integer.parseInt(args[1]);
            }
        } catch (NumberFormatException exception) {
            System.out.println("Incorrect format of port.");
        }
        if (port == 9898) {
            System.out.println("Port hasn't been identified. " + port + " will be used.");
        }
        try {
            datagramSocket = new DatagramSocket(port);
        } catch (SocketException e) {
            System.out.println("Failed creating socket. Socket is already used.");
            return;
        }
        Logger logger = LoggerFactory.getLogger("Server");
        AnswerSender answerSender = new AnswerSender(logger);
        System.out.println(datagramSocket.getLocalSocketAddress());
        answerSender.setSocketAddress(datagramSocket.getLocalSocketAddress());
        WorkerFactory workerFactory = new WorkerFactory();
        workerFactory.setStartId(collectionManager.getLastId());
//        Connection connection = null;
//        try {
//           connection = DatabaseConnector.connection();
//        } catch (SQLException exception) {
//            System.out.println("Database connection problems.");
//            exception.printStackTrace();
//            return;
//        } catch (NoDatabaseDataException exception) {
//            System.out.println(exception.getMessage());
//            return;
//        }
        DatabaseConnector databaseConnector = new DatabaseConnector();
        Connection connection = databaseConnector.makeConnection();
        DatabaseInitializer databaseInitializer = new DatabaseInitializer(connection);
        try {
            databaseInitializer.initializeTable();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        DatabaseManager databaseManager;
        try {
            databaseManager = new DatabaseManager(connection, collectionManager);
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Hashing algorithm not found.");
            e.printStackTrace();
            return;
        }
        databaseManager.updateCollection();
        Receiver receiver = new Receiver(collectionManager, answerSender, workerFactory, databaseManager);
        Invoker invoker = new Invoker(receiver, databaseManager);
        invoker.initMap();
        RequestExecutor executor = new RequestExecutor(workerFactory, logger, invoker);
        RequestAcceptor requestAcceptor = new RequestAcceptor(answerSender, executor);
        System.out.println("Server is ready to work.");
        requestAcceptor.acceptRequest(datagramSocket);
        try {
            logger.info("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
            System.out.println("Server started on address " + InetAddress.getLocalHost() + " port: " + port);
        } catch (UnknownHostException exception) {
            exception.printStackTrace();
        }
        requestAcceptor.interrupt();
    }
}


package utility;

import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.AnswerSender;
import request.RequestAcceptor;
import request.SerializationForClient;
import utility.database.DatabaseCommandResult;
import utility.database.DatabaseManager;

import java.util.ArrayList;
import java.util.LinkedList;

public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(RequestAcceptor.class);
    private final CollectionManager collectionManager;
    private final WorkerFactory workerFactory;
    private final AnswerSender answerSender;
    private final DatabaseManager databaseManager;


    public Receiver(CollectionManager collectionManager, AnswerSender answerSender, WorkerFactory workerFactory, DatabaseManager databaseManager) {
        this.collectionManager = collectionManager;
        this.answerSender = answerSender;
        this.workerFactory = workerFactory;
        this.databaseManager = databaseManager;

    }

    public void wrongSession(){
        answerSender.sendAnswer(new SerializationForClient(false, "Incorrect user session.", null, null));
    }
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public void isRegister(String name){
        logger.info("Validation of user name was sent to client.");
        answerSender.sendAnswer(new SerializationForClient(databaseManager.checkName(name), null, null ,null));
    }
    public void register(String name, String password){
        databaseManager.registerUser(name, password);
        logger.info("User was registered. Answer was sent to client.");
        answerSender.sendAnswer(new SerializationForClient(true, null, null ,null));
    }
    public void authorize(String name, String password){
        logger.info("User was authorized. Answer was sent to client.");
        answerSender.sendAnswer(new SerializationForClient(databaseManager.checkUser(name, password), null ,null, null));

    }
    public void add(String name) {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(databaseManager.getNewId());
        workerFactory.setStartId(worker.getId());
        if (databaseManager.addWorker(name, worker)) {
            collectionManager.add(worker);
            answerSender.sendAnswer(new SerializationForClient(true, "Worker was added to the collection.", null, null));
        } else {
            answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
        }
        logger.info("Result of command \"add\" has been sent to client.");
    }

    public void addIfMax(String name) {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(databaseManager.getNewId());
        workerFactory.setStartId(worker.getId());
        if (collectionManager.addIfMax(worker)){
            if (databaseManager.addWorker(name, worker)) {
                answerSender.sendAnswer(new SerializationForClient(true, "Worker was added to the collection.", null, null) );
            } else {
                answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
            }
        } else{
            answerSender.sendAnswer(new SerializationForClient(false, "There is a worker in collection, which is greater than indicated one.", null, null));
        }
        logger.info("Result of command \"add_if_max\" has been sent to client.");
    }

    public void clear(String name) {
        for (Worker worker : databaseManager.getByOwner(name)){
            collectionManager.removeById(worker.getId());
        }
        if (databaseManager.clear(name)) {
            answerSender.sendAnswer(new SerializationForClient(true, "Workers of user " + name + " has been cleared.", null, null));
        } else answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
        logger.info("Result of command \"clear\" has been sent to client.");
    }

    public void countLessThanStartDate(String arg) {
        Long answer = collectionManager.countLessThanStartDate(arg);
        answerSender.sendAnswer(new SerializationForClient(true, "Suitable elements in the collection: ", answer, null));
        logger.info("Result of command \"count_less_than_start_date\" has been sent to client.");
    }

    public void filterGreaterThanStartDate(String arg) {
        LinkedList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        if (answer == null) {
            answerSender.sendAnswer(new SerializationForClient(false, "Collection doesn't contains satisfying elements.", null, null));
        } else {
            answerSender.sendAnswer(new SerializationForClient(true, "Command has done successfully.", null, answer));
        }
        logger.info("Result of command \"filter_greater_than_start_date\" has been sent to client.");
    }

    public void groupCountingByPosition() {
        String answer = collectionManager.groupCountingByPosition();
        answerSender.sendAnswer(new SerializationForClient(true, answer, null, null));
        logger.info("Result of command \"group_counting_by_position\" has been sent to client.");
    }

    public void info() {
        String answer = collectionManager.getInfo();
        answerSender.sendAnswer(new SerializationForClient(true, answer, null, null));
        logger.info("Result of command \"info\" has been sent to client.");
    }

    public void removeById(String arg,String name) {
        long id;
        try {
            id = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            if (collectionValidator.checkExistId(id)) {
                DatabaseCommandResult answer = databaseManager.removeById(name, id);
                switch (answer) {
                    case GOOD:
                        collectionManager.removeById(id);
                        answerSender.sendAnswer(new SerializationForClient(true, "Element with id " + id + " has been removed.", null, null));
                        break;
                    case PERMISSION:
                        answerSender.sendAnswer(new SerializationForClient(false, "You are not allowed to change this worker.", null, null));
                        break;
                    case SQL:
                        answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
                        break;
                }
            } else {
                answerSender.sendAnswer(new SerializationForClient(false, "There is no elements with matched id in the collection.", null, null));
            }
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }
        logger.info("Result of command \"remove_by_id\" has been sent to client.");
    }

    public void removeGreater(String name) {
        Worker worker = (Worker) workerFactory.getLoadObject();
        ArrayList<Worker> temp = collectionManager.getGreater(worker);
        if (temp.isEmpty()){
            answerSender.sendAnswer(new SerializationForClient(false, "There is no elements in collection which are greater than indicated one.", null, null));
        } else {
            for (Worker worker1 : temp) {
                DatabaseCommandResult answer = databaseManager.removeById(name, worker1.getId());
                switch (answer) {
                    case GOOD:
                        collectionManager.removeById(worker1.getId());
                        answerSender.sendAnswer(new SerializationForClient(true, "Element with id " + worker1.getId() + " has been removed.", null, null));
                        break;
                    case PERMISSION:
                        answerSender.sendAnswer(new SerializationForClient(false, "You are not allowed to change this worker.", null, null));
                        break;
                    case SQL:
                        answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
                        break;
                }
            }
        }

        logger.info("Result of command \"remove_greater\" has been sent to client.");
    }

    public void removeLower(String name) {
        Worker worker = (Worker) workerFactory.getLoadObject();
        ArrayList<Worker> temp = collectionManager.getLower(worker);
        if (temp.isEmpty()){
            answerSender.sendAnswer(new SerializationForClient(false, "There is no elements in collection which are lower than indicated one.", null, null));
        } else {
            for (Worker worker1 : temp) {
                DatabaseCommandResult answer = databaseManager.removeById(name, worker1.getId());
                switch (answer) {
                    case GOOD:
                        collectionManager.removeById(worker1.getId());
                        answerSender.sendAnswer(new SerializationForClient(true, "Element with id " + worker1.getId() + " has been removed.", null, null));
                        break;
                    case PERMISSION:
                        answerSender.sendAnswer(new SerializationForClient(false, "You are not allowed to change this worker.", null, null));
                        break;
                    case SQL:
                        answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
                        break;
                }
            }
        }

        logger.info("Result of command \"remove_greater\" has been sent to client.");
    }

    public void show() {
        answerSender.sendAnswer(new SerializationForClient(true, "", null, collectionManager.show()));
        logger.info("Result of command \"show\" has been sent to client.");
    }

    public void update(String arg, String name) {
        Long tempId;
        Worker worker = (Worker) workerFactory.getLoadObject();
        try {
            tempId = Long.parseLong(arg);
            if (databaseManager.getByIdAndOwner(name, tempId)) {
                DatabaseCommandResult answer = databaseManager.update(name, worker, tempId);
                switch (answer) {
                    case GOOD:
                        collectionManager.update(tempId, worker);
                        answerSender.sendAnswer(new SerializationForClient(true, "Element with id " + tempId + " has been removed.", null, null));
                        break;
                    case PERMISSION:
                        answerSender.sendAnswer(new SerializationForClient(false, "You are not allowed to change this worker.", null, null));
                        break;
                    case SQL:
                        answerSender.sendAnswer(new SerializationForClient(false, "SQL problems.", null, null));
                        break;
                }
            } else {
                answerSender.sendAnswer(new SerializationForClient(false, "There is no elements with matched id in the collection.", null, null));
            }
        } catch (NumberFormatException exception) {
            answerSender.sendAnswer(new SerializationForClient(false, "Command is incorrect.", null, null));
        }
        logger.info("Result of command \"update\" has been sent to client.");
    }

    public void validateId(String arg, String name) {
        Long tempId;
        try {
            tempId = Long.parseLong(arg);
            boolean temp = databaseManager.getByIdAndOwner(name, tempId);
            if (temp) {
                answerSender.sendAnswer(new SerializationForClient(true, "", null, null));
            } else answerSender.sendAnswer(new SerializationForClient(false, "There is no element with the same id or you don't have permission to interact with it.", null, null));
        } catch (NumberFormatException exception) {
            answerSender.sendAnswer(new SerializationForClient(false, "", null, null));
        }
        logger.info("Result of validation has been sent to client.");
    }

}

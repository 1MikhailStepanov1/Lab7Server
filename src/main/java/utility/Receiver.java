package utility;

import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.AnswerSender;
import request.RequestAcceptor;
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
        answerSender.addToAnswer(false, "Incorrect user session.", null, null);
        answerSender.sendAnswer();
    }
    public DatabaseManager getDatabaseManager(){
        return databaseManager;
    }

    public void isRegister(){
        answerSender.addToAnswer(databaseManager.checkName(databaseManager.getName()), null, null ,null);
        answerSender.sendAnswer();
    }
    public void registerPassword(String arg){
        databaseManager.setPassword(arg);
    }
    public void register(String arg){
        databaseManager.registerUser();
        answerSender.addToAnswer(true, null, null ,null);
    }
    public void authorize(String arg){
        answerSender.addToAnswer(databaseManager.checkUser(), null ,null, null);
        answerSender.sendAnswer();

    }
    public void add() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(databaseManager.getNewId());
        workerFactory.setStartId(worker.getId());
        if (databaseManager.addWorker(databaseManager.getName(), worker)){
            collectionManager.add(worker);
            answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
        } else{
            answerSender.addToAnswer(false, "SQL problems.", null, null);
        }
        logger.info("Result of command \"add\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void addIfMax() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(databaseManager.getNewId());
        workerFactory.setStartId(worker.getId());
        if (collectionManager.addIfMax(worker)){
            if (databaseManager.addWorker(databaseManager.getName(), worker)) {
                answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
            } else {
                answerSender.addToAnswer(false, "SQL problems.", null, null);
            }
        } else{
            answerSender.addToAnswer(false, "There is a worker in collection, which is greater than indicated one.", null, null);
        }
        logger.info("Result of command \"add_if_max\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void clear() {
        collectionManager.clear();
        if (databaseManager.clear(databaseManager.getName())) {
            answerSender.addToAnswer(true, "Workers of user " + databaseManager.getName() + " has been cleared.", null, null);
        } else answerSender.addToAnswer(false, "SQL problems.", null, null);
        logger.info("Result of command \"clear\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void countLessThanStartDate(String arg) {
        Long answer = collectionManager.countLessThanStartDate(arg);
        answerSender.addToAnswer(true, "Suitable elements in the collection: ", answer, null);
        logger.info("Result of command \"count_less_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void filterGreaterThanStartDate(String arg) {
        LinkedList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        if (answer == null) {
            answerSender.addToAnswer(false, "Collection doesn't contains satisfying elements.", null, null);
        } else {
            answerSender.addToAnswer(true, "Command has done successfully.", null, answer);
        }
        logger.info("Result of command \"filter_greater_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void groupCountingByPosition() {
        String answer = collectionManager.groupCountingByPosition();
        answerSender.addToAnswer(true, answer, null, null);
        logger.info("Result of command \"group_counting_by_position\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void info() {
        String answer = collectionManager.getInfo();
        answerSender.addToAnswer(true, answer, null, null);
        logger.info("Result of command \"info\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeById(String arg) {
        long id;
        try {
            id = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            if (collectionValidator.checkExistId(id)) {
                String answer = databaseManager.removeById(databaseManager.getName(), id);
                switch (answer) {
                    case "good":
                        collectionManager.removeById(id);
                        answerSender.addToAnswer(true, "Element with id " + id + " has been removed.", null, null);
                        break;
                    case "permission":
                        answerSender.addToAnswer(false, "You are not allowed to change this worker.", null, null);
                        break;
                    case "SQl":
                        answerSender.addToAnswer(false, "SQL problems.", null, null);
                        break;
                }
            } else {
                answerSender.addToAnswer(false, "There is no elements with matched id in the collection.", null, null);
            }
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }
        logger.info("Result of command \"remove_by_id\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeGreater() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        ArrayList<Worker> temp = collectionManager.getGreater(worker);
        if (temp.isEmpty()){
            answerSender.addToAnswer(false, "There is no elements in collection which are greater than indicated one.", null, null);
        } else {
            for (Worker worker1 : temp) {
                String answer = databaseManager.removeById(databaseManager.getName(), worker1.getId());
                switch (answer) {
                    case "good":
                        collectionManager.removeById(worker1.getId());
                        answerSender.addToAnswer(true, "Element with id " + worker1.getId() + " has been removed.", null, null);
                        break;
                    case "permission":
                        answerSender.addToAnswer(false, "You are not allowed to change this worker.", null, null);
                        break;
                    case "SQl":
                        answerSender.addToAnswer(false, "SQL problems.", null, null);
                        break;
                }
            }
        }

        logger.info("Result of command \"remove_greater\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeLower() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        ArrayList<Worker> temp = collectionManager.getGreater(worker);
        if (temp.isEmpty()){
            answerSender.addToAnswer(false, "There is no elements in collection which are lower than indicated one.", null, null);
        } else {
            for (Worker worker1 : temp) {
                String answer = databaseManager.removeById(databaseManager.getName(), worker1.getId());
                switch (answer) {
                    case "good":
                        collectionManager.removeById(worker1.getId());
                        answerSender.addToAnswer(true, "Element with id " + worker1.getId() + " has been removed.", null, null);
                        break;
                    case "permission":
                        answerSender.addToAnswer(false, "You are not allowed to change this worker.", null, null);
                        break;
                    case "SQl":
                        answerSender.addToAnswer(false, "SQL problems.", null, null);
                        break;
                }
            }
        }

        logger.info("Result of command \"remove_greater\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void show() {
        answerSender.addToAnswer(true, "", null, collectionManager.show());
        logger.info("Result of command \"show\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void update(String arg) {
        Long tempId;
        Worker worker = (Worker) workerFactory.getLoadObject();
        try {
            tempId = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            if (collectionValidator.checkExistId(tempId)) {
                String answer = databaseManager.update(databaseManager.getName(), worker, tempId);
                switch (answer) {
                    case "good":
                        collectionManager.removeById(tempId);
                        answerSender.addToAnswer(true, "Element with id " + tempId + " has been removed.", null, null);
                        break;
                    case "permission":
                        answerSender.addToAnswer(false, "You are not allowed to change this worker.", null, null);
                        break;
                    case "SQl":
                        answerSender.addToAnswer(false, "SQL problems.", null, null);
                        break;
                }
            } else {
                answerSender.addToAnswer(false, "There is no elements with matched id in the collection.", null, null);
            }
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer(false, "Command is incorrect.", null, null);
        }
        logger.info("Result of command \"update\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void validateId(String arg) {
        Long tempId;
        try {
            tempId = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            answerSender.addToAnswer(collectionValidator.checkExistId(tempId), "", null, null);
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer(false, "", null, null);
        }
        logger.info("Result of validation has been sent to client.");
        answerSender.sendAnswer();
    }


}

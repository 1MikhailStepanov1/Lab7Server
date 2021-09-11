package utility.database;

import data.Worker;
import utility.CollectionManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Instant;

public class DatabaseManager {
    private final Connection connection;
    private final MessageDigest messageDigest;
    private final CollectionManager collectionManager;
    private String name;
    private String password;
    public DatabaseManager(Connection connection, CollectionManager collectionManager) throws NoSuchAlgorithmException {
        this.connection = connection;
        this.collectionManager = collectionManager;
        messageDigest = MessageDigest.getInstance("SHA-384");
    }

    public boolean checkName(String userName){
        try{
            PreparedStatement check = connection.prepareStatement(Statements.CHECK_NAME);
            check.setString(1, userName);
            ResultSet resultSet = check.executeQuery();
            return resultSet.next();
        } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
        }
    }

    public void registerUser(){
        try {
            PreparedStatement register = connection.prepareStatement(Statements.ADD_USER);
            register.setString(1, name);
            register.setBytes(2, messageDigest.digest(password.getBytes(StandardCharsets.UTF_8)));
            register.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean checkUser(String arg){
        try {
            PreparedStatement check = connection.prepareStatement(Statements.CHECK_USER);
            check.setString(1, name);
            check.setBytes(2, messageDigest.digest(arg.getBytes(StandardCharsets.UTF_8)));
            ResultSet result = check.executeQuery();
            return result.next();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public void updateCollection(){
        try {
            PreparedStatement updateCollection = connection.prepareStatement(Statements.GET_WORKERS);
            collectionManager.updateCollection(updateCollection.executeQuery());
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
    public boolean addWorker(String userName, Worker worker){
        try {
            PreparedStatement addWorker = connection.prepareStatement(Statements.ADD_WORKER);
            addWorker.setLong(1, worker.getId());
            addWorker.setString(2, worker.getName());
            addWorker.setLong(3, worker.getCoordinates().getCoordinateX());
            addWorker.setInt(4, worker.getCoordinates().getCoordinateY());
            addWorker.setDouble(5, worker.getSalary());
            Timestamp startDate = Timestamp.from(Instant.from(worker.getStartDate()));
            addWorker.setTimestamp(6, startDate);
            Timestamp endDate = Timestamp.from(Instant.from(worker.getEndDate()));
            addWorker.setTimestamp(7, endDate);
            addWorker.setString(8, worker.getPosition().toString());
            addWorker.setLong(9, worker.getPerson().getHeight());
            addWorker.setInt(10, worker.getPerson().getWeight());
            addWorker.setString(11, userName);
            addWorker.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean clear(String userName){
        try {
            PreparedStatement clear = connection.prepareStatement(Statements.CLEAR_WORKERS);
            clear.setString(1, userName);
            clear.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public String removeById(String userName, long id){
        try {
            PreparedStatement getById = connection.prepareStatement(Statements.GET_BY_ID);
            getById.setLong(1, id);
            ResultSet worker = getById.executeQuery();
            if (worker.next()){
                if (worker.getString("owner").equals(userName)){
                    PreparedStatement remove = connection.prepareStatement(Statements.DELETE_BY_ID);
                    remove.setLong(1, id);
                    remove.executeUpdate();
                } else return "permission";
            } else return "noId";
        } catch (SQLException exception) {
            exception.printStackTrace();
            return "SQL";
        }
        return "good";
    }

    public String update(String userName, Worker worker, Long id){
        try {
            PreparedStatement getById = connection.prepareStatement(Statements.GET_BY_ID);
            getById.setLong(1, id);
            ResultSet result = getById.executeQuery();
            if (result.next()){
                if (result.getString("owner").equals(userName)){
                    PreparedStatement update = connection.prepareStatement(Statements.UPDATE_WORKER);
                    update.setString(1, worker.getName());
                    update.setLong(2, worker.getCoordinates().getCoordinateX());
                    update.setInt(3, worker.getCoordinates().getCoordinateY());
                    update.setDouble(4, worker.getSalary());
                    Timestamp startDate = Timestamp.from(Instant.from(worker.getStartDate()));
                    update.setTimestamp(5, startDate);
                    Timestamp endDate = Timestamp.from(Instant.from(worker.getEndDate()));
                    update.setTimestamp(6, endDate);
                    update.setString(7, worker.getPosition().toString());
                    update.setLong(8, worker.getPerson().getHeight());
                    update.setInt(9, worker.getPerson().getWeight());
                    update.setString(10, userName);
                    update.executeUpdate();
                } else return "permission";
            } else return "noId";
        } catch (SQLException exception) {
            exception.printStackTrace();
            return "SQL";
        }
        return "good";
    }

    public Long getNewId(){
        try {
            PreparedStatement getId = connection.prepareStatement(Statements.INCREASE_ID);
            ResultSet resultSet = getId.executeQuery();
            if (resultSet.next()){
                return resultSet.getLong("nextval");
            }
            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

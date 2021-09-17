package utility.database;

import data.Coordinates;
import data.Person;
import data.Position;
import data.Worker;
import utility.CollectionManager;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.TimeZone;

public class DatabaseManager {
    private final Connection connection;
    private final MessageDigest messageDigest;
    private final CollectionManager collectionManager;
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

    public void registerUser(String name, String password){
        try {
            PreparedStatement register = connection.prepareStatement(Statements.ADD_USER);
            register.setString(1, name);
            register.setBytes(2, messageDigest.digest(password.getBytes(StandardCharsets.UTF_8)));
            register.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean checkUser(String userName, String password){
        try {
            PreparedStatement check = connection.prepareStatement(Statements.CHECK_USER);
            check.setString(1, userName);
            check.setBytes(2, messageDigest.digest(password.getBytes(StandardCharsets.UTF_8)));
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
            if (worker.getEndDate() != null) {
                Timestamp endDate = Timestamp.from(Instant.from(worker.getEndDate()));
                addWorker.setTimestamp(7, endDate);
            } else addWorker.setTimestamp(7, null);
            if (worker.getPosition() != null) {
                addWorker.setString(8, worker.getPosition().toString());
            } else addWorker.setString(8, null);
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
            Statement updateId = connection.createStatement();
            updateId.executeUpdate("ALTER SEQUENCE IF EXISTS ids RESTART WITH " + collectionManager.getLastId());
            } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return true;
    }

    public DatabaseCommandResult removeById(String userName, long id){
        try {
            PreparedStatement getById = connection.prepareStatement(Statements.GET_BY_ID);
            getById.setLong(1, id);
            ResultSet worker = getById.executeQuery();
            if (worker.next()){
                if (worker.getString("owner").equals(userName)){
                    PreparedStatement remove = connection.prepareStatement(Statements.DELETE_BY_ID);
                    remove.setLong(1, id);
                    remove.executeUpdate();
                } else return DatabaseCommandResult.PERMISSION;
            } else return DatabaseCommandResult.NO_ID;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return DatabaseCommandResult.SQL;
        }
        return DatabaseCommandResult.GOOD;
    }
    public boolean getByIdAndOwner(String userName, Long id){
        try {
            PreparedStatement getByIdAndOwner = connection.prepareStatement(Statements.GET_BY_ID_AND_OWNER);
            getByIdAndOwner.setLong(1, id);
            getByIdAndOwner.setString(2, userName);
            ResultSet result = getByIdAndOwner.executeQuery();
            if (result.next()){
                return true;
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
        return false;
    }
    public DatabaseCommandResult update(String userName, Worker worker, Long id){
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
                } else return DatabaseCommandResult.PERMISSION;
            } else return DatabaseCommandResult.NO_ID;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return DatabaseCommandResult.SQL;
        }
        return DatabaseCommandResult.GOOD;
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
    public ArrayList<Worker> getByOwner(String userName){
        try {
            PreparedStatement getByOwner = connection.prepareStatement(Statements.GET_WORKERS_BY_OWNER);
            getByOwner.setString(1, userName);
            ResultSet result = getByOwner.executeQuery();
            ArrayList<Worker> resultCollection = new ArrayList<>();
            while (result.next()) {
                resultCollection.add(new Worker(result.getLong(1),
                        result.getString(2),
                        new Coordinates(result.getLong(3), result.getInt(4)),
                        result.getDate(5).toLocalDate().atStartOfDay(TimeZone.getDefault().toZoneId()),
                        result.getDouble(6),
                        ZonedDateTime.of(result.getTimestamp(7).toLocalDateTime(), TimeZone.getDefault().toZoneId()),
                        result.getTimestamp(8) != null ? ZonedDateTime.of(result.getTimestamp(8).toLocalDateTime(), TimeZone.getDefault().toZoneId()) : null,
                        Position.valueOf(result.getString(9)),
                        new Person(result.getLong(10), result.getInt(11))));
            }
            return resultCollection;
        } catch (SQLException exception) {
            exception.printStackTrace();
            return null;
        }
    }
}

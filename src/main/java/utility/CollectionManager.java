package utility;

import data.Coordinates;
import data.Person;
import data.Position;
import data.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is used to do all operations with collection
 */

public class CollectionManager {
    private LinkedList<Worker> collection = new LinkedList<>();
    private boolean ExeDone;
    private final ZonedDateTime InitTime = ZonedDateTime.now();

    public CollectionManager() {
    }
    //TODO ReadWriteLocks

    public void updateCollection(ResultSet resultSet) throws SQLException {
        while (resultSet.next()){
            add(new Worker(resultSet.getLong(1), resultSet.getString(2), new Coordinates(resultSet.getLong(3), resultSet.getInt(4)), ZonedDateTime.of(LocalDateTime.from(resultSet.getDate(5).toLocalDate()), TimeZone.getDefault().toZoneId()), resultSet.getDouble(6), ZonedDateTime.of(resultSet.getTimestamp(7).toLocalDateTime(), TimeZone.getDefault().toZoneId()), ZonedDateTime.of(resultSet.getTimestamp(8) != null ? resultSet.getTimestamp(8).toLocalDateTime() : null, TimeZone.getDefault().toZoneId()), Position.valueOf(resultSet.getString(9)), new Person(resultSet.getLong(10), resultSet.getInt(11))));
        }
    }

    /**
     * Adds new worker to the collection
     *
     * @param worker worker instance to be add
     */
    public void add(Worker worker) {
        ExeDone = true;
        collection.add(worker);
    }

    public boolean addIfMax(Worker worker) {
        try {
            ExeDone = true;
            Worker max;
            max = collection.getFirst();
            for (Worker worker1 : collection) {
                if (max.compareTo(worker1) > 0) {
                    max = worker1;
                }
            }
            if (worker.compareTo(max) > 0) {
                collection.add(worker);
                return true;
            } else return false;
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }

    public Long countLessThanStartDate(String arg) {
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        ArrayList<Worker> tempCollection;
        Long result;
        if (collection.size() > 0) {
            ZonedDateTime finalTempTime = tempTime;
            tempCollection = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) < 0).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            result = (long) tempCollection.size();
        } else {
            result = null;
        }
        return result;
    }

    public LinkedList<Worker> filterGreaterThanStartDate(String arg) {
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        LinkedList<Worker> result = null;
        if (collection.size() > 0) {
            ZonedDateTime finalTempTime = tempTime;
            result = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) > 0).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
            result.sort(Comparator.comparing(Worker::getName));
            return result;
        }
        return result;
    }

    public String groupCountingByPosition() {
        String result = "";
        Map<String, Long> answer = collection.stream().collect(Collectors.groupingBy(worker -> {
            if (worker.getPosition() != null){
                return worker.getPosition().toString();
            } else return "";
        } , Collectors.counting()));
        for (Map.Entry<String, Long> entry : answer.entrySet()) {
            if (!entry.getKey().equals("")) {
                result += (entry.getKey() + " - " + entry.getValue() + "\n");
            } else result += ("No position - " + entry.getValue() + "\n");
        }
        return result;
    }

    public void removeById(Long id) {
        collection.removeIf(worker -> worker.getId() == id);
    }

//    public boolean removeGreater(Worker worker) {
//        boolean temp = false;
//        for (Worker worker1 : collection) {
//            if (worker.compareTo(worker1) > 0) {
//                collection.remove(worker1);
//                temp = true;
//            }
//        }
//        return temp;
//    }

    public ArrayList<Worker> getGreater(Worker worker){
        ArrayList<Worker> result = new ArrayList<>();
        for (Worker worker1 : collection) {
            if (worker.compareTo(worker1) > 0) {
                result.add(worker1);
            }
        }
        return result;
    }

//    public boolean removeLower(Worker worker) {
//        boolean temp = false;
//        for (Worker worker1 : collection) {
//            if (worker.compareTo(worker1) > 0) {
//                collection.remove(worker1);
//                temp = true;
//            }
//        }
//        return temp;
//    }

    public ArrayList<Worker> getLower(Worker worker){
        ArrayList<Worker> result = new ArrayList<>();
        for (Worker worker1 : collection) {
            if (worker.compareTo(worker1) < 0) {
                result.add(worker1);
            }
        }
        return result;
    }


    public LinkedList<Worker> show() {
        return collection.stream().sorted(Comparator.comparing(Worker::getName)).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    public void update(Long id, Worker worker) {
        collection.forEach(worker1 -> {
            if (worker1.getId() == id) {
                worker1.setName(worker.getName());
                worker1.setCoordinates(worker.getCoordinates());
                worker1.setSalary(worker.getSalary());
                worker1.setStartDate(worker.getStartDate());
                worker1.setEndDate(worker.getEndDate());
                worker1.setPosition(worker.getPosition());
                worker1.setPerson(worker.getPerson());
            }
        });
    }


    /**
     * Remove all elements from collection
     */
    public void clear() {
        ExeDone = true;
        collection.clear();
    }

    /**
     * @return true if collection have unsaved changes
     */
    public boolean exeDone() {
        return ExeDone;
    }

    /**
     * @return string array with information about collection
     */
    public String getInfo() {
        String Type = "Type: Collection of worker's type objects\n";
        String Init = "Initialization time: " + InitTime.toString() + "\n";
        String Size = "Number of elements: " + collection.size()+ "\n";
        String State;
        if (exeDone()) {
            State = "Collection has been modified.";
        } else {
            State = "Collection hasn't been modified yet.";
        }
        return Type + Init + Size + State   ;
    }

    /**
     * @return copy collection with workers
     */
    public LinkedList<Worker> getCollection() {
        return collection;
    }

    public void setCollection(LinkedList<Worker> collection) {
        this.collection = collection;
    }

    /**
     * Load collection from indicated file
     *
     * @param collectionFromFile external collection of worker instances
     */
    public void load(Collection<Worker> collectionFromFile) {
        collection.addAll(collectionFromFile);
    }

    public Long getLastId(){
        Long lastId = 0L;
        for (Worker w : collection){
            if (w.getId() > lastId){
                lastId = w.getId();
            }
        }
        return lastId;
    }

}
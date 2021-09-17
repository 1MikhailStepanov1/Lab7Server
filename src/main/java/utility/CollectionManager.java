package utility;

import data.Coordinates;
import data.Person;
import data.Position;
import data.Worker;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;


/**
 * This class is used to do all operations with collection
 */

public class CollectionManager {
    private LinkedList<Worker> collection = new LinkedList<>();
    private boolean ExeDone;
    private final ZonedDateTime InitTime = ZonedDateTime.now();
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CollectionManager() {
    }

    public void updateCollection(ResultSet resultSet) throws SQLException {
        while (resultSet.next()) {
            add(new Worker(resultSet.getLong(1),
                    resultSet.getString(2),
                    new Coordinates(resultSet.getLong(3), resultSet.getInt(4)),
                    resultSet.getDate(5).toLocalDate().atStartOfDay(TimeZone.getDefault().toZoneId()),
                    resultSet.getDouble(6),
                    ZonedDateTime.of(resultSet.getTimestamp(7).toLocalDateTime(), TimeZone.getDefault().toZoneId()),
                    resultSet.getTimestamp(8) != null ? ZonedDateTime.of(resultSet.getTimestamp(8).toLocalDateTime(), TimeZone.getDefault().toZoneId()) : null,
                    Position.valueOf(resultSet.getString(9)),
                    new Person(resultSet.getLong(10), resultSet.getInt(11))));
        }
    }

    /**
     * Adds new worker to the collection
     *
     * @param worker worker instance to be add
     */
    public void add(Worker worker) {
        lock.writeLock().lock();
        try {
            ExeDone = true;
            collection.add(worker);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public boolean addIfMax(Worker worker) {
        lock.writeLock().lock();
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
        } finally {
            lock.writeLock().unlock();
        }
        return false;
    }

    public Long countLessThanStartDate(String arg) {
        lock.readLock().lock();
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        ArrayList<Worker> tempCollection;
        Long result;
        try {
            if (collection.size() > 0) {
                ZonedDateTime finalTempTime = tempTime;
                tempCollection = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) < 0).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
                result = (long) tempCollection.size();
            } else {
                result = null;
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    public LinkedList<Worker> filterGreaterThanStartDate(String arg) {
        lock.readLock().lock();
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        LinkedList<Worker> result = null;
        try {
            if (collection.size() > 0) {
                ZonedDateTime finalTempTime = tempTime;
                result = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) > 0).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
                result.sort(Comparator.comparing(Worker::getName));
                return result;
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    public String groupCountingByPosition() {
        String result = "";
        lock.readLock().lock();
        try {
            Map<String, Long> answer = collection.stream().collect(Collectors.groupingBy(worker -> {
                if (worker.getPosition() != null) {
                    return worker.getPosition().toString();
                } else return "";
            }, Collectors.counting()));
            for (Map.Entry<String, Long> entry : answer.entrySet()) {
                if (!entry.getKey().equals("")) {
                    result += (entry.getKey() + " - " + entry.getValue() + "\n");
                } else result += ("No position - " + entry.getValue() + "\n");
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }

    public void removeById(Long id) {
        lock.writeLock().lock();
        try {
            collection.removeIf(worker -> worker.getId() == id);
        } finally{
            lock.writeLock().unlock();
        }
    }


    public ArrayList<Worker> getGreater(Worker worker) {
        lock.readLock().lock();
        ArrayList<Worker> result = new ArrayList<>();
        try {
            for (Worker worker1 : collection) {
                if (worker.compareTo(worker1) > 0) {
                    result.add(worker1);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }


    public ArrayList<Worker> getLower(Worker worker) {
        lock.readLock().lock();
        ArrayList<Worker> result = new ArrayList<>();
        try {
            for (Worker worker1 : collection) {
                if (worker.compareTo(worker1) < 0) {
                    result.add(worker1);
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }


    public LinkedList<Worker> show() {
        lock.readLock().lock();
        LinkedList<Worker> temp;
        try {
            temp = collection.stream().sorted(Comparator.comparing(Worker::getName)).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
        }finally {
            lock.readLock().unlock();
        }
        return temp;
    }

    public void update(Long id, Worker worker) {
        lock.writeLock().lock();
        try {
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
        } finally {
            lock.writeLock().unlock();
        }
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
        lock.readLock().lock();
        String Type = "";
        String Init = "";
        String Size = "";
        String State = "";
        try {
            Type = "Type: Collection of worker's type objects\n";
            Init = "Initialization time: " + InitTime.toString() + "\n";
            Size = "Number of elements: " + collection.size() + "\n";
            if (exeDone()) {
                State = "Collection has been modified.";
            } else {
                State = "Collection hasn't been modified yet.";
            }
        } finally {
            lock.readLock().unlock();
        }
        return Type + Init + Size + State;
    }

    /**
     * @return copy collection with workers
     */
    public LinkedList<Worker> getCollection() {
        LinkedList<Worker> temp = null;
        lock.readLock().lock();
        try {
            temp = collection;
        } finally {
            lock.readLock().unlock();
        }
        return temp;
    }

    public void setCollection(LinkedList<Worker> collection) {
        this.collection = collection;
    }

    public long getLastId() {
        lock.readLock().lock();
        long lastId = 1;
        try {
            for (Worker w : collection) {
                if (w.getId() > lastId) {
                    lastId = w.getId();
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return lastId;
    }

}
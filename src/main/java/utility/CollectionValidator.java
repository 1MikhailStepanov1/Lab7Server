package utility;

import data.Position;
import data.Worker;

import java.util.Arrays;
import java.util.LinkedList;

public class CollectionValidator {
    private final CollectionManager collectionManager;

    public CollectionValidator(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }

    public boolean validateObject(LinkedList<Worker> collection, Worker worker) {
        boolean tempName;
        boolean tempCoordinates;
        boolean tempSalary;
        boolean tempStartDate;
        boolean tempPosition;
        boolean tempHeight;
        boolean tempWeight;
        if (!collection.isEmpty()) {
            for (Worker w : collection){
                if (w.getId() == worker.getId()){
                    return false;
                }
            }
            if (!worker.getName().equals("") && worker.getName() != null) {
                tempName = true;
            } else tempName = false;
            if (worker.getCoordinates().getCoordinateX() <= 768) {
                if (worker.getCoordinates().getCoordinateY() != null) {
                    tempCoordinates = true;
                } else tempCoordinates = false;
            } else tempCoordinates = false;
            if (worker.getSalary() > 0) {
                tempSalary = true;
            } else tempSalary = false;
            if (worker.getStartDate() != null) {
                tempStartDate = true;
            } else tempStartDate = false;
            tempPosition = checkExistPosition(String.valueOf(worker.getPosition()));
            if (worker.getPerson().getHeight() > 0) {
                tempHeight = true;
            } else tempHeight = false;
            if (worker.getPerson().getWeight() > 0) {
                tempWeight = true;
            } else tempWeight = false;
            return tempName && tempCoordinates && tempSalary && tempStartDate && tempPosition && tempHeight && tempWeight;
        }
        return true;
    }


    private boolean checkExistPosition(String position) {
        return Arrays.stream(Position.values()).anyMatch(x -> x.name().equals(position));
    }

    public boolean checkExistId(Long id) {
        for (Worker worker : collectionManager.getCollection()) {
            if (worker.getId() == id) {
                return true;
            }
        }
        return false;
    }
}

package data;

import java.io.Serializable;
import java.time.ZonedDateTime;
public class Worker implements Comparable<Worker>, Serializable {
    private static final long serialVersionUID = 32L;
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private double salary; //Значение поля должно быть больше 0
    private java.time.ZonedDateTime startDate; //Поле не может быть null
    private java.time.ZonedDateTime endDate; //Поле может быть null
    private Position position; //Поле может быть null
    private Person person; //Поле может быть null

    public Worker(Long id, String name, Coordinates coordinates, ZonedDateTime creationDate, double salary, ZonedDateTime startDate, ZonedDateTime endDate, Position position, Person person) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.person = person;
    }
    public Worker(){

    }

    public double getValue() {
        return salary + position.ordinal();
    }

    @Override
    public int compareTo(Worker w) {
        if (this.getValue() > w.getValue()) {
            return -1;
        }
        if (this.getValue() < w.getValue()) {
            return 1;
        }
        return 0;
    }


    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public double getSalary() {
        return salary;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getEndDate() {
        return endDate;
    }

    public Position getPosition() {
        return position;
    }

    public Person getPerson() {
        return person;
    }

    @Override
    public String toString() {
        String info = "";
        info += "Worker №" + id;
        info += "(added " + creationDate + ")";
        info += "\nName: " + name;
        info += "\nLocation: " + coordinates;
        info += "\nSalary: " + salary;
        info += "\nStart Date: " + startDate;
        info += "\nEnd Date: " + endDate;
        info += "\nPosition: " + position;
        info += "\nPerson: " + person;
        return info;
    }

    public void setId(Long id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void setStartDate(ZonedDateTime startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(ZonedDateTime endDate) {
        this.endDate = endDate;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}




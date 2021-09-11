package data;


import java.io.Serializable;

public class Person implements Serializable {
    private static final long serialVersionUID = 32L;
    private Long height; //Поле может быть null, Значение поля должно быть больше 0
    private Integer weight; //Поле может быть null, Значение поля должно быть больше 0

    public Person(Long height, Integer weight) {
        this.height = height;
        this.weight = weight;
    }

    public void setHeight(Long height){
        this.height = height;
    }

    public void setWeight(Integer weight){
        this.weight = weight;
    }
    public Long getHeight() {
        return height;
    }

    public Integer getWeight() {
        return weight;
    }
}

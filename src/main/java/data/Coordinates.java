package data;


import java.io.Serializable;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 32L;
    private long x; //Максимальное значение поля: 768
    private Integer y; //Поле не может быть null

    public Coordinates(long x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public void setCoordinateX(long x){
        this.x = x;
    }

    public void setCoordinateY(Integer y){
        this.y = y;
    }
    public long getCoordinateX() {
        return x;
    }

    public Integer getCoordinateY() {
        return y;
    }

}

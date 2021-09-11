package utility.database;

public class Statements {
    public static final String CHECK_NAME = "SELECT * FROM 312709users WHERE login=?;";
    public static final String ADD_USER = "INSERT INTO 312709users (login, password) VALUES(?,?);";
    public static final String CHECK_USER = "SELECT * FROM 312709users WHERE login=? AND password=?;";
    public static final String CLEAR_WORKERS = "DELETE FROM 312709workers WHERE owner=?";
    public static final String INCREASE_ID = "SELECT nextval('ids');";
    public static final String GET_BY_ID = "SELECT * FROM 312709workers WHERE id = ?";
    public static final String DELETE_BY_ID = "DELETE * FROM 312709workers WHERE id = ?";
    public static final String ADD_WORKER = "INSERT INTO 312709workers (id, name, coordinateX, coordinateY, salary, startDate, endDate, pos, height, weight, owner) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_WORKER = "UPDATE 312709workers SET name=?, coordinateX=?, coordinateY=?, salary=?, startDate=?, endDate=?, pos=?, height=?, weight=? WHERE id=?";
    public static final String GET_WORKERS = "SELECT * FROM 312709workers";
}

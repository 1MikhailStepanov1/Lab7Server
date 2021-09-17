package utility.database;

public class Statements {
    public static final String CHECK_NAME = "SELECT * FROM users312709 WHERE login=?;";
    public static final String ADD_USER = "INSERT INTO users312709 (login, password) VALUES(?,?);";
    public static final String CHECK_USER = "SELECT * FROM users312709 WHERE login=? AND password=?;";
    public static final String CLEAR_WORKERS = "DELETE FROM workers312709 WHERE owner=?";
    public static final String INCREASE_ID = "SELECT nextval('ids');";
    public static final String GET_BY_ID = "SELECT * FROM workers312709 WHERE id = ?";
    public static final String GET_BY_ID_AND_OWNER = "SELECT * FROM workers312709 WHERE id = ? AND owner = ?";
    public static final String DELETE_BY_ID = "DELETE FROM workers312709 WHERE id = ?";
    public static final String ADD_WORKER = "INSERT INTO workers312709 (id, name, xCoordinate, yCoordinate, salary, startDate, endDate, pos, height, weight, owner) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
    public static final String UPDATE_WORKER = "UPDATE workers312709 SET name=?, xCoordinate=?, yCoordinate=?, salary=?, startDate=?, endDate=?, pos=?, height=?, weight=? WHERE id=?";
    public static final String GET_WORKERS = "SELECT * FROM workers312709";
    public static final String GET_WORKERS_BY_OWNER = "SELECT * FROM workers312709 WHERE owner = ?";
}

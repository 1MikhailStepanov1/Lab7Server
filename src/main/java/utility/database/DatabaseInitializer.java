package utility.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    private final Connection connection;
    public DatabaseInitializer(Connection connection){
        this.connection = connection;
    }

    public void initializeTable() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("CREATE SEQUENCE IF NOT EXISTS ids START 1");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS workers312709 (" +
                "id bigint PRIMARY KEY," +
                "name varchar (255) NOT NULL," +
                "coordinateX bigint NOT NULL CHECK(coordinateX < 769)," +
                "coordinateY int NOT NULL," +
                "creationDate date DEFAULT (current_date),"+
                "salary real NOT NULL CHECK (salary  > 0)," +
                "startDate timestamp with time zone NOT NULL," +
                "endDate timestamp with time zone," +
                "pos varchar(20)," +
                "height bigint NOT NULL CHECK(height > 0)," +
                "weight int NOT NULL CHECK(weight > 0),"+
                "owner varchar(255) NOT NULL"
        +")");
        statement.executeUpdate("CREATE TABLE IF NOT EXISTS users312709(" +
                "login varchar(255) PRIMARY KEY," +
                "password BYTEA DEFAULT (null)" +
                ")");
    }
}

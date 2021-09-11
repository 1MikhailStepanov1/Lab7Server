package utility.database;

import exceptions.NoDatabaseDataException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {
    public static Connection connection() throws SQLException, NoDatabaseDataException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        String login = System.getenv("DB_LOGIN");
        String password = System.getenv("DB_PASSWORD");
        String host = System.getenv("DB_HOST");
        if (login == null || password == null) {
            throw new NoDatabaseDataException();
        }
        if (host == null) {
            host = "jdbc:postgresql://pg:5432/studs";
        }
        return DriverManager.getConnection(host, login, password);
    }
}

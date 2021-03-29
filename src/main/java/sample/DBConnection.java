package sample;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection SQLConnection(){
        Connection sql = null;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String url = "jdbc:sqlserver://localhost;databaseName=Bibliotheque;user=LAPTOP-CKH6HE9G\\eric9;password=;integratedSecurity=true";
            sql = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sql;
    }
}

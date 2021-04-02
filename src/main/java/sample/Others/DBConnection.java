package sample.Others;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    /**
     * Connecte à la base de données SQL Server
     * @return connexion à la base de données SQL Server
     */
    public static Connection SQLConnection(){
        Connection sql = null;
        try {
            DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
            String url = "jdbc:sqlserver://localhost;databaseName=Bibliotheque;user=LAPTOP-KA8PJG45\\erwan;password=;integratedSecurity=true";
            sql = DriverManager.getConnection(url);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sql;
    }
}

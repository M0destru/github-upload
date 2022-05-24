package keywords.dictionary;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private String URL = "";
    private String username = "";
    private String password = "";

    public Database (String URL, String username, String password) {
        SaveConnection(URL, username, password);
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getURL() {
        return URL;
    }

    public Connection GetConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, username, password);
        }
        catch (SQLException ex) {
            System.out.println((ex.getMessage()));
        }
        return conn;
    }

    public void SaveConnection(String URL, String username, String password) {
        this.username = username;
        this.URL = URL;
        this.password = password;
    }
}

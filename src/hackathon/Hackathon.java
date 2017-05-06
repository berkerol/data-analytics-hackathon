package hackathon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;

public class Hackathon {
    
    private static String className = "com.mysql.jdbc.Driver";
    private static String dbName = "jdbc:mysql://localhost/hackathon";
    
    private static void insertStores() throws IOException, ClassNotFoundException, SQLException {
        Path path = Paths.get("C:\\Users\\Berk Erol\\Desktop\\Invent_Compec_HackathonData\\Stores.csv");
        ArrayList<String> file = (ArrayList<String>) Files.readAllLines(path);
        file.remove(0);
        Connection conn = null;
        Statement stmt = null;
        Class.forName(className);
        conn = DriverManager.getConnection(dbName, "root", "");
        stmt = conn.createStatement();
        String sql;
        for (String s : file) {
            String[] line = s.split("[|]");
            sql = "INSERT INTO store(ID, City) VALUES(" + line[0].substring(5) + ", '" + line[1] + "')";
            System.out.println(sql);
            //stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
    }
    
    private static void insertProducts() throws IOException, ClassNotFoundException, SQLException {
        Path path = Paths.get("C:\\Users\\Berk Erol\\Desktop\\Invent_Compec_HackathonData\\Products.csv");
        ArrayList<String> file = (ArrayList<String>) Files.readAllLines(path);
        file.remove(0);
        Connection conn = null;
        Statement stmt = null;
        Class.forName(className);
        conn = DriverManager.getConnection(dbName, "root", "");
        stmt = conn.createStatement();
        String sql;
        for (String s : file) {
            String[] line = s.split("[|]");
            sql = "INSERT INTO product(ID, PG, Price, Cost) VALUES(" + line[0].substring(7) + ", " + line[1].substring(2) + ", " + line[2] + ", " + line[3] + ")";
            System.out.println(sql);
            //stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
    }
    
    private static void printStores() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName(className);
        conn = DriverManager.getConnection(dbName, "root", "");
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT ID, City FROM store";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.print("ID: " + rs.getInt("ID"));
            System.out.print(", City: " + rs.getString("City"));
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }
    
    private static void printProducts() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Statement stmt = null;
        Class.forName(className);
        conn = DriverManager.getConnection(dbName, "root", "");
        stmt = conn.createStatement();
        String sql;
        sql = "SELECT ID, PG, Price, Cost FROM product";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            System.out.print("ID: " + rs.getInt("ID"));
            System.out.print(", PG: " + rs.getInt("PG"));
            System.out.print(", Price: " + rs.getDouble("Price"));
            System.out.print(", Cost: " + rs.getDouble("Cost"));
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        //insertProducts();
    }
}

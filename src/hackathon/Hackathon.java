package hackathon;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;

public class Hackathon {

    private static final String CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "jdbc:mysql://localhost/hackathon";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String PATH = "C:\\Users\\Berk Erol\\Desktop\\Invent_Compec_HackathonData\\";

    private static ArrayList<String> readFile(String filename) throws IOException {
        Path path = Paths.get(PATH + filename);
        ArrayList<String> file = (ArrayList<String>) Files.readAllLines(path);
        file.remove(0);
        return file;
    }

    private static void insertStores() throws IOException, ClassNotFoundException, SQLException {
        ArrayList<String> file = readFile("Stores.csv");
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        for (String s : file) {
            String[] line = s.split("[|]");
            String sql = "INSERT INTO store(ID, City) VALUES(" + line[0].substring(5) + ", '" + line[1] + "')";
            System.out.println(sql);
            //stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
    }

    private static void insertProducts() throws IOException, ClassNotFoundException, SQLException {
        ArrayList<String> file = readFile("Products.csv");
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        for (String s : file) {
            String[] line = s.split("[|]");
            String sql = "INSERT INTO product(ID, PG, Price, Cost) VALUES(" + line[0].substring(7) + ", " + line[1].substring(2) + ", " + line[2] + ", " + line[3] + ")";
            System.out.println(sql);
            //stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
    }

    private static void insertInventory() throws IOException, ClassNotFoundException, SQLException {
        ArrayList<String> file = readFile("InventoryPosition.csv");
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        for (String s : file) {
            String[] line = s.split("[|]"), dmy = line[0].split("\\.");
            String sql = "INSERT INTO inventory(Date, StoreID, ProductID, SalesQuantity, StoreStock, IncomingStock, SalesRevenue) "
                    + "VALUES('" + dmy[2] + "-" + dmy[1] + "-" + dmy[0] + "', " + line[1].substring(5) + ", " + line[2].substring(7) + ", " + line[3] + ", " + line[4] + ", " + line[5] + ", " + line[6] + ")";
            System.out.println(sql);
            //stmt.executeUpdate(sql);
        }
        stmt.close();
        conn.close();
    }

    private static void printStores() throws ClassNotFoundException, SQLException {
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        String sql = "SELECT ID, City FROM store";
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
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        String sql = "SELECT ID, PG, Price, Cost FROM product";
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

    private static void printInventory() throws ClassNotFoundException, SQLException {
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        Statement stmt = conn.createStatement();
        String sql = "SELECT Date, StoreID, ProductID, SalesQuantity, StoreStock, IncomingStock, SalesRevenue FROM inventory";
        ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            String[] ymd = rs.getDate("Date").toString().split("[-]");
            System.out.print("Date: " + ymd[2] + "." + ymd[1] + "." + ymd[0]);
            System.out.print(", StoreID: " + rs.getInt("StoreID"));
            System.out.print(", ProductID: " + rs.getInt("ProductID"));
            System.out.print(", SalesQuantity: " + rs.getInt("SalesQuantity"));
            System.out.print(", StoreStock: " + rs.getInt("StoreStock"));
            System.out.print(", IncomingStock: " + rs.getInt("IncomingStock"));
            System.out.print(", SalesRevenue: " + rs.getDouble("SalesRevenue"));
            System.out.println();
        }
        rs.close();
        stmt.close();
        conn.close();
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        //printInventory();
    }
}
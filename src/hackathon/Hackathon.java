package hackathon;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Hackathon {

    private static final String CLASS_NAME = "com.mysql.jdbc.Driver";
    private static final String DB_NAME = "jdbc:mysql://localhost/hackathon";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static final String PATH = "C:\\Users\\Berk Erol\\Desktop\\Invent_Compec_HackathonData\\";
    /** Stores and product ids for forecasting*/
    private static final int[][] FOREPRODUCTS = {{16, 205}, {8, 229}, {17, 262}, {4, 136}, {15, 269}, {18, 355}, {18, 351}, {10, 373}, {8, 182}, {11, 263}};

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
    
    private static void parseProducts() throws ClassNotFoundException, SQLException, IOException {
        
        FileWriter writer = new FileWriter(new File("products_parsed.txt"));
        
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        
        for(int i=1; i<=391; i++){
            String sql = "SELECT ProductID, SalesQuantity, SalesRevenue FROM inventory WHERE ProductID = ? AND SalesQuantity != 0";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, i + "");
            ResultSet rs = prep.executeQuery();
            int salesQuantityCounter = 0;
            double salesRevenueAcc = 0;
            
            while (rs.next()) {
                salesQuantityCounter += rs.getInt("SalesQuantity");
                salesRevenueAcc += rs.getInt("SalesRevenue");
            }
            
            writer.write(i + " ");
            writer.write((salesRevenueAcc / salesQuantityCounter) + "");
            writer.write("\n");
            
            rs.close();
        }
  
        writer.close();
        conn.close();
    }
    
    /**
     * The structure of the data file this creates:
     * [Day SumRevenue Price SumQuantity] 
     * @throws ClassNotFoundException
     * @throws SQLException
     * @throws IOException 
     */
    private static void parseProductGroups() throws ClassNotFoundException, SQLException, IOException {
        
        FileWriter writer = new FileWriter(new File("product_group5.txt"));
        
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        
        TreeMap<String, ArrayList<Double>> dateDict = new TreeMap<String, ArrayList<Double> >();
        
        for(int i=327; i<=391; i++){
            String sql = "SELECT SUM(SalesQuantity) AS SQuantity, SUM(SalesRevenue) AS SRevenue, Date, ProductID FROM inventory WHERE ProductID = ? GROUP BY Date";
            PreparedStatement prep = conn.prepareStatement(sql);
            prep.setString(1, i + "");
            ResultSet rs = prep.executeQuery();
            
            while (rs.next()) {
                ArrayList<Double> arr = new ArrayList<Double>();
                String date = rs.getString("Date");
                double sumQ = rs.getInt("SQuantity");
                double sumR = rs.getInt("SRevenue");
                arr.add(sumR);
                if(sumQ != 0)
                    arr.add(sumR/sumQ);
                else
                    arr.add(0.0);
                if(dateDict.keySet().contains(date)){
                    arr.add(dateDict.get(date).get(2) + sumQ);
                    dateDict.put(date, arr);   
                }
                else{
                    arr.add(sumQ);
                    dateDict.put(date, arr);   

                }
            }          
            rs.close();
        }
        
        for(Map.Entry<String, ArrayList<Double> > entry : dateDict.entrySet()){
                String key = entry.getKey();
                ArrayList<Double> value = entry.getValue();
                writer.write(dateToDay(key) + " " + value.get(0));
                writer.write(" " + value.get(1));
                writer.write(" " + value.get(2));
                writer.write("\n");
        }
        
        writer.close();
        conn.close();
    }
    
    private static void getDataOfaProduct(int storeID, int productID) throws IOException, ClassNotFoundException, SQLException{
        FileWriter writer = new FileWriter(new File("foreProducts/product_group_" + storeID + "_" + productID + ".txt"));
        
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        
        String sql = "SELECT ProductID, Date, (SalesRevenue/SalesQuantity) AS DailyPrice, SalesQuantity FROM `inventory` WHERE StoreID = ? AND ProductID = ? ORDER BY Date ASC ";
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setString(1, storeID + "");
        prep.setString(2, productID + "");
        ResultSet rs = prep.executeQuery();
        
        while(rs.next()){
            writer.write(rs.getString("Date") + " ");
            if(rs.getInt("SalesQuantity") == 0){
                writer.write(0 + " ");
            }
            else{
                writer.write(rs.getInt("SalesQuantity") + " ");
            }
            
            writer.write(rs.getFloat("DailyPrice") + "\n");
        
        }
        
        writer.close();
        rs.close();
    }
    
    private static void getDataOfaProduct(int productID) throws IOException, ClassNotFoundException, SQLException{
        FileWriter writer = new FileWriter(new File("foreProductsAllStores/product_group_" + productID + ".txt"));
        
        Class.forName(CLASS_NAME);
        Connection conn = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
        
        String sql = "SELECT ProductID, Date, (SalesRevenue/SalesQuantity) AS DailyPrice, SalesQuantity FROM `inventory` WHERE ProductID = ? ORDER BY Date ASC ";
        PreparedStatement prep = conn.prepareStatement(sql);
        prep.setString(1, productID + "");
        ResultSet rs = prep.executeQuery();
        
        while(rs.next()){
            writer.write(rs.getString("Date") + " ");
            if(rs.getInt("SalesQuantity") == 0){
                writer.write(0 + " ");
            }
            else{
                writer.write(rs.getInt("SalesQuantity") + " ");
            }
            
            writer.write(rs.getFloat("DailyPrice") + "\n");
        
        }
        
        writer.close();
        rs.close();
    }
    
    private static void generateForecastData() throws IOException, ClassNotFoundException, SQLException{
        for(int i=0; i<FOREPRODUCTS.length; i++){
                getDataOfaProduct(FOREPRODUCTS[i][0], FOREPRODUCTS[i][1]);
        }
    }
    
    private static void generateForecastData2() throws IOException, ClassNotFoundException, SQLException{
        for(int i=0; i<FOREPRODUCTS.length; i++){
                getDataOfaProduct(FOREPRODUCTS[i][1]);
        }
    }
    
    
    private static int dateToDay(String date){
        String[] arr = date.split("-");
        String year = arr[0];
        String month = arr[1];
        String day = arr[2];
        
        int days = 0;
        if(year.equals("2016")){
            days += 365;
        }
        else if(year.equals("2017")){
            days += 730;
        }
        
        days += (Integer.parseInt(month) - 1) * 30;
        days += (Integer.parseInt(day) - 1);
        
        return days;
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        //printStores();
        //parseProducts();
        parseProductGroups();
        //generateForecastData2();
    }
}
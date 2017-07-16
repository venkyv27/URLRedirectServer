package com.http.server;

import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class DatabaseURL {
    
  //JDBC driver and db url
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/test";
    static final String test_URL = "http://google.com";
    
    //DB credentials
    static final String userName = "guest";
    static final String password = "guestpassword";
    
    //
    private Connection conn = null;
    private Statement statement= null;
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private ResultSetMetaData rsmd = null;
    int autoIncId = 0;
    String inputURL = null;
    
    public Connection connectToDB() throws ClassNotFoundException, SQLException{
        Class.forName(JDBC_DRIVER);
        System.out.println("Connecting...");

        conn = DriverManager.getConnection(DB_URL, userName, password);
        System.out.println("Connected to DB successfully");
        return conn;
    }
    
    
    public void setMapping(String origURL, String shortURL, int lastAutoIncrId) throws ClassNotFoundException, SQLException{
        String URL = shortURL;
        int id = lastAutoIncrId;
        System.out.println("The id to be updated is " + id);       
        conn = this.connectToDB();
        //statement = conn.createStatement();
        //statement.executeUpdate("update url set shortURL = '"+shortURL+'" +  where id = '"+lastAutoIncrId+'" ");
        preparedStatement = conn.prepareStatement("Update url set shortURL = ? where id = ?");
        preparedStatement.setString(1, URL);
        preparedStatement.setInt(2, id);
        preparedStatement.executeUpdate();
    }
    
    public int insertIntoDB(String url) throws Exception{
        try {
            this.inputURL = url;
            conn = this.connectToDB();
            System.out.println("Executing a statement");
            statement = conn.createStatement();
            String sql = "insert into url (origURL) values ('"+inputURL+"')";//This use of single and double quotes is tricky. TODO - prepared statement instead.
            //rs = statement.executeQuery(sql);
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
             rs = statement.getGeneratedKeys();
            if(rs.next()){
                autoIncId = rs.getInt(1);
            }
            else{
                throw new Exception();
            }
            
        }
        
        catch(SQLException sqle){
            sqle.printStackTrace();
        }
        
        catch(Exception e){
            throw e;
        }
        
        finally{
            if(conn!=null){
                conn.close();
            }
        }
        
        return autoIncId;
    }
    
  public String getMappedURL(String shortURL){
      String origURL = null;
      System.out.println("ShortURL passed here is " + shortURL);
      try {
          
        Connection connection = this.connectToDB();
        PreparedStatement ps = connection.prepareStatement("select origURL from url where shortURL = ?");
        ps.setString(1, shortURL);
        ResultSet rs= ps.executeQuery();
        while(rs.next()){
            origURL = rs.getString(1);
            System.out.println("what's here " + origURL);
        }
    } catch (ClassNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (SQLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
      System.out.println("Original URL when extracting from DB here " + origURL);
      return origURL;
      
  }


}

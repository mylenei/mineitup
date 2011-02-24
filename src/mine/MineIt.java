/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;

/**
 * Definition: class responsible for data mining, specifically, connecting to the DB and many other DB operations. =)
 * @author wella
 */
public class MineIt {
    private ArrayList<String> synonymsOfKeyword = new ArrayList<String>(5);
    private ContentReader reader;
    private LinkedList<String> listResult;

    public MineIt() {
        reader = new ContentReader();
        listResult = new LinkedList<String>();
    }

    public ArrayList<String> getSynonymsOfKeyword() {
        return synonymsOfKeyword;
    }

    public void setSynonymsOfKeyword(ArrayList<String> synonymsOfKeyword) {
        this.synonymsOfKeyword = synonymsOfKeyword;
    }

    public void setSynonymsOfKeyword(String[] synonymsOfKeyword) {
        this.synonymsOfKeyword.addAll(Arrays.asList(synonymsOfKeyword));
    }

    public LinkedList<String> getListResult() {
        return listResult;
    }

    public void setListResult(LinkedList<String> listResult) {
        this.listResult = listResult;
    }

    public ContentReader getReader() {
        return reader;
    }

    public void setReader(ContentReader reader) {
        this.reader = reader;
    }

    /**
     * Store contents in DB if content is still null
     */
    private boolean storeContentsInDB(int id, String content) {
        boolean ok = false;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
            Statement statement = conn.createStatement();
            String sql = "UPDATE " + DBAccountInfo.TABLE_NAME + " SET extractedText = \'" + content + "\' WHERE id = " + id;
            statement.executeUpdate(sql);
            statement.close();
            conn.close();
            ok = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }

    /** 
     * extract the contents to be written
     */
    private boolean extract(int id, String path) {
        String content = "";
        boolean ok = false;
        if(path.endsWith(".doc") || path.endsWith(".docx")) {
            content = reader.readDocFile(path);
        }
        else if(path.endsWith(".xls") || path.endsWith(".xlsx")) {
            content = reader.readExcelFile(path);
        }
        else if(path.endsWith(".ppt") || path.endsWith(".pptx")) {
            content = reader.readPPTFile(path);
        }
        else if(path.endsWith(".pdf") && !path.startsWith("http")) {
            content = reader.readPDFFile(path);
        }
        else if(path.endsWith(".txt")) {
            content = reader.readTxtFile(path);
        }
        else if(path.startsWith("http")) {
            content = reader.readWebText(path);
        }
        if(!content.equals("")) {
            if(content.contains("'")){
                content = content.replaceAll("'", "`");
            }
            storeContentsInDB(id, content);
            ok = true;
        }
        return ok;
    }

    /**
     * extracting the path from the database
     * if arg is true, the db is updated/reloaded
     */
    public void populateDB(boolean refresh) {
        Connection con = null;
        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
          if(!con.isClosed()) {
              System.out.println("Successfully connected to MySQL server using TCP/IP...");
              String query = "SELECT * FROM " + DBAccountInfo.TABLE_NAME;
              Statement st = con.createStatement();                     //creates the java statement
              ResultSet rs = st.executeQuery(query);                    // execute the query, and get a java resultset
              while (rs.next())                                         // iterate through the java resultset
              {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                String content = rs.getString("extractedText");
                if(content == null) {
                    extract(id, path);
                }
                else {
                    //check if equal ba sila, if not, update the value of extractedtext
                }
              }
              st.close();
              con.close();
            }
        }
        catch (SQLException s){
            System.out.println("SQL statement is not executed!");
        }
        catch (Exception e){
          e.printStackTrace();
        }
    }

    /**
     * inserts the path into the DB. use in adding datasource.
     */
    public boolean insertPath(String path) {
        boolean ok = false;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
            Statement statement = conn.createStatement();
            String sql = "INSERT INTO " + DBAccountInfo.TABLE_NAME + "(path) VALUES('" + path + "')";
            statement.executeUpdate(sql);
            statement.close();
            conn.close();
            ok = true;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return ok;
    }
}
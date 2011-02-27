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
     * extract the contents to be written
     */
    private String extract(String path) {
        String content = "";
        
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
        else if(path.endsWith(".odt") || path.endsWith(".odp") || path.endsWith(".ods")) {
            content = reader.readODFFile(path);
        }
        else if(path.endsWith(".txt")) {
            content = reader.readTxtFile(path);
        }
        else if(path.startsWith("http")) {
            content = reader.readWebText(path);
        }        
        return content;
    }

    /**
     * Performs select query
     * @param sql statement
     * @return ResultSet
     */
    public ResultSet selectQuery(String sql) {
        Connection conn = null;
        ResultSet rs = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(sql);
            statement.close();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Performs update (or insert) query
     * @param sql statement
     * @return returns the number of rows being affected by the update. -1 if update occurs
     */
    public int updateQuery(String sql) {
        Connection conn = null;
        int res = -1;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
            Statement statement = conn.createStatement();
            res = statement.executeUpdate(sql);
            statement.close();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Adds entry to the database
     * @param path chosen by the user
     */
    public int addDataSource(String path) {
        String content = this.extract(path);
        content = content.replaceAll("'", "`");
        String sql = "INSERT INTO " + DBAccountInfo.TABLE_NAME + "(path, content) VALUES('" + path + "','" + content + "')";
        int res = this.updateQuery(sql);
        return res;
    }

    /**
     * Refreshes/reloads/updates the content in the DB
     * @return returns number of rows updated. -1 if update fails.
     */
    private int reload(int id, String content) {
        int res = -1;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + DBAccountInfo.DB_NAME, DBAccountInfo.USER_NAME, DBAccountInfo.PASSWORD);
            Statement statement = conn.createStatement();
            String sql = "UPDATE " + DBAccountInfo.TABLE_NAME + " SET content = \'" + content + "\' WHERE id = " + id;
            res = statement.executeUpdate(sql);
            statement.close();
            conn.close();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * Iterates through the rows of the DB and updates the content
     * @return returns the number of rows being updated
     */
    public int reloadContents() throws SQLException {
        int res = 0;
        String select = "SELECT * FROM " + DBAccountInfo.TABLE_NAME;
        String content = "";
        ResultSet rs = this.selectQuery(select);
        while(rs.next()) {
            content = this.extract(rs.getString("path"));
            this.reload(rs.getInt("id"), content);
            res++;
        }
        return res;
    }

    /**
     * Counts the number of entries in the DB
     */
    private int countDBEntry() throws SQLException {
        int res = 0;
        String sql = "SELECT count(*) from " + DBAccountInfo.TABLE_NAME;
        ResultSet rs = this.selectQuery(sql);
        res = rs.getInt(1);
        return res;
    }
}
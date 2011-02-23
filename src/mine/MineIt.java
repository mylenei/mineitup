/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ArrayList;
import rita.wordnet.*;

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

    private boolean storeContentsInDB(int id, String content) {
        boolean ok = false;
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
            Statement statement = conn.createStatement();
            String sql = "UPDATE datasources SET extractedText = \'" + content + "\' WHERE id = " + id;
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

    //extract the contents to be written
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
        else if(path.startsWith("http")) {
            content = reader.readWebText(path);
        }
        if(!content.equals("")) {
            ok = true;
        }
        return ok;
    }

    /*
     * extracting the path from the database
     * if arg is true, the db is updated/reloaded
     */
    public void populateDB(boolean refresh) {
        Connection con = null;
        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
          if(!con.isClosed()) {
              System.out.println("Successfully connected to MySQL server using TCP/IP...");
              String query = "SELECT * FROM datasources";
              Statement st = con.createStatement();                     //creates the java statement
              ResultSet rs = st.executeQuery(query);                    // execute the query, and get a java resultset
              while (rs.next())                                         // iterate through the java resultset
              {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                String content = rs.getString("extractedText");
                content = content.replaceAll("'", "`");
                if(content == null || refresh) {
                    if(extract(id, path)) {
                        storeContentsInDB(id, content);
                    }
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
}
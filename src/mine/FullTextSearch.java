/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author wella
 */
public class FullTextSearch {
    private ArrayList<String> extractedTexts = new ArrayList<String>(10);
    private LinkedList<String> listResult;
    private ContentReader reader;

    public FullTextSearch() {
        reader = new ContentReader();
    }

    public ContentReader getReader() {
        return reader;
    }

    public void setReader(ContentReader reader) {
        this.reader = reader;
    }

    public void search(String keyword) {
        Connection con = null;
        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
          if(!con.isClosed()) {
              System.out.println("Successfully connected to MySQL server using TCP/IP...");
              String query = "SELECT * FROM sourcePath";
              Statement st = con.createStatement();             //creates the java statement
              ResultSet rs = st.executeQuery(query);            // execute the query, and get a java resultset
              while (rs.next())                                 // iterate through the java resultset
              {
                String path = rs.getString("path");
                this.extract(path);
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

    private void extract(String path) {
        String content;
        if(path.endsWith(".doc") || path.endsWith(".docx")) {
            content = reader.readDocFile(path);
            listResult.add(path);
            extractedTexts.add(content);
        }
        else if(path.endsWith(".xls") || path.endsWith(".xlsx")) {
            content = reader.readExcelFile(path);
            listResult.add(path);
            extractedTexts.add(content);
        }
        else if(path.endsWith(".ppt") || path.endsWith(".pptx")) {
            content = reader.readPPTFile(path);
            listResult.add(path);
            extractedTexts.add(content);
        }
        else if(path.endsWith(".pdf") && !path.startsWith("http")) {
            content = reader.readPDFFile(path);
            listResult.add(path);
            extractedTexts.add(content);
        }
        else if(path.startsWith("http")) {
            content = reader.readWebText(path);
            listResult.add(path);
            extractedTexts.add(content);
        }
    }
}

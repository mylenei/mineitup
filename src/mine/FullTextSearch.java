/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.LinkedHashMap;

/**
 *
 * @author wella
 */
public class FullTextSearch {
    private LinkedHashMap contents;
    private ContentReader reader;

    public FullTextSearch() {
        contents = new LinkedHashMap();
        reader = new ContentReader();
    }

    public LinkedHashMap getContents() {
        return contents;
    }

    public void setContents(LinkedHashMap contents) {
        this.contents = contents;
    }

    public ContentReader getReader() {
        return reader;
    }

    public void setReader(ContentReader reader) {
        this.reader = reader;
    }

    //returns the number of search results
    public int search(String keyword) {
        int numOfResults = 0;
        Connection con = null;
        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
          if(!con.isClosed()) {
              String query = "SELECT id,extractedText, MATCH(path,extractedText) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE) AS SCORE "
                      + "FROM datasources WHERE MATCH(path,extractedText) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE)";
              Statement st = con.createStatement();                     //creates the java statement
              ResultSet rs = st.executeQuery(query);                    // execute the query, and get a java resultset
              while (rs.next())                                         // iterate through the java resultset
              {
                int id = rs.getInt("id");
                String path = rs.getString("extractedText");
                System.out.println(id);
                store(path);
                numOfResults++;
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
        return numOfResults;
    }

    //returns true if content that is extracted is stored in the LinkedHashMap, false otherwise
    public boolean store(String path) {
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
            contents.put(path, contents);
            ok = true;
        }
        return ok;
    }
}

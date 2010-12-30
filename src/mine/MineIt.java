/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.io.*;
import java.sql.*;

/**
 *
 * @author wella
 */
public class MineIt {
    private String keyword;
    private ContentReader reader;
    
    public MineIt() {
        keyword = "";
        reader = new ContentReader();
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public ContentReader getReader() {
        return reader;
    }

    public void setReader(ContentReader reader) {
        this.reader = reader;
    }

    public void searchKeywordOccurence(String keyword) {
        Connection con = null;
        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
          if(!con.isClosed()) {
              System.out.println("Successfully connected to MySQL server using TCP/IP...");
              // our SQL SELECT query.
              // if you only need a few columns, specify them by name instead of using "*"
              String query = "SELECT * FROM sources";

              // create the java statement
              Statement st = con.createStatement();

              // execute the query, and get a java resultset
              ResultSet rs = st.executeQuery(query);

              // iterate through the java resultset
              while (rs.next())
              {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                // print the results
                System.out.format("%s, %s\n", id, path);
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

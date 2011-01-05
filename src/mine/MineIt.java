/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 *
 * @author wella
 */
public class MineIt {
    //private String keyword;
    private ContentReader reader;
    private LinkedList<String> listResult;

    public MineIt() {
      //  keyword = "";
        reader = new ContentReader();
        listResult = new LinkedList<String>();
    }

    public LinkedList<String> getListResult() {
        return listResult;
    }

    public void setListResult(LinkedList<String> listResult) {
        this.listResult = listResult;
    }

//    public String getKeyword() {
//        return keyword;
//    }
//
//    public void setKeyword(String keyword) {
//        this.keyword = keyword;
//    }

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
              String query = "SELECT * FROM sources";
              Statement st = con.createStatement(); //creates the java statement
              ResultSet rs = st.executeQuery(query); // execute the query, and get a java resultset
              while (rs.next()) // iterate through the java resultset
              {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                processKeywordInFile(keyword, path); //if mo-return siya ug true, store the id somewhere for data mining algo to be used
                //System.out.format("%s, %s\n", id, path);
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
        displayListResult(listResult);
    }

    //returns false if the file given by path does not contain keyword, true otherwise
    private boolean processKeywordInFile(String keyword, String path) {
        String content;
        boolean ok = false;
        if(path.endsWith(".doc") || path.endsWith(".docx")) {
            content = reader.readDocFile(path);
            //System.out.println(content);
            if(content.toLowerCase().contains(keyword)) {
                listResult.add(path);
                System.out.println(content);
                ok = true;
            }
        }
        else if(path.endsWith(".xls") || path.endsWith(".xlsx")) {
            content = reader.readExcelFile(path);
            //System.out.println(content);
            if(content.toLowerCase().contains(keyword)) {
                listResult.add(path);
                System.out.println(content);
                ok = true;
            }
        }
        else if(path.endsWith(".ppt") || path.endsWith(".pptx")) {
            content = reader.readPPTFile(path);
            //System.out.println(content);
            if(content.toLowerCase().contains(keyword)) {
                listResult.add(path);
                System.out.println(content);
                ok = true;
            }
        }
        else if(path.endsWith(".pdf")) {
            content = reader.readPDFFile(path);
            //System.out.println(content);
            if(content.toLowerCase().contains(keyword)) {
                listResult.add(path);
                System.out.println(content);
                ok = true;
            }
        }
        
        return ok;
    }

    //displays all the elements in the listResult
    public void displayListResult(LinkedList<String> listResult) {
        String list = "";
        System.out.println("Search Results");
        ListIterator<String> iterator = listResult.listIterator();
        while(iterator.hasNext()) {
            System.out.println("oh yeah!");
            list += iterator.next() + "\n";
            System.out.println(list);
        }
        String res = listResult.size() + " Search Result(s)";
        javax.swing.JOptionPane.showMessageDialog(null, list, res, javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
}

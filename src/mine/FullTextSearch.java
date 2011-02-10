/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Hashtable;

/**
 *
 * @author wella
 */
public class FullTextSearch {
    private LinkedHashMap extractedTexts;
    private LinkedList contentList;
    private ContentReader reader;
    private double thresholdScore = 0.20;

    public FullTextSearch() {
        extractedTexts = new LinkedHashMap();
        contentList = new LinkedList<String>();
        reader = new ContentReader();
    }

    public double getThresholdScore() {
        return thresholdScore;
    }

    public void setThresholdScore(double thresholdScore) {
        this.thresholdScore = thresholdScore;
    }

    public LinkedHashMap getExtractedTexts() {
        return extractedTexts;
    }

    public void setExtractedTexts(LinkedHashMap extractedTexts) {
        this.extractedTexts = extractedTexts;
    }

    public LinkedList getContentList() {
        return contentList;
    }

    public void setContentList(LinkedList contentList) {
        this.contentList = contentList;
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
        extractedTexts.clear();
        Connection con = null;
        rita.RiStemmer stem = new rita.RiStemmer(null, rita.RiStemmer.PORTER_STEMMER);

        try {
          Class.forName("com.mysql.jdbc.Driver");
          con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mineitup","root","1234");
          if(!con.isClosed()) {
              String query = "SELECT id,path,extractedText, MATCH(path,extractedText) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE) AS SCORE "
                      + "FROM datasources WHERE MATCH(path,extractedText) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE)";
//                      + "UNION"
//                      + "SELECT id,path,extractedText, MATCH(path,extractedText) AGAINST"
//                      + "('character' IN NATURAL LANGUAGE MODE) AS SCORE "
//                      + "FROM datasources WHERE MATCH(path,extractedText) AGAINST"
//                      + "('character' IN NATURAL LANGUAGE MODE) UNION";
              Statement st = con.createStatement();                     //creates the java statement
              ResultSet rs = st.executeQuery(query);                     // execute the query, and get a java resultset
              while (rs.next())                                         // iterate through the java resultset
              {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                double score = rs.getDouble("SCORE");
                
                if(score > thresholdScore) {
                    String texts = rs.getString("extractedText");
                    extractedTexts.put(path,texts);
                    contentList.add(texts);
                }
                System.out.println(id);
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

    //stores the extracted text of the given path and returns true if storing is successful, false otherwise.
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
            extractedTexts.put(path,content);
            ok = true;
        }
        return ok;
    }

    //search also for the realted words or synonyms of the given keyword
    public Hashtable<String, String[]> getRelatedWords(String keyword) {
        Hashtable<String, String[]> dictionary = new Hashtable();
        rita.wordnet.RiWordnet wordnet = new rita.wordnet.RiWordnet(null);
        String[] words = keyword.split(" ");
        for(String s : words) {
            if(wordnet.exists(s)) {
                dictionary.put(s, wordnet.getAllSynonyms(keyword, wordnet.getBestPos(keyword))); //magbutang lang siguro ug para option sa user noh like max search or normal search. ang max search kay allsynonyms ang normal search kay allsynsets. :D
            }
            else{
                dictionary.put(s, null);
            }
        }
        return dictionary;
    }
}

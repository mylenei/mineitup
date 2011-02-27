/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * Definition: class that does the full searching of the occurrence of the given keyword. it does a lot more :D
 * This is implemented through MySQL's support for FTS
 * @author wella
 */
public class FullTextSearch {
    private LinkedHashMap extractedTexts;
    private LinkedList contentList;
    private ContentReader reader;
    private KeywordProcessor kprocess;
    private MineIt mine;
    private double thresholdScore = 0.20;

    public FullTextSearch() {
        extractedTexts = new LinkedHashMap();
        contentList = new LinkedList<String>();
        reader = new ContentReader();
        kprocess = new KeywordProcessor();
        mine = new MineIt();
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

    /**
     * Does a maximum search by searching the related words also
     */
    public int doMaxSearch(String keyword) throws SQLException {
        int numOfResults = 0;
        extractedTexts.clear();
        String query = "SELECT id,path,cotent, MATCH(path,content) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE) AS SCORE "
                      + "FROM " + DBAccountInfo.TABLE_NAME + " WHERE MATCH(path,content) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE)";
        ResultSet rs = mine.selectQuery(query);                   // execute the query, and get a java resultset
        while (rs.next())                                          // iterate through the java resultset
        {
            int id = rs.getInt("id");
            String path = rs.getString("path");
            double score = rs.getDouble("SCORE");
            if(score > thresholdScore) {
                String texts = rs.getString("content");
                extractedTexts.put(path,texts);
                contentList.add(texts);
            }
            System.out.println(id);
            numOfResults++;
        }
  
        return numOfResults;
    }

    /**
     * returns the number of search results
     */
    public int search(String keyword) throws SQLException {
        int numOfResults = 0;
        extractedTexts.clear();
        String query = "SELECT id,path,cotent, MATCH(path,content) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE) AS SCORE "
                      + "FROM " + DBAccountInfo.TABLE_NAME + " WHERE MATCH(path,content) AGAINST"
                      + "('" + keyword + "' IN NATURAL LANGUAGE MODE)";
        ResultSet rs = mine.selectQuery(query);                   // execute the query, and get a java resultset
        try {
            while (rs.next())                                          // iterate through the java resultset
            {
                int id = rs.getInt("id");
                String path = rs.getString("path");
                double score = rs.getDouble("SCORE");
                if(score > thresholdScore) {
                    String texts = rs.getString("content");
                    extractedTexts.put(path,texts);
                    contentList.add(texts);
                }
                System.out.println(id);
                numOfResults++;
            }
        } 
        catch(SQLException e) {e.printStackTrace();}
        catch(Exception e) {e.printStackTrace();}
        return numOfResults;
    }

    /**
     * stores the extracted text of the given path and
     * returns true if storing is successful, false otherwise.
     */
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
        else if(path.endsWith(".odt") || path.endsWith(".odp") || path.endsWith(".ods")) {
            content = reader.readODFFile(path);
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
            extractedTexts.put(path,content);
            ok = true;
        }
        return ok;
    }
}

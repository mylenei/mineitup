/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import java.io.*;

/**
 * Definition: Class that manipulates/accesses the database account info
 * @author wella
 */
public class DBAccountInfo {
    protected static String TABLE_NAME = "datasources";
    protected static String USER_NAME = "root";
    protected static String PASSWORD = "";
    protected static String DB_NAME = "mineitup";
    protected static String COL_NAME0 = "id";
    protected static String COL_NAME1 = "path";
    protected static String COL_NAME2 = "content";
 
    private String filename = "src/db_account_info.txt";

    /**
     * Reads the database account information from a file
     */
    private String readDbAccountInfo() {
        StringBuffer sb = new StringBuffer(100);
        try {
            FileReader fr = new FileReader(filename);
            int i = 0;
            while((i = fr.read()) != -1) {
                sb.append((char)i);
            }
        }
        catch(Exception e) {
            System.out.println(e);
        }
        return sb.toString();
    }

    /**
     * Sets the database account info.
     * It's fortunate that we know the format of the file. ha-ha
     */
    public void setDBInfo() {
        String info = readDbAccountInfo();
        String[] infos = info.split("\n");              //splits line by line
        for(int i = 0; i < infos.length; i++) {         //iterates through the lines
            String[] temp = infos[i].split("=");        //splits by = trying to get the right hand side
            String[] temp2 = temp[1].split("\"");       //trying to get the data without the "
            String data = temp2[1];                     //gets the middle value,
            switch(i) {
                case 0: TABLE_NAME = data; break;
                case 1: USER_NAME = data; break;
                case 2: PASSWORD = data; break;
                case 3: DB_NAME = data; break;
            }
        }
    }
}

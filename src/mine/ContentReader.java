/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mine;

import org.apache.poi.poifs.filesystem.*;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hslf.HSLFSlideShow;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xslf.XSLFSlideShow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.htmlparser.parserapplications.*;
import org.htmlparser.util.ParserException;
import java.io.*;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Definition: Reads MS Word documents, MS Excel documents, MS Powerpoint documents, *.txt files, PDF files and Website/blog contents
 * @author wella
 */
public class ContentReader {
    private StringBuffer TextBuffer;    //Used in method readODFFile
    /**
     * File reader for *.txt
     */
    public String readTxtFile(String filename) {
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
     * MS Word document reader
     * Reads *.doc and *.docx
     */
    public String readDocFile(String filename) {
        String content = "";
        File file = new File(filename);
        try {
            FileInputStream stream = new FileInputStream(file);
            if(filename.endsWith(".doc")) {
                POIFSFileSystem fs = new POIFSFileSystem(stream);
                HWPFDocument doc = new HWPFDocument(fs);
                WordExtractor we = new WordExtractor(doc);
                String[] paragraphs = we.getParagraphText();
                for( int i=0; i<paragraphs.length; i++ ) {
                    content += paragraphs[i];
                }
            }
            else if(filename.endsWith(".docx")) {
                XWPFDocument doc = new XWPFDocument(stream);
                XWPFWordExtractor we = new XWPFWordExtractor(doc);
                String text = we.getText();
                content += text;
            }
        }
        catch(Exception e) {e.printStackTrace(); }
        return content;
    }

    /**
     * PDF File reader
     * Reads *.pdf files
     */
    public String readPDFFile(String filename) {
        String content = "";
        try {
            File file = new File(filename);
            PDDocument pdd = new PDDocument();
            pdd = PDDocument.load(file);
            PDFTextStripper pts = new PDFTextStripper();
            content += pts.getText(pdd);
            pdd.close();
        }
        catch(Exception e) { e.printStackTrace(); }
        return content;
    }

    /**
     * MS Powerpoint reader
     * Reads *.ppt and *.pptx
     */
    public String readPPTFile(String filename) {
        String content = "";
        File file = new File(filename);
        POIFSFileSystem fs = null;
        try {
            FileInputStream stream = new FileInputStream(file);
            if(filename.endsWith(".ppt")) {
                fs = new POIFSFileSystem(stream);
                HSLFSlideShow ppt = new HSLFSlideShow(fs);
                PowerPointExtractor powerPointExtractor = new PowerPointExtractor(ppt);
                content += powerPointExtractor.getText(true, true);
            }
            else if(filename.endsWith(".pptx")) {
                XSLFSlideShow ppt = new XSLFSlideShow(filename);
                XSLFPowerPointExtractor powerPointExtractor = new XSLFPowerPointExtractor(ppt);
                content += powerPointExtractor.getText(true, true);
            }
        }
        catch(Exception e) { e.printStackTrace(); }
        return content;
     }

    /**
     * MS Excel reader
     * Reads *.xls and *.xlsx files
     */
     public String readExcelFile(String filename) {
        String content = "";
        if(filename.endsWith(".xls")) {
            content += this.readExcel2003(filename);
        }
        else if(filename.endsWith(".xlsx")) {
            content += this.readExcel2007(filename);
        }
        return content;
     }

     /**
      * Helper fucntion that reads MS Excel 2007
      */
     private String readExcel2007(String s) {
        String content = "";
        try {
            Workbook wb = new XSSFWorkbook(s);
            for(int i = 0; i < wb.getNumberOfSheets(); i++) {
                Sheet sheet = wb.getSheetAt(0);
                Iterator rows = sheet.iterator();
                while(rows.hasNext()) {
                    XSSFRow row = (XSSFRow)rows.next();
                    Iterator cells = row.cellIterator();
                    while(cells.hasNext()) {
                        XSSFCell cell = (XSSFCell)cells.next();
                        switch(cell.getCellType()) {
                            case XSSFCell.CELL_TYPE_NUMERIC:
                                content += cell.getNumericCellValue();
                                break;
                            case XSSFCell.CELL_TYPE_STRING:
                                String tmp = cell.getRichStringCellValue().getString();
                                content += tmp;
                                break;
                            default: System.out.println("Type not supported"); break;
                        }
                    }
                }
            }

        }
        catch(Exception e) { e.printStackTrace(); }
        return content;
    }

     /**
      * Helper function that reads MS Excel 2003
      */
     private String readExcel2003(String s) {
         String content = "";
         try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(s));
            HSSFSheet sheet = workbook.getSheetAt(0);
            Iterator rows = sheet.rowIterator();
            while(rows.hasNext()) {
                HSSFRow row = (HSSFRow)rows.next();
                Iterator cells = row.cellIterator();
                while(cells.hasNext()) {
                    HSSFCell cell = (HSSFCell)cells.next();
                    switch(cell.getCellType()) {
                        case HSSFCell.CELL_TYPE_NUMERIC:
                            content += cell.getNumericCellValue();
                            break;
                        case HSSFCell.CELL_TYPE_STRING:
                            String tmp = cell.getRichStringCellValue().getString();
                            content += tmp;
                            break;
                        default: System.out.println("Type not supported"); break;
                    }
                }
            }
        }
        catch (Exception e) {
            System.out.println("!! Bang !! xlRead() : " + e);
        }
         return content;
     }

     /**
      * Website/blog text reader
      * Extract plaintext strings from a web page
      */
     public String readWebText(String source) {
        String text = "";
        StringExtractor sExt = new StringExtractor(source);
        try {
            text = sExt.extractStrings(false);
        }
        catch(ParserException p) {
            p.printStackTrace();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return text;
    }

     /**
      * ODF text extractor
      * Extracts text from OpenOffice documents
      */
     public String readODFFile(String filename) {
         String result = "";
         try {
            result = getText(filename);
         }
         catch(Exception e) {e.printStackTrace();}
         return result;
     }

    //Process text elements recursively
    public void processElement(Object o) {

        if (o instanceof Element) {

            Element e = (Element) o;
            String elementName = e.getQualifiedName();

            if (elementName.startsWith("text")) {

                if (elementName.equals("text:tab")) // add tab for text:tab
                    TextBuffer.append("\t");
                else if (elementName.equals("text:s"))  // add space for text:s
                    TextBuffer.append(" ");
                else {
                    List children = e.getContent();
                    Iterator iterator = children.iterator();

                    while (iterator.hasNext()) {

                        Object child = iterator.next();
                        //If Child is a Text Node, then append the text
                        if (child instanceof Text) {
                            Text t = (Text) child;
                            TextBuffer.append(t.getValue());
                        }
                        else
                        processElement(child); // Recursively process the child element
                    }
                }
                if (elementName.equals("text:p"))
                    TextBuffer.append("\n");
            }
            else {
                List non_text_list = e.getContent();
                Iterator it = non_text_list.iterator();
                while (it.hasNext()) {
                    Object non_text_child = it.next();
                    processElement(non_text_child);
                }
            }
        }
    }

    public String getText(String fileName) throws Exception {
        TextBuffer = new StringBuffer();

        //Unzip the openOffice Document
        ZipFile zipFile = new ZipFile(fileName);
        Enumeration entries = zipFile.entries();
        ZipEntry entry;

        while(entries.hasMoreElements()) {
            entry = (ZipEntry) entries.nextElement();

            if (entry.getName().equals("content.xml")) {

                TextBuffer = new StringBuffer();
                SAXBuilder sax = new SAXBuilder();
                Document doc = sax.build(zipFile.getInputStream(entry));
                Element rootElement = doc.getRootElement();
                processElement(rootElement);
                break;
            }
        }
        return TextBuffer.toString();
    }

}

/*
 * MineView.java
 */

package mine;

import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.text.*;
import java.awt.Color;
import java.util.ArrayList;

/**
 * The application's main frame.
 */
public class MineView extends FrameView {
    private boolean searched = false;
    private MineIt mine;
    private Highlighter.HighlightPainter myHighlightPainter = new MyHighlightPainter(Color.red);        // An instance of the private subclass of the default highlight painter
    
    public MineView(SingleFrameApplication app) {
        super(app);
        initComponents();
        resultPanel.setVisible(false);

        mine = new MineIt();
        mine.populateDB();

        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(false);

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    statusMessageLabel.setText((text == null) ? "" : text);
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
                }
            }
        });
    }

    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = MineApp.getApplication().getMainFrame();
            aboutBox = new MineAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        MineApp.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        scrollPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        lblLogo = new javax.swing.JLabel();
        txtKeyword = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        resultPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPaneResult = new javax.swing.JTextPane();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();
        statusPanel = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusMessageLabel = new javax.swing.JLabel();
        statusAnimationLabel = new javax.swing.JLabel();
        progressBar = new javax.swing.JProgressBar();

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mine.MineApp.class).getContext().getResourceMap(MineView.class);
        mainPanel.setBackground(resourceMap.getColor("mainPanel.background")); // NOI18N
        mainPanel.setFont(resourceMap.getFont("mainPanel.font")); // NOI18N
        mainPanel.setName("mainPanel"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        scrollPanel.setBackground(resourceMap.getColor("scrollPanel.background")); // NOI18N
        scrollPanel.setName("scrollPanel"); // NOI18N

        headerPanel.setBackground(resourceMap.getColor("headerPanel.background")); // NOI18N
        headerPanel.setName("headerPanel"); // NOI18N

        lblLogo.setBackground(resourceMap.getColor("lblLogo.background")); // NOI18N
        lblLogo.setIcon(resourceMap.getIcon("lblLogo.icon")); // NOI18N
        lblLogo.setText(resourceMap.getString("lblLogo.text")); // NOI18N
        lblLogo.setName("lblLogo"); // NOI18N

        txtKeyword.setFont(resourceMap.getFont("txtKeyword.font")); // NOI18N
        txtKeyword.setText(resourceMap.getString("txtKeyword.text")); // NOI18N
        txtKeyword.setName("txtKeyword"); // NOI18N
        txtKeyword.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtKeywordKeyPressed(evt);
            }
        });

        btnSearch.setBackground(resourceMap.getColor("btnSearch.background")); // NOI18N
        btnSearch.setFont(resourceMap.getFont("btnSearch.font")); // NOI18N
        btnSearch.setText(resourceMap.getString("btnSearch.text")); // NOI18N
        btnSearch.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSearch.setName("btnSearch"); // NOI18N
        btnSearch.setMnemonic('S');
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout headerPanelLayout = new javax.swing.GroupLayout(headerPanel);
        headerPanel.setLayout(headerPanelLayout);
        headerPanelLayout.setHorizontalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(270, 270, 270)
                        .addComponent(lblLogo))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(537, 537, 537)
                        .addComponent(btnSearch))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(419, 419, 419)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 311, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(359, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(btnSearch)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        resultPanel.setBackground(resourceMap.getColor("resultPanel.background")); // NOI18N
        resultPanel.setName("resultPanel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        txtPaneResult.setName("txtPaneResult"); // NOI18N
        jScrollPane2.setViewportView(txtPaneResult);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1229, Short.MAX_VALUE)
                .addContainerGap())
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 304, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout scrollPanelLayout = new javax.swing.GroupLayout(scrollPanel);
        scrollPanel.setLayout(scrollPanelLayout);
        scrollPanelLayout.setHorizontalGroup(
            scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scrollPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(resultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        scrollPanelLayout.setVerticalGroup(
            scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scrollPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(resultPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jScrollPane1.setViewportView(scrollPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1292, Short.MAX_VALUE)
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 604, Short.MAX_VALUE)
        );

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N
        fileMenu.setMnemonic('F');

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(mine.MineApp.class).getContext().getActionMap(MineView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N
        helpMenu.setMnemonic('H');

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        statusPanel.setName("statusPanel"); // NOI18N

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N

        statusMessageLabel.setName("statusMessageLabel"); // NOI18N

        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N

        progressBar.setName("progressBar"); // NOI18N

        javax.swing.GroupLayout statusPanelLayout = new javax.swing.GroupLayout(statusPanel);
        statusPanel.setLayout(statusPanelLayout);
        statusPanelLayout.setHorizontalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(statusPanelSeparator, javax.swing.GroupLayout.DEFAULT_SIZE, 1302, Short.MAX_VALUE)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(statusMessageLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 1132, Short.MAX_VALUE)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(statusAnimationLabel)
                .addContainerGap())
        );
        statusPanelLayout.setVerticalGroup(
            statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(statusPanelLayout.createSequentialGroup()
                .addComponent(statusPanelSeparator, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(statusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(statusMessageLabel)
                    .addComponent(statusAnimationLabel)
                    .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3))
        );

        setComponent(mainPanel);
        setMenuBar(menuBar);
        setStatusBar(statusPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        btnSearch.setEnabled(false);
        if(!searched) {                                             //resetting the locations of the logo, textfield and button
            ClassLoader cldr = this.getClass().getClassLoader();
            java.net.URL imageURL = cldr.getResource("mine/resources/mineItUp2.JPG");
            ImageIcon icon = new ImageIcon(imageURL);
            this.lblLogo.setIcon(icon);
            this.lblLogo.setSize(icon.getIconWidth(), icon.getIconHeight());
            this.lblLogo.setLocation(10, 10);
            this.txtKeyword.setLocation(300,30);
            this.btnSearch.setLocation(420,70);
            this.mainPanel.repaint();
            searched = true;
        }
        if(!txtKeyword.getText().trim().equals("")) {
            FullTextSearch fts = new FullTextSearch();
            fts.search(txtKeyword.getText().trim());
            displayResults(fts.getExtractedTexts());
            resultPanel.setVisible(true);
            //ArrayList<String> pattern = mine.getSynonymsOfKeyword();
            //pattern.add(txtKeyword.getText());
           // highlight(txtPaneResult, pattern);
        }        
        btnSearch.setEnabled(true);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void displayResults(java.util.LinkedHashMap texts) {
        String resultPath = "";
        String resultContents = "";
        java.util.Iterator i = texts.keySet().iterator();
        java.util.Iterator j = texts.entrySet().iterator();
        while(i.hasNext()) {
            resultPath += i.next() + "\n";
            resultContents += j.next() + "\n********************\n";
        }
        String res = texts.size() + " Search Result(s)";
        javax.swing.JOptionPane.showMessageDialog(null, resultPath, res, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        txtPaneResult.setText(resultContents);

    }

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            btnSearch.doClick();
        }
    }//GEN-LAST:event_txtKeywordKeyPressed

        // Creates highlights around all occurrences of pattern in textComp
    public void highlight(JTextComponent textComp, String pattern) {
        // First remove all old highlights
        removeHighlights(textComp);
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            // Search for pattern
            while ((pos = text.indexOf(pattern, pos)) >= 0) {
                // Create highlighter using private painter and apply around pattern
                hilite.addHighlight(pos, pos+pattern.length(), myHighlightPainter);
                pos += pattern.length();
            }
        } catch (BadLocationException e) {
        }
    }

     public void highlight(JTextComponent textComp, ArrayList<String> pattern) {
        // First remove all old highlights
        removeHighlights(textComp);

        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            //rita.RiKWICker wicker = new rita.RiKWICker(null);
           // String[] linesOfText = text.split("\r\n");
           // wicker.addLines(linesOfText);
            // Search for pattern
            for(String p : pattern) {
                while ((pos = text.indexOf(p, pos)) >= 0) {
                    //if(text.charAt(pos-1) == ' ' && text.charAt(pos+1) == ' ') {
                        // Create highlighter using private painter and apply around pattern
                        hilite.addHighlight(pos, pos+p.length(), myHighlightPainter);
                        pos += p.length();
                    //}
                }
            }
        } catch (BadLocationException e) {
        }
    }

    // Removes only our private highlights
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

    // A private subclass of the default highlight painter
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JPanel scrollPanel;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JTextField txtKeyword;
    private javax.swing.JTextPane txtPaneResult;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;
}

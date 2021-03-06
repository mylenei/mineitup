/*
 * MineView.java
 */

package mine;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;

import org.xeustechnologies.googleapi.spelling.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import javax.swing.text.*;
import javax.swing.JOptionPane;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.ListIterator;
/**
 * The application's main frame.
 */
public class MineView extends FrameView {
    private boolean searched = false;
    private MineIt mine;
    private Highlighter.HighlightPainter myHighlightPainter;
    private LinkedHashMap texts;
    private Iterator keyI;
    private ListIterator valueIterator;
    private int ctr = 0, resultCount = 0;
    
    public MineView(SingleFrameApplication app) {
        super(app);
        initComponents();
        resultPanel.setVisible(false);
        lblSuggestions.setVisible(false);

        mine = new MineIt();
   
        myHighlightPainter = new MyHighlightPainter(Color.yellow);        // An instance of the private subclass of the default highlight painter
        texts = new LinkedHashMap();
        
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
        jComboBox1 = new javax.swing.JComboBox();
        resultPanel = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtPaneResult = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        lblResults = new javax.swing.JLabel();
        lblSuggestions = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        addDatasourceMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        reloadFormMenuItem = new javax.swing.JMenuItem();
        refreshAllMenuItem = new javax.swing.JMenuItem();
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

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Max Search", "Normal Search", "Min Search", "Boolean Search" }));
        jComboBox1.setName("jComboBox1"); // NOI18N

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
                        .addGap(408, 408, 408)
                        .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(headerPanelLayout.createSequentialGroup()
                        .addGap(523, 523, 523)
                        .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(359, Short.MAX_VALUE))
        );
        headerPanelLayout.setVerticalGroup(
            headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(headerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblLogo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(headerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addComponent(btnSearch, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE))
        );

        resultPanel.setBackground(resourceMap.getColor("resultPanel.background")); // NOI18N
        resultPanel.setName("resultPanel"); // NOI18N

        jScrollPane2.setName("jScrollPane2"); // NOI18N

        txtPaneResult.setEditable(false);
        txtPaneResult.setName("txtPaneResult"); // NOI18N
        jScrollPane2.setViewportView(txtPaneResult);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);
        jToolBar1.setName("jToolBar1"); // NOI18N

        btnBack.setIcon(resourceMap.getIcon("btnBack.icon")); // NOI18N
        btnBack.setText(resourceMap.getString("btnBack.text")); // NOI18N
        btnBack.setToolTipText(resourceMap.getString("btnBack.toolTipText")); // NOI18N
        btnBack.setFocusable(false);
        btnBack.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        jToolBar1.add(btnBack);

        btnNext.setIcon(resourceMap.getIcon("btnNext.icon")); // NOI18N
        btnNext.setText(resourceMap.getString("btnNext.text")); // NOI18N
        btnNext.setFocusable(false);
        btnNext.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnNext.setName("btnNext"); // NOI18N
        btnNext.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });
        jToolBar1.add(btnNext);

        lblResults.setText(resourceMap.getString("lblResults.text")); // NOI18N
        lblResults.setName("lblResults"); // NOI18N
        jToolBar1.add(lblResults);

        javax.swing.GroupLayout resultPanelLayout = new javax.swing.GroupLayout(resultPanel);
        resultPanel.setLayout(resultPanelLayout);
        resultPanelLayout.setHorizontalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(resultPanelLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1239, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
        resultPanelLayout.setVerticalGroup(
            resultPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(resultPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblSuggestions.setBackground(resourceMap.getColor("lblSuggestions.background")); // NOI18N
        lblSuggestions.setText(resourceMap.getString("lblSuggestions.text")); // NOI18N
        lblSuggestions.setName("lblSuggestions"); // NOI18N

        javax.swing.GroupLayout scrollPanelLayout = new javax.swing.GroupLayout(scrollPanel);
        scrollPanel.setLayout(scrollPanelLayout);
        scrollPanelLayout.setHorizontalGroup(
            scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scrollPanelLayout.createSequentialGroup()
                .addGroup(scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(scrollPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(resultPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(scrollPanelLayout.createSequentialGroup()
                        .addGap(437, 437, 437)
                        .addComponent(lblSuggestions, javax.swing.GroupLayout.PREFERRED_SIZE, 291, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
        );
        scrollPanelLayout.setVerticalGroup(
            scrollPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(scrollPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(headerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblSuggestions, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
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

        addDatasourceMenuItem.setText(resourceMap.getString("addDatasourceMenuItem.text")); // NOI18N
        addDatasourceMenuItem.setName("addDatasourceMenuItem"); // NOI18N
        addDatasourceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDatasourceMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(addDatasourceMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(mine.MineApp.class).getContext().getActionMap(MineView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        jMenu1.setText(resourceMap.getString("jMenu1.text")); // NOI18N
        jMenu1.setName("jMenu1"); // NOI18N

        reloadFormMenuItem.setText(resourceMap.getString("reloadFormMenuItem.text")); // NOI18N
        reloadFormMenuItem.setName("reloadFormMenuItem"); // NOI18N
        reloadFormMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadFormMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(reloadFormMenuItem);

        refreshAllMenuItem.setText(resourceMap.getString("refreshAllMenuItem.text")); // NOI18N
        refreshAllMenuItem.setName("refreshAllMenuItem"); // NOI18N
        refreshAllMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(refreshAllMenuItem);

        menuBar.add(jMenu1);

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
        
        if(!txtKeyword.getText().trim().equals("")) { //if keyword is entered
            lblSuggestions.setText("Did you mean: ");
            KeywordProcessor kprocess = new KeywordProcessor();
            if(!kprocess.spellCheck(txtKeyword.getText().trim())) { //if spelling is wrong, suggest words
                SpellCorrection[] suggestions = kprocess.getSpellingSuggestions(txtKeyword.getText().trim());
                if(suggestions != null) {
                    for(SpellCorrection sc : suggestions) {
                        String[] s = sc.getWords();
                        lblSuggestions.setText(lblSuggestions.getText() + " " + s[0] + "\t");
                        lblSuggestions.setVisible(true);
                    }
                }
            }
            //else {
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
                FullTextSearch fts = new FullTextSearch();
                try {
                    fts.search(txtKeyword.getText().trim());
                } catch (SQLException ex) {
                    Logger.getLogger(MineView.class.getName()).log(Level.SEVERE, null, ex);
                }
                texts = fts.getExtractedTexts();
                keyI = texts.keySet().iterator();
                valueIterator = fts.getContentList().listIterator();
                ctr = 0;
                displayInitialResult();
                resultPanel.setVisible(true);
                rita.RiString text = new rita.RiString(txtKeyword.getText().trim());// + new rita.RiStemmer().stem(txtKeyword.getText().trim()));
                String[] words = text.getWords();
                highlight(txtPaneResult, words);

           // }
        }        
        btnSearch.setEnabled(true);
    }//GEN-LAST:event_btnSearchActionPerformed

    private void displayInitialResult() {
        String resultPath = "";
        while(keyI.hasNext()) {
            resultPath += keyI.next() + "\n";
        }
        resultCount = texts.size();
        String res = resultCount + " Search Result(s)";
        javax.swing.JOptionPane.showMessageDialog(null, resultPath, res, javax.swing.JOptionPane.INFORMATION_MESSAGE);
        String resultMsg = "     " + ++ctr + "/" + resultCount;
        this.lblResults.setText(resultMsg);
        if(valueIterator.hasNext()) {
            txtPaneResult.setText(valueIterator.next().toString()); //the first result is displayed
        }
    }

    private void txtKeywordKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKeywordKeyPressed
        if(evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
            btnSearch.doClick();
        }
    }//GEN-LAST:event_txtKeywordKeyPressed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        if(valueIterator.hasNext()) {
            String resultMsg = "     " + ++ctr + "/" + resultCount;
            this.lblResults.setText(resultMsg);
            txtPaneResult.setText(valueIterator.next().toString());
            rita.RiString text = new rita.RiString(txtKeyword.getText().trim());// + new rita.RiStemmer().stem(txtKeyword.getText().trim()));
            String[] words = text.getWords();
            highlight(txtPaneResult, words);
        }
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
       if(valueIterator.hasPrevious()) {
            String resultMsg = "     " + --ctr + "/" + resultCount;
            this.lblResults.setText(resultMsg);
            txtPaneResult.setText(valueIterator.previous().toString());
            rita.RiString text = new rita.RiString(txtKeyword.getText().trim());// + new rita.RiStemmer().stem(txtKeyword.getText().trim()));
            String[] words = text.getWords();
            highlight(txtPaneResult, words);
       }
    }//GEN-LAST:event_btnBackActionPerformed

    private void reloadFormMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadFormMenuItemActionPerformed
        scrollPanel.repaint();
    }//GEN-LAST:event_reloadFormMenuItemActionPerformed

    /*
     * updates the local copy of the data source
     */
    private void refreshAllMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllMenuItemActionPerformed
        //mine.reloadContents();
    }//GEN-LAST:event_refreshAllMenuItemActionPerformed

    /*
     * asks the user for a path to the data source and adds that data source to the DB
     */
    private void addDatasourceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDatasourceMenuItemActionPerformed
        Filechooser choice = new Filechooser(this.getFrame(),true);//javax.swing.JOptionPane.showMessageDialog(null, choice.getPath());
        int result = JOptionPane.showConfirmDialog(null, "Insert the path and the file contents to the DB?");
        if(result == JOptionPane.OK_OPTION) {
           String path = choice.getPath();
           path = path.replace("\\", "/");
           if(mine.addDataSource(path) != -1) {
                JOptionPane.showMessageDialog(null, "You have successfully added a data source \n" + path, "Success", JOptionPane.INFORMATION_MESSAGE);
           }
           else{
                JOptionPane.showMessageDialog(null, "Adding datasource failed!", "Error", JOptionPane.ERROR_MESSAGE);
           }
        }
        else  {
            JOptionPane.showMessageDialog(null, "You choose to cancel the operation of adding a datasource");
        }
    }//GEN-LAST:event_addDatasourceMenuItemActionPerformed

    /*
     * Creates highlights around all occurrences of pattern in textComp
     */
    public void highlight(JTextComponent textComp, String pattern) {
        removeHighlights(textComp);                                                    // First remove all old highlights
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            rita.RiString ritext = new rita.RiString(text);
            String[] words = ritext.getWords();
            for(String s : words) {
                if(pattern.equalsIgnoreCase(s)) {
                    pos = text.indexOf(s, pos);
                    hilite.addHighlight(pos, pos + s.length(), myHighlightPainter);
                    pos += s.length();
                }
            }
        } catch (BadLocationException e) {
        }
    }

    /*
     * Creates highlights around all occurrences of pattern in textComp
     */
    public void highlight(JTextComponent textComp, String[] pattern) {
        removeHighlights(textComp);                                                    // First remove all old highlights
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            rita.RiString ritext = new rita.RiString(text);
            String[] words = ritext.getWords();
            for(String s : words) {
                for(String p : pattern) {
                    if(p.equalsIgnoreCase(s)) {
                        pos = text.indexOf(s, pos);
                        hilite.addHighlight(pos, pos + s.length(), myHighlightPainter);
                        pos += s.length();
                    }
                }
            }
        } catch (BadLocationException e) {
        }
    }

     public void highlight(JTextComponent textComp, ArrayList<String> pattern) {
        removeHighlights(textComp);                                                     // First remove all old highlights
        try {
            Highlighter hilite = textComp.getHighlighter();
            Document doc = textComp.getDocument();
            String text = doc.getText(0, doc.getLength());
            int pos = 0;
            for(String p : pattern) {
                while ((pos = text.indexOf(p, pos)) >= 0) {
                    hilite.addHighlight(pos, pos+p.length(), myHighlightPainter);
                    pos += p.length();
                }
            }
        } catch (BadLocationException e) {
        }
    }

    /*
     * Removes only our private highlights
     */
    public void removeHighlights(JTextComponent textComp) {
        Highlighter hilite = textComp.getHighlighter();
        Highlighter.Highlight[] hilites = hilite.getHighlights();

        for (int i=0; i<hilites.length; i++) {
            if (hilites[i].getPainter() instanceof MyHighlightPainter) {
                hilite.removeHighlight(hilites[i]);
            }
        }
    }

    /*
     * A private subclass of the default highlight painter
     */
    class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {
        public MyHighlightPainter(Color color) {
            super(color);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem addDatasourceMenuItem;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnSearch;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JLabel lblLogo;
    private javax.swing.JLabel lblResults;
    private javax.swing.JLabel lblSuggestions;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JMenuItem refreshAllMenuItem;
    private javax.swing.JMenuItem reloadFormMenuItem;
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
    private JDialog chooser;
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Filechooser.java
 *
 * Created on 02 27, 11, 9:09:25 AM
 */

package mine;

import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author wella
 */
public class Filechooser extends javax.swing.JDialog {
    int returnVal;
    private String path = "";
    /** Creates new form Filechooser */
    public Filechooser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        returnVal = filechooser.showOpenDialog(parent);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filechooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); // NOI18N

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(mine.MineApp.class).getContext().getResourceMap(Filechooser.class);
        filechooser.setDialogTitle(resourceMap.getString("filechooser.dialogTitle")); // NOI18N
        filechooser.setFileFilter(new FileNameExtensionFilter("Documents", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "odt", "odp", "ods", "pdf", "txt"));
        filechooser.setName("filechooser"); // NOI18N
        filechooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filechooserActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(filechooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void filechooserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filechooserActionPerformed
        //Handle open button action.
        if (returnVal == filechooser.APPROVE_OPTION) {
            java.io.File file = filechooser.getSelectedFile();
            path = file.getAbsolutePath();
        } else {
            int i = javax.swing.JOptionPane.showConfirmDialog(rootPane, "Are you sure you want to exit?");
            if(i == javax.swing.JOptionPane.OK_OPTION) {
                this.dispose();
            }
        }
    }//GEN-LAST:event_filechooserActionPerformed

    /**
    * @param args the command line arguments
    */
//    public static void main(String args[]) {
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                Filechooser dialog = new Filechooser(new javax.swing.JFrame(), true);
//                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
//                    public void windowClosing(java.awt.event.WindowEvent e) {
//                        System.exit(0);
//                    }
//                });
//                dialog.setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser filechooser;
    // End of variables declaration//GEN-END:variables

}

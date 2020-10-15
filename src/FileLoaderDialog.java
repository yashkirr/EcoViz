/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author yashkir
 */
public class FileLoaderDialog extends javax.swing.JDialog {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCancel;
    private JButton btnDirectoryInput;
    private JButton btnLoadFiles;
    private JLabel lblElevationInput;
    private JTextField txtDirectoryInput;
    // End of variables declaration//GEN-END:variables

    private java.awt.Frame parentFrame;
    private String elv;
    private String spc;
    private String pdbCan;
    private String pdbUnder;
    private String dataDirectory;
    private Controller localController;


    /**
     * Creates new form FileLoaderDialog
     */
    public FileLoaderDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        parentFrame = parent;
        initComponents();
        this.setLocationRelativeTo(null);
        localController = new Controller();
        this.setResizable(false);
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        txtDirectoryInput = new javax.swing.JTextField();
        btnDirectoryInput = new javax.swing.JButton();
        lblElevationInput = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        btnLoadFiles = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        btnDirectoryInput.setText("...");
        btnDirectoryInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDirectoryInputActionPerformed(evt);
            }
        });

        lblElevationInput.setText("Select a directory containing .elv, .spc, .pdb files");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnLoadFiles.setBackground(new java.awt.Color(54, 128, 239));
        btnLoadFiles.setForeground(new java.awt.Color(255, 255, 255));
        btnLoadFiles.setText("Load Files");
        btnLoadFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                try {
                    btnLoadFilesActionPerformed(evt);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(70, 70, 70)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lblElevationInput)
                                                .addContainerGap(130, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(txtDirectoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, 351, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnDirectoryInput, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(0, 0, Short.MAX_VALUE)
                                                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(btnLoadFiles, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGap(69, 69, 69))))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(70, Short.MAX_VALUE)
                                .addComponent(lblElevationInput)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtDirectoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDirectoryInput, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnLoadFiles)
                                        .addComponent(btnCancel))
                                .addGap(79, 79, 79))
        );

        pack();
    }   // </editor-fold>//GEN-END:initComponents

    private void btnDirectoryInputActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileFilter(new FileNameExtensionFilter("Folders only","/"));
        chooser.setAcceptAllFileFilterUsed(false);

        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

            System.out.println("getCurrentDirectory(): "
                    +  chooser.getCurrentDirectory());

            System.out.println("getSelectedFile() : "
                    +  chooser.getSelectedFile());
            txtDirectoryInput.setText(chooser.getSelectedFile().toString());

        }
        else {
            System.out.println("No Selection ");
        }
    }

    public void print(String s){
        System.out.println("FileLoaderDialog: "+s);
    }

    private void btnLoadFilesActionPerformed(ActionEvent evt) throws IOException {
        this.setVisible(false);
        print("btnLoadFilesActionPerformed");
        dataDirectory = txtDirectoryInput.getText();

        try{
            ArrayList<String> fileNameList = FileLoader.listFileNamesInDirectory(new File(dataDirectory)); //Stores file names excluding paths
            if(!validateDataDirectory(fileNameList)){throw new IOException("File Input Error");}
            ArrayList<String> filePathList = FileLoader.listFilePathsInDirectory(new File(dataDirectory));
            Collections.sort(filePathList);

            /* Collections.sort(filePathList) sorts the list of file paths
            to avoid read methods loading the incorrect files.
            The list is as follows (index position):

            0: .elv
            1: .spc
            2: .pdb (canopy)
            3: .pdb (undergrowth)

             */
            LoadingSplashDialog loadingSplashDialog = new LoadingSplashDialog();
            loadingSplashDialog.setVisible(true);
            FileLoadingWorker fileLoadingWorker = new FileLoadingWorker(filePathList);
            fileLoadingWorker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    System.out.println(propertyChangeEvent.getPropertyName());
                    Object value = propertyChangeEvent.getNewValue();
                    if (value instanceof SwingWorker.StateValue) {
                        SwingWorker.StateValue state = (SwingWorker.StateValue) value;
                        switch (state) {
                            case DONE: {
                                try {
                                    fileLoadingWorker.get();
                                    localController.initializeTerrainGrid();
                                    loadingSplashDialog.dispose();
                                } catch (InterruptedException | ExecutionException | IOException ex) {
                                    ex.printStackTrace();
                                }
                            }
                            break;
                        }
                    }
                }
            });
            fileLoadingWorker.execute();
            dispose();

        }catch(IOException e){
            JOptionPane.showMessageDialog(this,"Please check the directory contents for correctness.",
                    "Error",JOptionPane.WARNING_MESSAGE);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean validateDataDirectory(ArrayList<String> fileList) throws IOException {
       //Test 1: Checks if 4 files in directory (pdb,pdb,elv,spc)
        if(fileList.size() !=4){ return false; }

        //Test 2: Checks if files are named correctly if passes the first test, files should have a set name with "-"
        //Characters prior to the "-" indicates the name of the data set, baseSubset stores this name
        String baseSubset = "";
        int dashFirstOccurrence = fileList.get(0).indexOf("-");
        if(dashFirstOccurrence != -1){
            baseSubset = fileList.get(0).substring(0,dashFirstOccurrence);
        }else{
            return false;
        }

        //Test 3: Checks if files belong to the same data set (baseSubset) and counts number of file extensions
        int pdb = 0,elv = 0,spc = 0;
        for (String file:
             fileList) {
            int dashFirstOccurrence2 = file.indexOf("-"); //checks for dash of consequent files
            if(dashFirstOccurrence2 != -1){
                String currentSubset = file.substring(0,dashFirstOccurrence2);
                if(!currentSubset.equals(baseSubset)){return false;}
                String extension = file.substring(file.lastIndexOf(".")+1);
                
                switch (extension){
                    case "pdb":
                        pdb++;
                        break;
                    case "elv":
                        elv++;
                        break;
                    case "spc":
                        spc++;
                        break;
                }
            }else{
                return false;
            }
            
        }
        
        //Test 4: Check if the file correct number of file extensions exist( 2 pdb, 1 elv, 1 spc)
        if(!((pdb == 2) && (elv ==1) && (spc == 1))){
            return false;
        }

        return true;
    }

    /**
     * Closes the dialog
     * @param evt
     */
    private void btnCancelActionPerformed(ActionEvent evt) {
        dispose();
    }


    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileLoaderDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileLoaderDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileLoaderDialog.class.getName()).log(Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileLoaderDialog.class.getName()).log(Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> {
            FileLoaderDialog dialog = new FileLoaderDialog(new JFrame(), true);
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }


}

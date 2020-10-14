/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.stream.Stream;
import javax.swing.*;

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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtDirectoryInput = new JTextField();
        btnDirectoryInput = new JButton();
        lblElevationInput = new JLabel();
        btnCancel = new JButton();
        btnLoadFiles = new JButton();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        btnDirectoryInput.setText("...");
        btnDirectoryInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnDirectoryInputActionPerformed(evt);
            }
        });

        lblElevationInput.setText("Select directory containing .elv , .spc , .pbd files");

        btnCancel.setText("Cancel");
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnLoadFiles.setText("Load Files");
       btnLoadFiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnLoadFilesActionPerformed(evt);
            }
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(btnLoadFiles, GroupLayout.PREFERRED_SIZE, 94, GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(txtDirectoryInput, GroupLayout.PREFERRED_SIZE, 351, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(btnDirectoryInput)))
                    .addComponent(lblElevationInput)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(81, 81, 81)
                .addComponent(lblElevationInput)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDirectoryInput, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtDirectoryInput, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnLoadFiles))
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDirectoryInputActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        chooser.setCurrentDirectory(new java.io.File("."));
        chooser.setDialogTitle("Select Folder");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
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

    private void btnLoadFilesActionPerformed(ActionEvent evt) {
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
            localController.loadFile(filePathList.get(0),"elv");
            localController.loadFile(filePathList.get(1),"spc");
            localController.loadFile(filePathList.get(2),"pdb");
            localController.loadFile(filePathList.get(3),"pdb");

            localController.initializeTerrainGrid();
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
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

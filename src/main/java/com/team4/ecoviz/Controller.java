/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author yashkir
 */
public class Controller {
    private static boolean initialized = false;

    public static String selectFile(String name, String fileType) {
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setFileFilter(new FileNameExtensionFilter(name, fileType));
        int returnVal = fileDialog.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return fileDialog.getSelectedFile().getAbsolutePath();
        }
        return "No File Selected";
    }

    public static void loadFile(){
        // TODO: Connect FileLoader
    }

    public static void setInitialization(boolean value){
        initialized = value;
    }

    public static boolean checkInitialization(){
        return initialized;
    }

    /*
    Temporary Prototype Methods
     */

    /**
     * Displays a loading screen window
     * @param parentFrame
     */
    public static void showLoadingScreen(java.awt.Frame parentFrame) throws IOException {
        
        closeLoadingScreenAfterTime(4).start();
        
        JOptionPane.showOptionDialog(parentFrame, "Loading files, please wait...","Initializing EcoViz", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
        updateVisualizer();
    }

    private static Timer closeLoadingScreenAfterTime(int seconds) {
        ActionListener close = (ActionEvent e) -> {
            Window[] windows = Window.getWindows();
            for (Window window : windows) {
                if (window instanceof JDialog) {
                    JDialog dialog = (JDialog) window;
                    if (dialog.getContentPane().getComponentCount() == 1
                            && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                        dialog.dispose();
                    }
                }
            }
        };
        Timer t = new Timer(seconds * 1000, close);
        t.setRepeats(false);
        return t;
    }

    public static void updateVisualizer() throws IOException {
        BufferedImage dummyViz = ImageIO.read(new File("EcoViz Data/DummyVizUsing256.png"));
        //int width = dummyViz.getWidth();
       // int height = dummyViz.getHeight();
        
        //dummyVizLabel.setMinimumSize(new Dimension(width,height));
       // dummyVizLabel.setBounds(1,1,1200,1200);
        UserView.setVisualizerScreen(dummyViz);
    }
}




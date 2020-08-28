/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * monkey see monkey doo
 * @author yashkir
 */
public class Controller {
    
    public static String loadFile(String name, String fileType){
        JFileChooser fileDialog = new JFileChooser();
        fileDialog.setFileFilter(new FileNameExtensionFilter(name, fileType)); 
        int returnVal = fileDialog.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION){
            return fileDialog.getSelectedFile().getAbsolutePath();
        }
        return "No File Selected";
        }
    
}

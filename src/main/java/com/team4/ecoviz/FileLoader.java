/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 *
 * @author yashkir
 */
public class FileLoader {
    //elevation parameters
    private static int elvDimX;
    private static int elvDimY;
    private static double elvGridSpacing;
    private static double elvLatitude;
    private static double[][] elevations;

    //species parameters
    private static String[][] species;

    //plant parameters


    public static void readFiles(String elv,String pdbCanopy,String pdbUndergrowth,String spc){
        try {
            Scanner elvScanner = new Scanner(new File(elv));
            elvDimX = elvScanner.nextInt();
            elvDimY = elvScanner.nextInt();
            elvGridSpacing = elvScanner.nextDouble();
            elvLatitude = elvScanner.nextDouble();
            elevations = new double[elvDimX][elvDimY];
            while (elvScanner.hasNext()){
                for (int y = 0;y<elvDimX;y++){
                    String[] lineArr = elvScanner.nextLine().split(" ");
                    for (int x = 0; x<elvDimY;x++){
                        elevations[y][x] =Double.valueOf(lineArr[x]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("elv file not found");
        }

        try {
            Scanner pdbCanopyScanner = new Scanner(new File(pdbCanopy));


        } catch (FileNotFoundException e) {
            System.out.println("canopy pdb file not found");
        }

        try {
            Scanner pdbUndergrowthScanner = new Scanner(new File(pdbUndergrowth));
        } catch (FileNotFoundException e) {
            System.out.println("undergrowth pdb file not found");
        }

        try {
            Scanner spcScanner = new Scanner(new File(spc));
        } catch (FileNotFoundException e) {
            System.out.println("spc file not found");
        }

        //elv


    }
}


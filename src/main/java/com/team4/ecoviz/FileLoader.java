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
    private static int dimx;
    private static int dimy;
    private static double spacing;
    private static double latitude;
    private static float[][] terrain; //float since cheaper than double

    //species parameters
    private static String[][] species;

    //plant parameters
    private static int numSpecies;


    public static void readELV(String elv){
        try {
            Scanner elvScanner = new Scanner(new File(elv));
            dimx = elvScanner.nextInt();
            dimy = elvScanner.nextInt();
            spacing = elvScanner.nextFloat();
            latitude = elvScanner.nextFloat();
            terrain = new float[dimx][dimy];
            while (elvScanner.hasNext()){
                for (int y = 0;y<dimx;y++){
                    for (int x = 0; x<dimy;x++){
                        terrain[y][x] = elvScanner.nextFloat();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("elv file not found");
        }
    }

    public static void readSPC(String spc){
        try {
            Scanner spcScanner = new Scanner(new File(spc));
            String[][] spcKey = new String[16][2];
            int junk;
            for (int i = 0; i<16; i++){          // could generalise line count
                junk = spcScanner.nextInt();
                spcKey[i][0] = spcScanner.next(); //English name
                spcKey[i][1] = spcScanner.next(); //Latin name
            }
        } catch (FileNotFoundException e) {
            System.out.println("spc file not found");
        }
    }

    public static void readPDB(String pdb, boolean canopy){
        try {
            Scanner pdbScanner = new Scanner(new File(pdb));
            numSpecies = pdbScanner.nextInt();




        } catch (FileNotFoundException e) {
            if(canopy) { System.out.println("canopy pdb file not found"); }
            else { System.out.println("undergrowth pdb file not found"); }
        }
    }

    public static void readFiles(String elv,String pdbCanopy,String pdbUndergrowth,String spc){
        try {
            Scanner elvScanner = new Scanner(new File(elv));
            dimx = elvScanner.nextInt();
            dimy = elvScanner.nextInt();
            spacing = elvScanner.nextDouble();
            latitude = elvScanner.nextDouble();
            terrain = new float[dimx][dimy];
            while (elvScanner.hasNext()){
                for (int y = 0;y<dimx;y++){
                    String[] lineArr = elvScanner.nextLine().split(" ");
                    for (int x = 0; x<dimy;x++){
                        //terrain[y][x] =Double.valueOf(lineArr[x]);
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


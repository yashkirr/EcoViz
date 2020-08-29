/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

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
    //private static String[][] species;
    public static String[][] spcKey;

    //plant parameters
    private static int numSpecies;
    public static Species[] [] species;


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
            spcKey = new String[16][2];
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
            // can use if to set color canopy vs undergrowth
            numSpecies = pdbScanner.nextInt();
            ArrayList<ArrayList<Plant> > speciesList = new ArrayList<ArrayList<Plant>>(16);
            for (int i = 0; i < numSpecies; i++){
                int ID = pdbScanner.nextInt();
                float minH = pdbScanner.nextFloat();
                float maxH = pdbScanner.nextFloat();
                float avgCanHiRatio = pdbScanner.nextFloat();
                int count = pdbScanner.nextInt();
                Species s = new Species(ID, Color.GREEN, minH, maxH, avgCanHiRatio, count);

                ArrayList<Plant> plantList = new ArrayList<Plant>();

                for (int j = 0; j < count; j++){
                    float x = pdbScanner.nextFloat();
                    float y = pdbScanner.nextFloat();
                    float z = pdbScanner.nextFloat();
                    Vector v = new Vector();
                    v.add(x);
                    v.add(y);
                    v.add(z);
                    float height = pdbScanner.nextFloat();
                    float radToHi = pdbScanner.nextFloat();
                    Plant plant = new Plant(v, height, radToHi);
                    plantList.add(plant);
                }
                speciesList.add(plantList);
            }



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


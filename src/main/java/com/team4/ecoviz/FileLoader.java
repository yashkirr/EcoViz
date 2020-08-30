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
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
<<<<<<< HEAD
=======
import java.util.regex.Matcher;
import java.util.regex.Pattern;
>>>>>>> master

/**
 *
 * @author yashkir
 */
public class FileLoader {
    //elevation parameters
    private static int dimx;
    private static int dimy;
    private static float spacing;
    private static float latitude;
    private static float[][] terrain; //float since cheaper than double

    //species parameters
    private static Species[] speciesCan;  //used in pdb
    private static Species[] speciesUnder;  //used in pdb
    public static String[][] spcKey;

    //plant parameters
    private static int numSpeciesCan;
    private static int numSpeciesUnder;
    private static ArrayList<ArrayList<Plant>> speciesListCan;
    private static ArrayList<ArrayList<Plant>> speciesListUnder;


    public static void readELV(String elv){
        Locale.setDefault(new Locale("en", "US")); // decimal should be point not comma
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
            String junk;
            for (int i = 0; i<16; i++) {          // could generalise line count
                //junk = spcScanner.next();
                //System.out.println(junk);
                String[] line = spcScanner.nextLine().split("“");
                spcKey[i][0] = line[1].split("”")[0]; //English name
                System.out.println(spcKey[i][0]);
                spcKey[i][1] = line[2].split("”")[0]; //Latin name
                System.out.println(spcKey[i][1]);
            }
        } catch (FileNotFoundException e) {
            System.out.println("spc file not found");
        }
    }

    public static void readPdbCan(String pdb){ // set canopy boolean?
        Locale.setDefault(new Locale("en", "US")); // decimal should be point not comma
        try {
            Scanner pdbScanner = new Scanner(new File(pdb));
            // can set color canopy
            numSpeciesCan = pdbScanner.nextInt();
            speciesCan = new Species[numSpeciesCan];

            // create nested arraylist: Species > Plants

            speciesListCan = new ArrayList<ArrayList<Plant>>(16);
            for (int i = 0; i < numSpeciesCan; i++){
                // species attributes
                int ID = pdbScanner.nextInt();
                float minH = pdbScanner.nextFloat();
                float maxH = pdbScanner.nextFloat();
                float avgCanHiRatio = pdbScanner.nextFloat();
                int count = pdbScanner.nextInt();

                // create species object and populate species array
                Species s = new Species(ID, Color.GREEN, minH, maxH, avgCanHiRatio, count);
                speciesCan[i] = s;  //NB this identifies which species each speciesList object is.

                // create list of plants of species type to insert into speciesList
                ArrayList<Plant> plantList = new ArrayList<Plant>();
                for (int j = 0; j < count; j++){
                    // plant attributes
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
                speciesListCan.add(plantList);
            }
        } catch (FileNotFoundException e) {
            System.out.println("canopy pdb file not found");
        }
    }


    public static void readPdbUnder(String pdb){
        Locale.setDefault(new Locale("en", "US")); // decimal should be point not comma
        try {
            Scanner pdbScanner = new Scanner(new File(pdb));
            // can set color canopy
            numSpeciesUnder = pdbScanner.nextInt();
            speciesUnder = new Species[numSpeciesUnder];

            // create nested arraylist: Species > Plants

            speciesListUnder = new ArrayList<ArrayList<Plant>>(16);
            for (int i = 0; i < numSpeciesUnder; i++){
                // species attributes
                int ID = pdbScanner.nextInt();
                float minH = pdbScanner.nextFloat();
                float maxH = pdbScanner.nextFloat();
                float avgCanHiRatio = pdbScanner.nextFloat();
                int count = pdbScanner.nextInt();

                // create species object and populate species array
                Species s = new Species(ID, Color.GREEN, minH, maxH, avgCanHiRatio, count);
                speciesUnder[i] = s;  //NB this identifies which species each speciesList object is.

                // create list of plants of species type to insert into speciesList
                ArrayList<Plant> plantList = new ArrayList<Plant>();
                for (int j = 0; j < count; j++){
                    // plant attributes
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
                speciesListUnder.add(plantList);
            }
        } catch (FileNotFoundException e) {
            System.out.println("undergrowth pdb file not found");
        }
    }


    /**
<<<<<<< HEAD
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


     }*/
=======
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


    }*/
>>>>>>> master
}


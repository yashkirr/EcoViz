/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;


/**
 *Class to handle storage of data from inputed file names into different data structures.
 * @author yashkir
 */
public class FileLoader {
    /**instance variables*/
    //elevation parameters
    private static int dimx;
    private static int dimy;
    private static float spacing;
    private static float latitude;
    private static float[][] terrain; //float since cheaper than double

    //species parameters
    private static Species[] speciesCan;  //used in pdb
    private static Species[] speciesUnder;  //used in pdb
    private static String[][] spcKey;

    //plant parameters
    private static int numSpeciesCan;
    private static int numSpeciesUnder;
    private static ArrayList<ArrayList<Plant>> speciesListCan;
    private static ArrayList<ArrayList<Plant>> speciesListUnder;


    /** @author Victor Bantchovski
     * Takes in an .elv file and reads the formatted data in the file into data structures.
     * @param elv
     */
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

    /**
     * @author Victor Bantchovski
     * @param spc
     * Takes in .spc file format and reads in data from the file into different data structures.
     */
    public static void readSPC(String spc){
        try {
            Scanner spcScanner = new Scanner(new File(spc));
            setSpcKey(new String[16][2]);
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

    /**
     * @author Victor Bantchovski
     * Takes in canopy .pdb file format and reads in the formatted data into data structures.
     * @param pdb
     */
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


    /**
     * @author Victor Bantchovski
     * Takes in undergrowth .pdb file format and reads data into data structures.
     * @param pdb
     */
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
     * @author Yashkir Ramsamy
     * @param path
     * @return String[][]
     * retrieves the species information stored in the .spc file given the file path
     */
    public static String[][] getSpcKey(String path) {
        readSPC(path);
        return spcKey;
    }

    /**
     * @author Yashkir Ramsamy
     * @param spcKey
     * updates the species information to the user inputted species information
     */
    public static void setSpcKey(String[][] spcKey) {
        FileLoader.spcKey = spcKey;
    }
}


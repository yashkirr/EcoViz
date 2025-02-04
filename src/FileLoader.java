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
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.VolatileImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;
import java.util.Vector;
import java.util.*;


/**
 *Class to handle storage of data from input file names into different data structures.
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
    private static Color[] spcColor;
    private static BufferedImage[] spcCircle;

    //plant parameters
    private static int numSpeciesCan;
    private static int numSpeciesUnder;
    private static ArrayList<ArrayList<Plant>> speciesListCan;
    private static ArrayList<Plant> speciesListCan1D;

    private static ArrayList<ArrayList<Plant>> speciesListUnder;
    private static ArrayList<Plant> speciesListUnder1D;
    private static double minElevation;
    private static double maxElevation;


    public static ArrayList<ArrayList<Plant>> getCanopy(){ return speciesListCan;}
    public static ArrayList<ArrayList<Plant>> getUnder(){ return speciesListUnder;}

    //filter parameters
    private static float minPlantHeightUndergrowth;
    private static float minPlantHeightCanopy;
    private static float maxPlantHeightUndergrowth;
    private static float maxPlantHeightCanopy;
    private static boolean[] spcDraw;
    private static boolean[] canDraw;
    private static boolean[] underDraw;

    //Userview width for plant specs
    private static int pnlWidth = UserView.pnlVizualizer.getWidth();
    private static int pnlHeight = UserView.pnlVizualizer.getHeight();

    //Find plants
    private static ArrayList<Plant>[][] finder;

    //Plant stats
    static float min = 10;
    static float max =0;
    static int totalCan = 0;
    static int totalUnder = 0;


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
            BlockGrid.setBlockDIm(dimx,dimy);
            Fire.dimX = dimx;
            Fire.dimY = dimy;
            finder = new ArrayList[(int)(dimx*spacing*10)][(int)spacing*dimy*10];
            maxElevation = -Double.MAX_VALUE;
            minElevation = Double.MAX_VALUE;
            while (elvScanner.hasNext()){
                for (int y = 0;y<dimx;y++){
                    for (int x = 0; x<dimy;x++){
                        terrain[y][x] = elvScanner.nextFloat();
                        BlockGrid.getGrid()[x][y] = new Block(terrain[y][x], x, y);
                        if(terrain[y][x]>maxElevation){
                            maxElevation=terrain[y][x];
                        }
                        if (terrain[y][x]<minElevation){
                            minElevation=terrain[y][x];
                        }
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
            int count = 0;
            while (spcScanner.hasNext()){
                count++;
                spcScanner.nextLine();
            }

            spcScanner = new Scanner(new File(spc));
            setSpcKey(new String[count][2]);
            spcDraw = new boolean[count];
            spcCircle = new BufferedImage[count];
            String junk;
            int i=0;
            //for (int i = 0; i<16; i++) {          // could generalise line count
            while(spcScanner.hasNext()){
                //junk = spcScanner.next();
                //System.out.println(junk);
                String[] line = spcScanner.nextLine().split("“");
                spcKey[i][0] = line[1].split("”")[0]; //English name
                spcKey[i][1] = line[2].split("”")[0]; //Latin name
                spcDraw[i]=true;
                i++;
            }
            //Set colours
            spcColor = new Color[i+1];
            int j = 0; //counter
            for(int B = 2; B>=0; B--){
                for(int G=4; G>=1; G--){
                    for(int R = 2; R>=0; R--){
                        spcColor[j] = new Color(R*70,G*60,B*80,170);
                        spcCircle[j] = drawIMG(new Color(R*70,G*60,B*80,170));
                        j++;
                        if(j==i){ return;};
                    }
                }
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
            canDraw = new boolean[numSpeciesCan];

            // create nested arraylist: Species > Plants

            speciesListCan = new ArrayList<ArrayList<Plant>>(16);
            for (int i = 0; i < numSpeciesCan; i++){
                // species attributes
                int ID = pdbScanner.nextInt();
                float minH = pdbScanner.nextFloat();
                float maxH = pdbScanner.nextFloat();
                minPlantHeightCanopy = minH;
                maxPlantHeightCanopy = maxH;
                float avgCanHiRatio = pdbScanner.nextFloat();
                int count = pdbScanner.nextInt();

                // create species object and populate species array
                Species s = new Species(ID, Color.GREEN, minH, maxH, avgCanHiRatio, count);
                speciesCan[i] = s;  //NB this identifies which species each speciesList object is.
                //canDraw[s.getID()] = true;

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
                    float radius = pdbScanner.nextFloat();
                    Plant plant = new Plant(v, height, radius, dimx, dimy, spacing, pnlWidth, pnlHeight);
                    //plant.setColor(spcColor[i]);
                    plant.setType("Canopy");
                    plant.setID(s.getID());
                    plant.setEnglishName(spcKey[plant.getID()][0]);
                    plant.setLatinName(spcKey[plant.getID()][1]);
                    plantList.add(plant);
                    int X = Math.round(x);
                    int Y = Math.round(y);
                    int r = (int)Math.ceil(radius/spacing);
                    for(int t = 1-r; t<r && t<pnlWidth; t++){
                        for(int n = 1-r; n<r && t<pnlHeight; n++){
                            if(t>=0 && t<pnlWidth && n>=0 && n<pnlHeight) {
                                if (n + t >= r && n * n + t * t < r * r) {
                                    BlockGrid.getBlock(t+X, n+Y).canopy.add(plant);
                                } else if (n + t < r) {
                                    BlockGrid.getBlock(t+X, n+Y).canopy.add(plant);
                                }
                            }
                        }
                    }
                    //Grid.getBlock(Math.round(x), Math.round(y)).canopy.add(plant);
                    totalCan++;
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
            //underDraw = new boolean[numSpeciesUnder];
            // create nested arraylist: Species > Plants

            speciesListUnder = new ArrayList<ArrayList<Plant>>(16);
            for (int i = 0; i < numSpeciesUnder; i++){
                // species attributes
                int ID = pdbScanner.nextInt();
                float minH = pdbScanner.nextFloat();
                float maxH = pdbScanner.nextFloat();
                minPlantHeightUndergrowth = minH;
                maxPlantHeightUndergrowth = maxH;
                float avgCanHiRatio = pdbScanner.nextFloat();
                int count = pdbScanner.nextInt();

                // create species object and populate species array
                Species s = new Species(ID, Color.GREEN, minH, maxH, avgCanHiRatio, count);
                speciesUnder[i] = s;  //NB this identifies which species each speciesList object is.
                //underDraw[s.getID()] = true;

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
                    float radius = pdbScanner.nextFloat();
                    Plant plant = new Plant(v, height, radius, dimx, dimy, spacing, pnlWidth, pnlHeight);
                    //plant.setColor(spcColor[i]);
                    plant.setType("Undergrowth");
                    plant.setID(s.getID());
                    plant.setEnglishName(spcKey[plant.getID()][0]);
                    plant.setLatinName(spcKey[plant.getID()][1]);
                    plantList.add(plant);
                    int X = Math.round(x);
                    int Y = Math.round(y);
                    int r = (int)Math.ceil(radius/spacing);
                    for(int t = 1-r; t<r && t<pnlWidth; t++){
                        for(int n = 1-r; n<r && t<pnlHeight; n++){
                            if(t>=0 && t<pnlWidth && n>=0 && n<pnlHeight) {
                                if (n + t >= r && n * n + t * t < r * r) {
                                    BlockGrid.getBlock(t+X, n+Y).undergrowth.add(plant);
                                } else if (n + t < r) {
                                    BlockGrid.getBlock(t+X, n+Y).undergrowth.add(plant);
                                }
                            }
                        }
                    }
                    //Grid.getBlock(Math.round(x), Math.round(y)).undergrowth.add(plant);
                    totalUnder++;
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
    public static Species[] getSpeciesCan(){return speciesCan;}
    public static  Species[] getSpeciesUnder(){return speciesUnder;}
    public static boolean[] getSpcDraw(){ return  spcDraw;}
    public static void setSpcDraw(int i, boolean bool){ spcDraw[i]=bool;}
    public static String[][] getSpcKey(){
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

    /**
     * Gets name of files in directory
     * @Author Yashkir Ramsamy
     * @param folder
     * @return ArrayList<String>
     */
    public static ArrayList<String> listFileNamesInDirectory(final File folder) {
        ArrayList<String> fileList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFileNamesInDirectory(fileEntry);
            } else {
                fileList.add(fileEntry.getName());
            }
        }

        return fileList;
    }

    /**
     * Gets path of files in directory
     * @Author Yashkir Ramsamy
     * @param folder
     * @return ArrayList<String>
     */
    public static ArrayList<String> listFilePathsInDirectory(final File folder) {
        ArrayList<String> fileList = new ArrayList<>();
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilePathsInDirectory(fileEntry);
            } else {
                fileList.add(fileEntry.getAbsolutePath());
            }
        }

        return fileList;
    }

    public static void savePDB() throws NullPointerException{
        if(!UserView.localController.getInit()) return;
        else {
            String sizeX = Integer.toString(dimx);
            String sizeY = Integer.toString(dimy);
            String filename = sizeX + "x" + sizeY + "_EcoViz_Changes";
            try {
                PrintWriter save = new PrintWriter(filename + "_Canopy.pdb");
                save.println(FileLoader.getNumCanopy());
                int i = 0;
                for (ArrayList<Plant> list : speciesListCan) {
                    int ID = list.get(0).getID();
                    String id = Integer.toString(ID);
                    String min = Float.toString(FileLoader.getMinCanopyHeight(i));
                    String max = Float.toString(FileLoader.getMaxCanopyHeight(i));
                    String avgHR = Integer.toString(0);
                    save.println(id + " " + min + " " + max + " " + avgHR);
                    save.println(Integer.toString(list.size()));
                    i++;
                    for (Plant plant : list) {
                        Vector<Float> pos = plant.getPos();
                        String x = Float.toString(pos.get(0));
                        String y = Float.toString(pos.get(1));
                        String z = Float.toString(pos.get(2));
                        String h = Float.toString(plant.getHeight());
                        String r = Float.toString(plant.getCanopyRadius());
                        save.println(x + " " + y + " " + z + " " + h + " " + r);
                    }
                }
                save.close();
            } catch (FileNotFoundException e) {
            }
            try {
                PrintWriter save = new PrintWriter(filename + "_Undergrowth.pdb");
                save.println(FileLoader.getNumUndergrowth());
                int i = 0;
                for (ArrayList<Plant> list : speciesListUnder) {
                    int ID = list.get(0).getID();
                    String id = Integer.toString(ID);
                    String min = Float.toString(FileLoader.getMinUndergrowthHeight(i));
                    String max = Float.toString(FileLoader.getMaxUndergrowthHeight(i));
                    String avgHR = Integer.toString(0);
                    save.println(id + " " + min + " " + max + " " + avgHR);
                    save.println(Integer.toString(list.size()));
                    i++;
                    for (Plant plant : list) {
                        Vector<Float> pos = plant.getPos();
                        String x = Float.toString(pos.get(0));
                        String y = Float.toString(pos.get(1));
                        String z = Float.toString(pos.get(2));
                        String h = Float.toString(plant.getHeight());
                        String r = Float.toString(plant.getCanopyRadius());
                        save.println(x + " " + y + " " + z + " " + h + " " + r);
                    }
                }
                save.close();
            } catch (FileNotFoundException e) {
            }
        }
    }

    public static float getMaxPlantHeight(){
        return Math.max(maxPlantHeightCanopy,maxPlantHeightUndergrowth);
    }

    public static float getMinPlantHeight(){
        return Math.min(minPlantHeightCanopy,minPlantHeightUndergrowth);
    }

    public static float getMaxCanopyHeight(int i) { return speciesCan[i].getMax();}

    public static float getMinCanopyHeight(int i) { return speciesCan[i].getMin();}

    public static float getMaxUndergrowthHeight(int i) { return speciesUnder[i].getMax();}

    public static float getMinUndergrowthHeight(int i) { return speciesUnder[i].getMin();}

    public static int getDimx(){ return dimx;}

    public static int getDimy(){return dimy;}

    public static float getSpacing(){return spacing;}

    public static float getLatitude(){return latitude;}

    public static float[][] getTerrain(){return terrain;}

    public static ArrayList<Plant> getSpeciesListCan1D(){
        return speciesListCan1D;
    }

    public static ArrayList<Plant> getSpeciesListUnder1D(){
        return speciesListUnder1D;
    }

    public static ArrayList<ArrayList<Plant>> getSpeciesListCan(){
        return speciesListCan;
    }

    public static ArrayList<ArrayList<Plant>> getSpeciesListUnder(){
        return speciesListUnder;
    }

    public static int getNumCanopy(){ return numSpeciesCan;}

    public static int getNumUndergrowth(){ return numSpeciesUnder;}




    public static void convertTo1DCan(){
        speciesListCan1D = new ArrayList<Plant>();
        Iterator i = speciesListCan.iterator();
        Iterator j;

        int count1 = 0;
        int count2 = 0;

        while (i.hasNext()){
            j = speciesListCan.get(count1).iterator();
            while(j.hasNext()){
                speciesListCan1D.add(speciesListCan.get(count1).get(count2));

                j.next();
                count2++;
            }

            i.next();
            count1++;
            count2 = 0;
        }
    }

    public static void convertTo1DUnder(){
        speciesListUnder1D = new ArrayList<Plant>();
        Iterator i = speciesListUnder.iterator();
        Iterator j;

        int count1 = 0;
        int count2 = 0;

        while (i.hasNext()){
            j = speciesListUnder.get(count1).iterator();
            while(j.hasNext()){
                speciesListUnder1D.add(speciesListUnder.get(count1).get(count2));
                count2++;
                j.next();
            }
            count1++;
            i.next();
            count2 = 0;
        }
    }
    public static Color[] getSpcColor(){
        return spcColor;
    }

    public static int n=500;
    private static boolean first = true;
    public static BufferedImage drawIMG(Color col){
        BufferedImage img = new BufferedImage(n, n, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gz = img.createGraphics();
        //Color col = new Color(rgb);//,true);
        gz.setColor(col);
        gz.fillOval(0,0,n,n);
        gz.dispose();
        return img;
    }
    public static BufferedImage getIMG(int i){ return spcCircle[i];}

    public static float getMinCanopyRadius(){
        Iterator i = speciesListCan1D.iterator();
        int count = 0;
        float min = 100000;
        while (i.hasNext()){
            Plant plant = speciesListCan1D.get(count);
            if (plant.getCanopyRadius()<min){
                min = plant.getCanopyRadius();
            }
            i.next();
            count++;
        }

        i = speciesListUnder1D.iterator();
        count = 0;
        float minUnder = 10000;
        while (i.hasNext()){
            Plant plant = speciesListUnder1D.get(count);
            if (plant.getCanopyRadius()<minUnder){
                minUnder = plant.getCanopyRadius();
            }
            i.next();
            count++;
        }

        return Math.min(min,minUnder);
    }

    public static float getMaxCanopyRadius(){
        Iterator i = speciesListCan1D.iterator();
        int count = 0;
        float max = -100000;
        while (i.hasNext()){
            Plant plant = speciesListCan1D.get(count);
            if (plant.getCanopyRadius()>max){
                max = plant.getCanopyRadius();
            }
            i.next();
            count++;
        }

        i = speciesListUnder1D.iterator();
        count = 0;
        float maxUnder = -10000;
        while (i.hasNext()){
            Plant plant = speciesListUnder1D.get(count);
            if (plant.getCanopyRadius()>maxUnder){
                maxUnder = plant.getCanopyRadius();
            }
            i.next();
            count++;
        }

        return Math.max(max,maxUnder);
    }

    public static double getMinElevation() {
        return minElevation;
    }

    public static double getMaxElevation() {
        return maxElevation;
    }
}




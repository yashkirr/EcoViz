/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.util.HashMap;

/**
 * Class to store details of species information specified in the .spc.txt input file
 * @author yashkir
 */
public class Species {
    private int ID;
    private Color color;
    private float minHeight;
    private float maxHeight;
    private double avgCanopyHeightRatio;
    private int count;

    /**
    * default constructor
    */
    public Species(){
        ID=0;
        count = 0;
        color = null;
        minHeight=0;
        maxHeight=0;
        avgCanopyHeightRatio = 0.0;

    }

    /**
    * Constructor - initialises all variables to variables specified by the argument
    */
    public Species(int ID, Color color, float minHeight, float maxHeight, float avgCanopyHeightRatio, int count) {
        this.ID = ID;
        this.color = color;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.avgCanopyHeightRatio = avgCanopyHeightRatio;
        this.count = count;
    }

    public int getID(){
        return this.ID;
    }
    public int setID(int ID){
        this.ID = ID;
        return ID;
    }
    /**
    *Stores all species details in a hash map and returns the resulting hash map
    */
    public HashMap<String,Object> speciesDetail(){
        HashMap<String,Object> mapDetail = new HashMap<>();
        mapDetail.put("ID",ID);
        mapDetail.put("Color",color);
        mapDetail.put("minHeight",minHeight);
        mapDetail.put("maxHeight",maxHeight);
        mapDetail.put("avgCanopyHeightRatio",avgCanopyHeightRatio);
        mapDetail.put("count",count);

        return mapDetail;

    }
    public float getMax(){return maxHeight;}

    public float getMin(){ return minHeight;}

}

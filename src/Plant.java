/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class to store information on each plant to be displayed in the ecosystem visualiser
 * @author yashkir
 */
public class Plant extends Species {
    private float height;
    private float age;
    private float canopyRadius;
    private float radToHi;
    private Vector<Float> pos;     //vs separate xyz
    private String type;
    private boolean isBurnt;
    private Ellipse2D circle;
    private Color color;
    private int rectX;
    private int rectY;
    private boolean draw = true;

    /**
    *default constructor
    */
    public Plant() {
        this.height = 0;
        this.age = 0;
        this.canopyRadius = 0;
        this.pos = null;
        this.type = "No type";
        this.isBurnt = false;
        circle = null;
        color = null;
    }

    /**
    * Constructor - initialises all variables to variables specified by the argument
    */
    public Plant(Vector v, float height, float radToHi, int dimx, int dimy, float spacing, int pnlWidth, int pnlHeight) {
        this.height = height;
        this.age = 0;
        this.radToHi = radToHi;
        this.pos = v;
        this.color = null;
        this.type = "No type";
        this.isBurnt = false;
        this.rectX = Math.round(this.pos.get(0)*pnlWidth/(dimx*spacing));
        this.rectY = Math.round(this.pos.get(1)*pnlHeight/(dimy*spacing));
        this.circle = new Ellipse2D.Float();
        circle.setFrame(this.rectX, this.rectY,5,5);
    }
    // retrieve Rect values
    public int getRectX(){ return this.rectX;}
    public int getRectY(){ return this.rectY;}

    public void setDrawStat(boolean draw){this.draw=draw;}
    public boolean getDrawStat(){return this.draw;}

    public Ellipse2D getShape(){return this.circle;}

    /**
    * Constructor - initialises all variables to variables specified by the argument
    */
    public Plant(float height, float age, float canopyRadius, Vector location, String type, boolean isBurnt) {
        this.height = height;
        this.age = age;
        this.canopyRadius = canopyRadius;
        this.pos = location;
        this.type = type;
        this.isBurnt = isBurnt;
        this.circle = new Ellipse2D.Float();
        circle.setFrame((float) pos.get(0),(float) pos.get(1),2,2);
        this.color = null;
    }


    /**
    * Constructor - initialises all variables to variables specified by the argument
    */
    public Plant(float height, float age, float canopyRadius, Vector location,
                 String type, boolean isBurnt, int ID, Color color, float minHeight,
                 float maxHeight, float avgCanopyHeightRatio, int count) {
        super(ID, color, minHeight, maxHeight, avgCanopyHeightRatio, count);
        this.height = height;
        this.age = age;
        this.canopyRadius = canopyRadius;
        this.pos = location;
        this.type = type;
        this.isBurnt = isBurnt;
        this.circle = new Ellipse2D.Float();
        circle.setFrame((float) pos.get(0),(float) pos.get(1),2,2);
        this.color = null;
    }

    /**
    *fetches position of the plant object and returns it.
    */
    public Vector getPos(){
        return this.pos;
    }

    /**
    *not yet implemented - changes position of the plant object to the coordinates specified as parameters
    */
    public void setPos(float x, float y, float z){
        //perhaps?
    }

    /**
    *Stores plant details in a hash map and returns the hash map
    */
    public HashMap<String,Object> detail(){
        HashMap<String,Object> plantDetail = new HashMap<String,Object>();
        plantDetail.put("height", height);
        plantDetail.put("age", age);
        plantDetail.put("canopyRadius", canopyRadius);
        plantDetail.put("location",pos);
        plantDetail.put("type",type);
        plantDetail.put("isBurnt", isBurnt);
        plantDetail.put("Species Details", super.speciesDetail());
        return plantDetail;
    }

    public void setColor(int rgb){
        this.color = new Color(rgb, true);
    }

    public Color getColor(){
        return color;
    }

    public float getHeight(){
        return height;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Vector;

/**
 * Class to store information on each plant to be displayed in the ecosystem visualiser
 * @author yashkir
 */
public class Plant extends Species {
    private float height;
    private int dimx;
    private int dimy;
    private float spacing;
    private float age;
    private float canopyRadius;
    private float radToHi;
    private Vector<Float> pos;     //vs separate xyz
    private String type;
    private boolean isBurnt;
    private Ellipse2D circle;
    private Color color;
    private float rectX;
    private float rectY;
    private float rectRad; // for drawing, jpanel radius
    private boolean draw = true;
    public AffineTransform at;

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
    public Plant(Vector v, float height, float radius, int dimx, int dimy, float spacing, int pnlWidth, int pnlHeight) {
        this.height = height;
        this.dimx = dimx;
        this.dimy = dimy;
        this.spacing = spacing;
        this.age = 0;
        this.canopyRadius = radius;
        this.pos = v;
        this.color = null;
        this.type = "No type";
        this.isBurnt = false;
        this.rectRad = this.canopyRadius*pnlWidth/(dimx*spacing);
        this.rectX = this.pos.get(0)*pnlWidth/(dimx*spacing) - rectRad;
        this.rectY = this.pos.get(1)*pnlHeight/(dimy*spacing) - rectRad;
        this.circle = new Ellipse2D.Float();
        circle.setFrame(this.rectX, this.rectY,rectRad*2,rectRad*2);
        float[] f = {this.getRad()*2/FileLoader.n, 0, 0, this.getRad()*2/FileLoader.n, this.getRectX(), this.getRectY()};
        this.at = new AffineTransform(f);
    }

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
     * Used for updating the visual position of drawn plant on visualizer panel upon form resize
     * @author Yashkir Ramsamy
     * @param pnlWidth
     * @param pnlHeight
     */
    public void updateVisualPosition(int pnlWidth,int pnlHeight){
        this.rectX = this.pos.get(0)*pnlWidth/(dimx*spacing) - rectRad;
        this.rectY = this.pos.get(1)*pnlHeight/(dimy*spacing) - rectRad;
        this.circle = new Ellipse2D.Float();
        circle.setFrame(this.rectX, this.rectY,rectRad*2,rectRad*2);
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
        return plantDetail;
    }
    // retrieve Rect values
    public float getRectX(){ return this.rectX;}
    public float getRectY(){ return this.rectY;}

    public void setDrawStat(boolean draw){this.draw=draw;}
    public boolean getDrawStat(){return this.draw;}

    public Ellipse2D getShape(){return this.circle;}
    public float getRad(){ return rectRad;}

    public Point getPoint(){
        return new Point(Math.round(rectX),Math.round(rectY));
    }

    public void setColor(Color rgb){
        this.color = rgb;
    }

    public Color getColor(){
        return color;
    }

    public float getHeight(){
        return height;
    }

    public float getDiameter(){ return this.canopyRadius*2;}
}

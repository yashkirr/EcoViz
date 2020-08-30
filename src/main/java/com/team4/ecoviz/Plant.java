/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import java.awt.*;
import java.util.HashMap;
import java.util.Vector;

/**
 *
 * @author yashkir
 */
public class Plant extends Species {
    private float height;
    private float age;
    private float canopyRadius;
    private float radToHi;
    private Vector pos;     //vs separate xyz
    private String type;
    private boolean isBurnt;

    public Plant() {
        this.height = 0;
        this.age = 0;
        this.canopyRadius = 0;
        this.pos = null;
<<<<<<< HEAD
        this.type = "No type";
        this.isBurnt = false;
    }

    public Plant(Vector v, float height, float radToHi) {
        this.height = height;
        this.age = 0;
        this.radToHi = radToHi;
        this.pos = v;
=======
>>>>>>> master
        this.type = "No type";
        this.isBurnt = false;
    }

<<<<<<< HEAD
=======
    public Plant(Vector v, float height, float radToHi) {
        this.height = height;
        this.age = 0;
        this.radToHi = radToHi;
        this.pos = v;
        this.type = "No type";
        this.isBurnt = false;
    }

>>>>>>> master
    public Plant(float height, float age, float canopyRadius, Vector location, String type, boolean isBurnt) {
        this.height = height;
        this.age = age;
        this.canopyRadius = canopyRadius;
        this.pos = location;
        this.type = type;
        this.isBurnt = isBurnt;
    }

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
    }

    public Vector getPos(){
        return this.pos;
    }

    public void setPos(float x, float y, float z){
        //perhaps?
    }
<<<<<<< HEAD

=======
    
>>>>>>> master
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

}

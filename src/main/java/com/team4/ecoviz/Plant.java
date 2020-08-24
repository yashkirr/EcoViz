/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import java.awt.Color;
import java.util.Vector;
import java.util.HashMap;

/**
 *
 * @author yashkir
 */
public class Plant extends Species {
    private double height;
    private double age;
    private double canopyRadius;
    private Vector location;
    private String type;
    private boolean isBurnt;

    public Plant() {
        this.height = 0;
        this.age = 0;
        this.canopyRadius = 0;
        this.location = null;
        this.type = "No type";
        this.isBurnt = false;
    }

    public Plant(double height, double age, double canopyRadius, Vector location, String type, boolean isBurnt) {
        this.height = height;
        this.age = age;
        this.canopyRadius = canopyRadius;
        this.location = location;
        this.type = type;
        this.isBurnt = isBurnt;
    }

    public Plant(double height, double age, double canopyRadius, Vector location, String type, boolean isBurnt, int ID, Color color, float minHeight, float maxHeight, float avgCanopyHeightRatio, int count) {
        super(ID, color, minHeight, maxHeight, avgCanopyHeightRatio, count);
        this.height = height;
        this.age = age;
        this.canopyRadius = canopyRadius;
        this.location = location;
        this.type = type;
        this.isBurnt = isBurnt;
    }
    
    public HashMap<String,Object> detail(){
        HashMap<String,Object> plantDetail = new HashMap<String,Object>();
        plantDetail.put("height", height);
        plantDetail.put("age", age);
        plantDetail.put("canopyRadius", canopyRadius);
        plantDetail.put("location",location);
        plantDetail.put("type",type);
        plantDetail.put("isBurnt", isBurnt);
        plantDetail.put("Species Details", super.speciesDetail());
        return plantDetail;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.team4.ecoviz;

import java.awt.Color;
import java.util.HashMap;

/**
 *
 * @author yashkir
 */
public class Species {
    private int ID;
    private Color color;
    private double minHeight;
    private double maxHeight;
    private double avgCanopyHeightRatio;
    private int count;
    
    public Species(){
        ID=0;
        count = 0;
        color = null;
        minHeight=0.0;
        maxHeight=0.0;
        avgCanopyHeightRatio = 0.0;
        
    }

    public Species(int ID, Color color, float minHeight, float maxHeight, float avgCanopyHeightRatio, int count) {
        this.ID = ID;
        this.color = color;
        this.minHeight = minHeight;
        this.maxHeight = maxHeight;
        this.avgCanopyHeightRatio = avgCanopyHeightRatio;
        this.count = count;
    }
    
    public HashMap<String,Object> speciesDetail(){
        HashMap<String,Object> mapDetail = new HashMap<String,Object>();
        mapDetail.put("ID",ID);
        mapDetail.put("Color",color);
        mapDetail.put("minHeight",minHeight);
        mapDetail.put("maxHeight",maxHeight);
        mapDetail.put("avgCanopyHeightRatio",avgCanopyHeightRatio);
        mapDetail.put("count",count);
        
        return mapDetail;
        
    }
    
}

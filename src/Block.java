import java.awt.*;
import java.util.ArrayList;

public class Block {
    float elv;
    ArrayList<Plant> canopy = new ArrayList<>();
    ArrayList<Plant> undergrowth = new ArrayList<>();
    float foliage=0;
    float intensity=0;
    float windSlope=0;
    float slope = 0;
    int x;
    int y;
    volatile boolean unburnt = true;
    volatile boolean burning = false;
    volatile boolean burnt = false;
    static Block[][] grid;
    public int fuel;

    Block(float elv, int x, int y){
        this.elv = elv;
        this.x = x;
        this.y = y;
    }

    public void setToRed(){
        for(Plant plant: canopy){
            plant.burnt = true;
        }
        for(Plant plant: undergrowth){
            plant.burnt = true;
        }
    }

    public boolean alight(){
        if(!unburnt || foliage==0){return false;}
        double a = Math.random()*20;
        float chance = (windSlope*foliage);// + intensity/4);
        intensity++;
        return (a<chance);
    }
}

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

    Block(float elv, int x, int y){
        this.elv = elv;
        this.x = x;
        this.y = y;
    }

    public void setToRed(){
        for(Plant plant: canopy){
            plant.setColor(Color.red);
        }
        for(Plant plant: undergrowth){
            plant.setColor(Color.red);
        }
    }

    public float chance(){
        this.foliage = canopy.size()*2 + undergrowth.size();
        return 1;
    }
}

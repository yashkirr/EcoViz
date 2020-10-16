import java.util.ArrayList;

public class Block {
    float elv;
    ArrayList<Plant> canopy = new ArrayList<>();
    ArrayList<Plant> undergrowth = new ArrayList<>();
    float foliage=0;
    float intensity=0;
    float openAir=1;
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
}

import java.util.ArrayList;

public class Block {
    float elv;
    ArrayList<Plant> canopy = new ArrayList<>();
    ArrayList<Plant> undergrowth = new ArrayList<>();
    int x;
    int y;
    boolean unburnt = true;
    boolean burning = false;
    boolean burnt = false;

    Block(float elv, int x, int y){
        this.elv = elv;
        this.x = x;
        this.y = y;
    }
}

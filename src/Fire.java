import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;

public class Fire extends Thread{
    boolean[][] isBurning;
    static int dimX;
    static int dimY;
    static BufferedImage fireLayer;
    int windX;
    int windY;
    int startX;
    int startY;
    public static volatile boolean paused = false;
    public static volatile  boolean stopped = false;
    public static volatile boolean running = false;
    private ArrayList<Plant> undergrowth;
    private ArrayList<Plant> canopy;
    static Graphics gBurn;
    int pnlWidth;
    int pnlHeight;
    float scale;
    int scaleInt;

    ArrayList<int[]> burnt = new ArrayList<>();
    ArrayList<Block> burning = new ArrayList<>();

public Fire(int pnlWidth,int pnlHeight, int sx, int sy, int wS, int wD, ArrayList<Plant> undergrowth, ArrayList<Plant> canopy){
        windX = (int) Math.round(wS*Math.cos(wD));; ;
        windY = (int) Math.round(wS*Math.sin(wD));;
        startX = sx;
        startY = sy;
        this.undergrowth = undergrowth;
        this.canopy = canopy;
        isBurning = new boolean[dimX][dimY];
        for (int x = 0; x<dimX;x++){
            for (int y = 0; y<dimY;y++){
                setFire(x,y,false);
            }
        }
        fireLayer = new BufferedImage(pnlWidth,pnlHeight,BufferedImage.TYPE_INT_ARGB);
        paused = true;
        stopped = false;
        this.pnlWidth = pnlWidth;
        this.pnlHeight = pnlHeight;
        scale = (float)pnlWidth/(float)dimX;
        scaleInt = (int)Math.ceil(scale);
    }

    public void setFire(int x, int y, boolean burning){
        isBurning[x][y] = burning;
    }

    public int getDimX(){
        return dimX;
    }

    public int getDimY(){
        return dimY;
    }

    public BufferedImage getImage(){
        for (int x = 0; x<dimX; x++){
            for (int y=0; y<dimY; y++){
                Color color;
                if (isBurning[x][y]==true){
                    color = new Color(1.0f,0.0f,0.0f,0.7f);
                }
                else{
                    color = new Color(0.0f,0.0f,0.0f,0.0f);
                }
                fireLayer.setRGB(x,y,color.getRGB());
            }
        }
        return fireLayer;
    }

    public boolean getIsBurning(int x, int y){
        return isBurning[x][y];
    }

    public void setBurningTrees(int x, int y,Graphics g){
        Iterator i = undergrowth.iterator();
        int count1 = 0;

        while (i.hasNext()){
            Plant plant = undergrowth.get(count1);
            if (plant.getCircle().contains(x,y)){
                plant.setBurnt(true);
                //g.setColor(Color.red);
                //g.fillOval(x,y,10,10);
                //UserView.localController.updateView();
            }
            else{

            }
            i.next();
            count1++;
        }

        i = canopy.iterator();
        count1 = 0;

        while (i.hasNext()){
            Plant plant = canopy.get(count1);
            if (plant.getCircle().contains(x,y)){
                plant.setBurnt(true);
                //g.setColor(Color.red);
                //g.fillOval(x,y,10,10);
                //UserView.localController.updateView();
            }
            else{

            }
            i.next();
            count1++;
        }


    }
    public BufferedImage printScreen(JPanel panel) throws AWTException {
        Point p = panel.getLocationOnScreen();
        Dimension dim = panel.getSize();
        Rectangle rect = new Rectangle(p, dim);

        Robot robot = new Robot();
        return robot.createScreenCapture(rect);
    }

    public void setStartX(int sx){
        startX = sx;
    }

    public void setWindY(int wS, int wD){
        windY = (int) -Math.round(wS*Math.cos(Math.toRadians(wD)));
        System.out.println("Speed: "+wS+" Direction "+wD+"Y vector: "+windY);
    }

    public void setWindX(int wS, int wD){
        windX = (int) Math.round(wS*Math.sin(Math.toRadians(wD)));
        System.out.println("Speed: "+wS+" Direction "+wD+"X vector: "+windX);
    }

    public void setStartY(int sy){
        startY = sy;
    }

    public void simulateOverGrid(Graphics g,Controller ctrl) throws IOException {
        try{ fireLayer = printScreen(UserView.pnlVizualizer);}
        catch(AWTException e) {}
        this.gBurn = fireLayer.createGraphics();
        int x = Math.round(startX/scale);
        int y = Math.round(startY/scale);  //x and y to correspond with grid
        burn(x,y);
//        Burn burn = new Burn(startX,startY);
//        burn.start();
    }
    public void blockBurn(int x, int y){
        Block block = BlockGrid.getBlock(x,y);
        burning.add(block);
        block.unburnt = false;
        block.setToRed();
        gBurn.fillRect(Math.round(x*scale),Math.round(y*scale),scaleInt,scaleInt);
    }
    Color red = new Color(255,0,0,150);
    Color brown = new Color(100,70,10,150);
    public void burnt(int x, int y){
        gBurn.setColor(brown);
        gBurn.fillRect(Math.round(x*scale),Math.round(y*scale),scaleInt,scaleInt);
        gBurn.setColor(red);
    }

    long start;
    long stop;
    public void burn(int x, int y){
        gBurn.setColor(red);
        BlockGrid.getBlock(x,y).fuel = 100;
        blockBurn(x,y);
        int a = 0;

        while(running){
            if(paused){
                while(paused){
                    try {this.sleep(200);}
                    catch (Exception e) {}
                }
            }
            if(stopped){
                running = false;
                stopped = false;
                UserView.localController.updateView();
                this.interrupt();
            }
            start = System.currentTimeMillis();
            ArrayList<Block> burningCache = (ArrayList<Block>)burning.clone();
            for(Block block: burningCache){
                if(block.x>0 && BlockGrid.getBlock(block.x-1,block.y).alight()) {blockBurn(block.x - 1, block.y);
                }
                if(block.y>0 && BlockGrid.getBlock(block.x,block.y-1).alight()){blockBurn(block.x, block.y - 1);
                }
                if(block.x<dimX-1 && BlockGrid.getBlock(block.x+1,block.y).alight()){
                    blockBurn(block.x + 1, block.y);
                }
                if(block.y<dimY-1 && BlockGrid.getBlock(block.x,block.y+1).alight()){
                    blockBurn(block.x, block.y +1);
                }
                //CHECK DIAGONAL CASE
                if(a==5) {
                    if (block.x > 0 && block.y>0 && BlockGrid.getBlock(block.x - 1, block.y - 1).alight()) {
                        blockBurn(block.x - 1, block.y - 1);
                    }
                    if (block.x < dimX - 1 && block.y > 0 && BlockGrid.getBlock(block.x + 1, block.y - 1).alight()) {
                        blockBurn(block.x + 1, block.y - 1);
                    }
                    if (block.x < dimX - 1 && block.y < dimY - 1 && BlockGrid.getBlock(block.x + 1, block.y + 1).alight()) {
                        blockBurn(block.x + 1, block.y + 1);
                    }
                    if (block.x > 0 && block.y < dimY - 1 && BlockGrid.getBlock(block.x - 1, block.y + 1).alight()) {
                        blockBurn(block.x - 1, block.y + 1);
                    }
                    a = 0;
                }
                block.fuel--;
                //Remove from at boundary or if out of fuel
                if(block.fuel == 0 || block.x==0 || block.y==0 || block.x==dimX-1 || block.y== dimY - 1){
                    burning.remove(block);
                    burnt(block.x,block.y);
                }
                //Check wh
                else if(block.fuel == 0 && !BlockGrid.getBlock(block.x + 1, block.y - 1).unburnt &&
                        !BlockGrid.getBlock(block.x + 1, block.y + 1).unburnt &&
                        !BlockGrid.getBlock(block.x + 1, block.y).unburnt &&
                        !BlockGrid.getBlock(block.x , block.y - 1).unburnt &&
                        !BlockGrid.getBlock(block.x - 1, block.y - 1).unburnt &&
                        !BlockGrid.getBlock(block.x - 1, block.y + 1).unburnt &&
                        !BlockGrid.getBlock(block.x - 1, block.y ).unburnt &&
                        !BlockGrid.getBlock(block.x , block.y - 1).unburnt) {
                    burning.remove(block);
                    burnt(block.x,block.y);
                }

                UserView.localController.updateView();
                a++;
            }
            stop = System.currentTimeMillis();
            try{ this.sleep(80 - stop + start);}
            catch(Exception e) {}
        }
        double start = System.nanoTime();
        double stop = System.nanoTime();

//        pixel(x,y);
//        gBurn.drawImage(getImage(), 0, 0, null);
//        for(int r=0; r<500; r++) {
////            start = System.nanoTime();
////            gBurn.fillOval(x-r,y-r,2*r,2*r);
////            stop = System.nanoTime();
////            System.out.println(stop-start);
//            for(int xPix = -r; xPix<=r; xPix=xPix+1*windX) {
//                for(int yPix = -r; yPix<=r; yPix=yPix+1*windY) {
//                    pixelBurn(xPix,yPix);
////                    if ((int) Math.sqrt(xPix*xPix + yPix*yPix) == r) {
//                        fireLayer.setRGB(x+xPix, y+yPix, Color.red.getRGB());
//                        gBurn.drawImage(Fire.fireLayer, 0, 0, null);
////                        gBurn.drawOval(x+xPix,y+yPix,x+xPix,y+yPix);
//                    }
//                }
////           }
//        }
    }

    public void simulateOverGrid2(Graphics g,Controller ctrl) throws IOException {
//        System.out.println(startX);
//        System.out.println(startY);
        System.out.println("TRY");
        int countX = startX;
        try {
            setFire(startX, startY, true);
            if (windX > 0 && windY > 0) {
                for (int x = startX; x < dimX - 1; x++) {
                    for (int y = startY; y < dimY - 1; y++) {
                        setFire(countX + 1, y + 1, true);
                        setFire(countX, y + 1, true);
                        setFire(countX + 1, y, true);
                        countX++;
                        g.drawImage(getImage(), 0, 0, null);
                    }
                    countX = x;

                }

            } else if (windX < 0 && windY > 0) {
                countX = startX;
                for (int x = startX; x > 0; x--) {
                    for (int y = startY; y < dimY; y++) {
                        setFire(countX - 1, y + 1, true);
                        setFire(countX - 1, y, true);
                        setFire(countX, y + 1, true);
                        g.drawImage(getImage(), 0, 0, null);
                        countX--;
                    }
                    countX = x;
                }
            } else if (windX > 0 && windY < 0) {
                countX = startX;
                for (int x = startX; x < dimX; x++) {
                    for (int y = startY; y > 0; y--) {
                        setFire(countX + 1, y - 1, true);
                        setFire(countX, y - 1, true);
                        setFire(countX + 1, y, true);
                        g.drawImage(getImage(), 0, 0, null);
                        countX++;
                    }
                    countX = x;
                }
            } else if (windX < 0 && windY < 0) {
                countX = startX;
                for (int x = startX; x > 0; x--) {
                    for (int y = startY; y > 0; y--) {
                        setFire(countX - 1, y - 1, true);
                        setFire(countX, y - 1, true);
                        setFire(countX - 1, y, true);
                        g.drawImage(getImage(), 0, 0, null);
                        countX--;
                    }
                    countX = x;
                }
            }
        }catch(Exception e){};//this.sleep(10);}

        /*
                double gradient = ((double) windX) / ((double) windY);
                System.out.println(gradient);
                double yInt = (-gradient * startX) + startY;
                System.out.println(yInt);
                if (windX>0 && windY>0) {
                    for (int x = startX; x < dimX; x++) {
                        for (int y = startY; y < dimY; y++) {

                            if (((int) (gradient * x + yInt) == y)) {
                                isBurning[x][y] = true;
                                setBurningTrees(x,y,g);
                                //g.setColor(Color.red);
                                //g.fillRect(x,y,1,1);
                                g.drawImage(getImage(), 0, 0, null);

                                //UserView.pnlVizualizer.filterHeight(0,(Graphics2D) g);
                                ctrl.updateView();
                            }
                        }
                    }
                    UserView.pnlVizualizer.startFireClicked = false;
                    stopped = true;
                }

                else if (windX>0 && windY<0){
                    for (int x = startX; x < dimX; x++) {
                        for (int y = startY; y >0 ; y--) {
                            if (((int) (gradient * x + yInt) == y)) {
                                isBurning[x][y] = true;
                                setBurningTrees(x,y,g);
                                g.drawImage(getImage(), 0, 0, null);


                                ctrl.updateView();
                            }
                        }
                    }
                    UserView.pnlVizualizer.startFireClicked = false;
                    stopped = true;
                }

                else if (windX<0 && windY>0){
                    for (int x = startX; x >0 ; x--) {
                        for (int y = startY; y < dimY; y++) {
                            if (((int) (gradient * x + yInt) == y)) {
                                isBurning[x][y] = true;
                                setBurningTrees(x,y,g);
                                g.drawImage(getImage(), 0, 0, null);

                                ctrl.updateView();
                            }
                        }
                    }
                    UserView.pnlVizualizer.startFireClicked = false;
                    stopped = true;
                }

                else if (windX<0 && windY<0){
                    for (int x = startX; x > 0; x--) {
                        for (int y = startY; y > 0; y--) {
                            if (((int) (gradient * x + yInt) == y)) {
                                isBurning[x][y] = true;
                                setBurningTrees(x,y,g);
                                g.drawImage(getImage(), 0, 0, null);
                                ctrl.updateView();
                            }
                        }
                    }
                    UserView.pnlVizualizer.startFireClicked = false;
                    stopped = true;
                }

         */
    }

    public void run(){
        while(startX==-1) {
            try {
                this.wait(50);      //Wait for play to be pressed.
            } catch (Exception e) {
            }
        }
        try {
            simulateOverGrid(UserView.pnlVizualizer.getGraphics(),UserView.localController);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void resetFireLayer(){
        for (int x = 0; x<dimX;x++){
            for (int y = 0; y<dimY;y++){
                setFire(x,y,false);
            }
        }
    }
}

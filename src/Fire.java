import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;

public class Fire extends Thread{
    boolean[][] isBurning;
    int dimX;
    int dimY;
    static BufferedImage fireLayer;
    int windX;
    int windY;
    int startX;
    int startY;
    public volatile boolean paused;
    public volatile  boolean stopped;
    private ArrayList<Plant> undergrowth;
    private ArrayList<Plant> canopy;
    static Graphics2D gBurn;

    public Fire(int dimx,int dimy, int sx, int sy, int wS, int wD, ArrayList<Plant> undergrowth, ArrayList<Plant> canopy){
        windX = (int) Math.round(wS*Math.cos(wD));; ;
        windY = (int) Math.round(wS*Math.sin(wD));;
        startX = sx;
        startY = sy;
        dimX = dimx;
        dimY = dimy;
        this.undergrowth = undergrowth;
        this.canopy = canopy;
        isBurning = new boolean[dimX][dimY];
        for (int x = 0; x<dimX;x++){
            for (int y = 0; y<dimY;y++){
                setFire(x,y,false);
            }
        }
        fireLayer = new BufferedImage(dimX,dimY,BufferedImage.TYPE_INT_ARGB);
        paused = true;
        stopped = false;
    }

    public void setFire(int x, int y, boolean burning){
        //if ((x<0||y<0||x>dimX||y>dimY)) {
         //   System.out.println("out of bounds");
       // }
        //else{
//            System.out.println("FIRE  "+x+"   "+y);
            isBurning[x][y] = burning;
       // }
    }

    public int getDimX(){
        return dimX;
    }

    public int getDimY(){
        return dimY;
    }
    public void pixel(int x, int y){
        fireLayer.setRGB(x,y,Color.red.getRGB());
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

    public void setStartX(int sx){
        startX = sx;
    }

    public void setWindY(int wS, int wD){
        windY = (int) Math.round(wS*Math.sin(Math.toRadians(wD)));
    }

    public void setWindX(int wS, int wD){
        windX = (int) Math.round(wS*Math.cos(Math.toRadians(wD)));
    }

    public void setStartY(int sy){
        startY = sy;
    }

    public void simulateOverGrid(Graphics g,Controller ctrl) throws IOException {
        this.gBurn = (Graphics2D)g;
        System.out.println("TRY0");

        burn(startX,startY);
//        Burn burn = new Burn(startX,startY);
//        burn.start();
    }
    public void burn(int x, int y){
        gBurn.setColor(new Color(255,0,0));
        double start = System.nanoTime();
        double stop = System.nanoTime();

//        pixel(x,y);
//        gBurn.drawImage(getImage(), 0, 0, null);
        for(int r=0; r<200; r++) {
            start = System.nanoTime();
            gBurn.drawOval(x-r,y-r,2*r,2*r);
            stop = System.nanoTime();
            System.out.println("stop-start");
//            for(int xPix = -r; xPix<=r; xPix++) {
//                for(int yPix = -r; yPix<=r; yPix++) {
//                    if ((int) Math.sqrt(xPix*xPix + yPix*yPix) == r) {
////                        fireLayer.setRGB(x+xPix, y+yPix, Color.red.getRGB());
////                        gBurn.drawImage(Fire.fireLayer, 0, 0, null);
//                        gBurn.drawLine(x+xPix,y+yPix,x+xPix,y+yPix);
//                    }
//                }
//            }
        }
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
                System.out.println("WAITING");
                this.wait(50);
            } catch (Exception e) {
            }
        }
        try {
            System.out.println("TRYING");
            simulateOverGrid(UserView.pnlVizualizer.getGraphics(),UserView.localController);
            //UserView.pnlVizualizer.filterHeight(0, (Graphics2D) UserView.pnlVizualizer.getGraphics());
            //UserView.localController.updateView();
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


import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

public class VizPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {

    private static volatile boolean complete;
    private ArrayList<Plant> undergrowthList;
    private ArrayList<Plant> canopyList;
    private Grid grid;
    private boolean initialized = false;
    private Image terrainLayer;
    public int heightSliderValue;

    private double zoomFactor = 1;
    private double prevZoomFactor = 1;
    private boolean zoomer;
    private boolean dragger;
    private boolean released;
    private double xOffset = 0;
    private double yOffset = 0;
    private int xDiff;
    private int yDiff;
    private Point startPoint;
    private boolean one = false;
    private AffineTransform at = new AffineTransform();
    private boolean zoomWithButton;

    public boolean canopyCHB;
    public boolean undergrowthCHB;

    // private HashMap<Point,Plant> canopyMap;
   // private HashMap<Point,Plant> undergrowthMap;


    public VizPanel(Grid grid){

        super();
        complete = false;
        this.grid = grid;
        //canopyMap = new HashMap<Point, Plant>();
       //undergrowthMap = new HashMap<Point, Plant>();
        canopyList = new ArrayList<Plant>();
        undergrowthList = new ArrayList<Plant>();
        heightSliderValue = UserView.getPlantHeightMin();
        zoomWithButton = false;
        canopyCHB = true;
        undergrowthCHB = true;
        addMouseListeners();
    }

    //default constructor
    public VizPanel() {
        super();
        complete = false;
        heightSliderValue = UserView.getPlantHeightMin();
       // canopyMap = new HashMap<Point, Plant>();
        //undergrowthMap = new HashMap<Point, Plant>();
        canopyList = new ArrayList<Plant>();
        undergrowthList = new ArrayList<Plant>();
        addMouseListeners();
    }

    public void addMouseListeners(){
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }
    public void print(String s){
        System.out.println("VizPanel: "+s);
    }

    public void setGrid(Grid grid){
        print("setGrid");
        this.grid = grid;
        initialized = true;

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);
        /* If files have been loaded, set initialized to true and enable these features*/
        if(initialized){

            Graphics2D g2 = (Graphics2D) g;

            if (zoomer) {
                setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
                System.out.println("Zooming");
                at = new AffineTransform();
                double xRel =0.0;
                double yRel = 0.0;
                if(zoomWithButton){
                    xRel = this.getLocationOnScreen().getX();
                    yRel = this.getLocationOnScreen().getY();
                    zoomWithButton = false;
                }else{
                     xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
                     yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();
                }


                double zoomDiv = zoomFactor / prevZoomFactor;

                xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
                yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

                at.translate(xOffset, yOffset);
                at.scale(zoomFactor, zoomFactor);
                prevZoomFactor = zoomFactor;
                g2.transform(at);
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                zoomer = false;
            }

            else if (dragger) {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                System.out.println("Dragging");
                at = new AffineTransform();
                at.translate(xOffset + xDiff, yOffset + yDiff);
                at.scale(zoomFactor, zoomFactor);
                g2.transform(at);

                if (released) {
                    xOffset += xDiff;
                    yOffset += yDiff;
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    dragger = false;

                }

            }
            else{ g2.transform(at);}

            if(!one){
                drawBackground(g2); //for drawing the terrain
                one = true;
            }
            drawBackground(g2);
            try {
                if (true){
                    filterHeight(heightSliderValue,g2);

                }
                else{
                    int s = 2;//drawPlantLayer(g2); //for drawing plants over terrain
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{ g.drawString("Select Files > Load Files to start a visualization",getWidth()/2,getHeight()/2); }

    }

    private void drawBackground(Graphics2D g){
        try {
            Image vizscaled = grid.getGreyscale().getScaledInstance(
                    getWidth(),
                    getHeight(),
                    Image.SCALE_SMOOTH);
            g.drawImage(vizscaled,0,0,null);
            this.terrainLayer = vizscaled;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterHeight(int sliderVal,Graphics2D g) throws IOException{
        double x = at.getScaleX();
        double a;
        double b;
        double c;
        int small=0;
        int med = 0;
        ArrayList<ArrayList<Plant>> pdbCan = FileLoader.getSpeciesListCan();
        ArrayList<ArrayList<Plant>> pdbUnder = FileLoader.getSpeciesListUnder();

        Iterator i = pdbUnder.iterator();
        Iterator j;
        int count = 0;
        int count2 = 0;
        if(undergrowthCHB==true) {
            while (i.hasNext()) {
                int id = FileLoader.getSpeciesUnder()[count].getID();
                if (FileLoader.getSpcDraw()[id]) {

                    j = pdbUnder.get(count).iterator();
                    g.setColor(pdbUnder.get(count).get(0).getColor());
                    //if(heightSliderValue!=sliderVal){break;}
                    while (j.hasNext()) {
                        Plant plant = pdbUnder.get(count).get(count2);
                        a = x * (plant.getRectX() + plant.getRad()) + at.getTranslateX(); //x transform
                        b = x * (plant.getRectY() + plant.getRad()) + at.getTranslateY(); //y transform
                        c = x * plant.getRad() / getWidth();    // ratio scaled radius to vizpanel
                        if (sliderVal <= plant.getHeight() && a >= 0 && a <= getWidth() && b >= 0 && b <= getHeight()) {
                            //SELECTIVE PRINTING MECHANICS
                        /*if (c <= 0.005&&c>0.001) {//draw small
                            if(small==20){
                                g.fill(plant.getShape());
                                small=0;
                            }
                            small +=1;
                        } else if (c>0.005&&c<=0.01) {  //draw medium
                            if(med==5){
                                g.fill(plant.getShape());
                                med = 0;
                            }
                            med += 1;
                        } else */
                            if (c > 0.01) {                //draw large
                                g.fill(plant.getShape());
                            }
                        }
                        j.next();
                        count2++;
                    }
                }
                i.next();
                count++;
                count2 = 0;
            }
        }

        i = pdbCan.iterator();

        count = 0;
        count2 = 0;

        if (canopyCHB == true) {
            while (i.hasNext()) {
                int id = FileLoader.getSpeciesCan()[count].getID();//get id of present species
                if (FileLoader.getSpcDraw()[id]) {                //check if Species ID should be drawn
                    j = pdbCan.get(count).iterator();
                    g.setColor(pdbCan.get(count).get(0).getColor());
                    //if(this.heightSliderValue!=sliderVal){break; }
                    while (j.hasNext()) {
                        Plant plant = pdbCan.get(count).get(count2);
                        a = x * (plant.getRectX() + plant.getRad()) + at.getTranslateX(); //x transform
                        b = x * (plant.getRectY() + plant.getRad()) + at.getTranslateY(); //y transform
                        c = x * plant.getRad() / getWidth();    // ratio scaled radius to vizpanel
                        if (sliderVal <= plant.getHeight() && a >= 0 && a <= getWidth() && b >= 0 && b <= getHeight()) {
                            //SELECTIVE PRINTING MECHANICS
                        /*if (c <= 0.01&&c>0.005) {//draw small
                            if(small==100){
                                g.fill(plant.getShape());
                                small=0;
                            }
                            small +=1;
                        } else if (c>0.01&&c<=0.02) {  //draw medium
                            if(med==8){
                                g.fill(plant.getShape());
                                med = 0;
                            }
                            med += 1;
                        } else */
                            if (c > 0.01) {                //draw large
                                g.fill(plant.getShape());
                            }
                        }
                        j.next();
                        count2++;
                    }
                }
                i.next();
                count++;
                count2 = 0;
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
/*
        Point location = mouseEvent.getPoint();

        for(Plant plant : undergrowthList){
            if (plant.getShape().contains(location)){
                System.out.println("Hit Undergrowth");
            }
        }

        for(Plant plant : canopyList){
            if(plant.getShape().contains(location)){
                System.out.println("Hit Canopy");
            }
        }*/
    }

    public Color getSelectedColor(Point location){
        try {
            Robot r = new Robot();
            Color c = r.getPixelColor((int)location.getX(),(int)location.getY());
            return c;
        } catch (AWTException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        released = true;
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        Point curPoint = mouseEvent.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent mouseWheelEvent) {

        zoomer = true;

        //Zoom in
        if (mouseWheelEvent.getWheelRotation() < 0) {
            zoomFactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (mouseWheelEvent.getWheelRotation() > 0 && zoomFactor>1) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    public void zoomInTenPercent(){
        zoomer = true;
        zoomWithButton = true;
        zoomFactor *= 1.1;
        repaint();
    }

    public void zoomOutTenPercent(){
        zoomer = true;
        zoomWithButton = true;
        zoomFactor /= 1.1;
        repaint();
    }
}

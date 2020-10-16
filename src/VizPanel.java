
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.Buffer;
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
import java.util.Objects;
import javax.naming.ldap.Control;
import javax.swing.*;

public class VizPanel extends JPanel implements MouseWheelListener, MouseListener, MouseMotionListener {

    private static volatile boolean complete;
    public int heightSliderValue;
    public int heightMinSliderValue;
    public int canopyMinSliderValue;
    public int canopyMaxSliderValue;
    private ArrayList<ArrayList<Plant>> undergrowthList;
    private ArrayList<ArrayList<Plant>> canopyList;
    private Grid grid;
    private boolean initialized = false;
//    private BufferedImage cache;
//    public void setCache(){
//        this.cache = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
//        this.gCache = cache.createGraphics();
//    }
//    private Graphics2D gCache;
//    private BufferedImage terrainLayer;


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

    public boolean startFireClicked;

    public int simStartX;
    public int simStartY;
    public Fire fire;

    private boolean simRunning;
    private double viewingThreshold = 0.001;// if plants are less than 1% of VizPanel, don't render until in view. Default.
    private int terrainRenderType = 1;
    int plantWithinRadVal;
    private Point location;

    // private HashMap<Point,Plant> canopyMap;
   // private HashMap<Point,Plant> undergrowthMap;


    public VizPanel(Grid grid){

        super();
        complete = false;
        this.grid = grid;
        //canopyMap = new HashMap<Point, Plant>();
       //undergrowthMap = new HashMap<Point, Plant>();
        //canopyList = FileLoader.getCanopy();
        //undergrowthList = FileLoader.getUnder();
        //heightSliderValue = UserView.getPlantHeightMin();
        zoomWithButton = false;
        canopyCHB = true;
        undergrowthCHB = true;
        addMouseListeners();
    }

    //default constructor
    public VizPanel() {
        super();
        complete = false;
        heightSliderValue = UserView.getPlantHeightMax();
        heightMinSliderValue = UserView.getPlantHeightMin();
        canopyMinSliderValue = UserView.getCanopyRadiusMin();
        canopyMaxSliderValue = UserView.getCanopyRadiusMax();
       // canopyMap = new HashMap<Point, Plant>();
        //undergrowthMap = new HashMap<Point, Plant>();
        //canopyList = FileLoader.getCanopy();
        //undergrowthList = FileLoader.getUnder();
        canopyCHB = true;
        undergrowthCHB = true;
        startFireClicked = false;
        simStartX = -1;
        simStartY = -1;
        simRunning = false;
        plantWithinRadVal = -1;
        plantWithinRadVal = 0;
        addMouseListeners();
    }
    public void setPlants(){
        this.canopyList = FileLoader.getCanopy();
        this.undergrowthList = FileLoader.getUnder();
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
        fire = new Fire(getWidth(),getHeight(),simStartX,simStartY,UserView.getWindSpeed(),UserView.getWindDirection(),FileLoader.getSpeciesListUnder1D(),FileLoader.getSpeciesListCan1D());
    }
    private boolean first = true;
    private int check;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        /* If files have been loaded, set initialized to true and enable these features*/
        if (initialized) {

            Graphics2D g2 = (Graphics2D) g;
            if (!simRunning) {
                if (zoomer) {
                    //System.out.println("Zooming");
                    at = new AffineTransform();
                    double xRel = 0.0;
                    double yRel = 0.0;
                    if (zoomWithButton) {
                        xRel = this.getLocationOnScreen().getX();
                        yRel = this.getLocationOnScreen().getY();
                        zoomWithButton = false;
                    } else {
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

                } else {
                    g2.transform(at);
                }

                drawBackground(g2);
                try {
                    if (true) {
                        filterHeightAndCanopyRadiusAndMore(heightMinSliderValue,heightSliderValue,
                                canopyMinSliderValue,canopyMaxSliderValue,plantWithinRadVal, g2);

                    } else {
                        int s = 2;//drawPlantLayer(g2); //for drawing plants over terrain
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (startFireClicked){
                simRunning = true;
                //System.out.println("CHECK");
//                this.printAll(gCache);
//                g2.drawImage(cache,0,0,this);

//                try {
//                    filterHeightAndCanopyRadius(0,heightSliderValue,0,canopyMaxSliderValue,g2);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }


                //UserView.localController.updateView();
//                Thread fireT = new Thread(fire);
//                fire.setStartX(simStartX);
//                fire.setStartY(simStartY);
//                fire.setWindX(UserView.getWindSpeed(),UserView.getWindDirection());
//                fire.setWindY(UserView.getWindSpeed(),UserView.getWindDirection());
//                if(called) {
//                    fireT.run();
//                    called = false;
//                }
                //fire.simulateOverGrid(g,UserView.localController);

            }
            //g.drawImage(fire.getImage(),0,0,null);

        }

    }

    private void drawBackground(Graphics2D g){
        try {
            Image vizscaled = grid.getGreyscale(terrainRenderType).getScaledInstance(
                    getWidth(),
                    getHeight(),
                    Image.SCALE_SMOOTH);
            g.drawImage(vizscaled,0,0,null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterHeightAndCanopyRadiusAndMore(int HeightValMin,int HeightValMax, int RadValMin,
                                                   int RadValMax, int withinRad, Graphics2D g) throws IOException{
        double x = at.getScaleX();
        double a;
        double b;
        double c;
        double z;
        int small=0;
        int med = 0;
        double start = System.nanoTime();
        double stop;
        ArrayList<ArrayList<Plant>> pdbCan = FileLoader.getSpeciesListCan();
        ArrayList<ArrayList<Plant>> pdbUnder = FileLoader.getSpeciesListUnder();

        Iterator i = pdbUnder.iterator();
        Iterator j;
        int count = 0;
        int count2 = 0;

        if (withinRad<0){
            withinRad = 0;
        }
            if (undergrowthCHB) {
                if (HeightValMin < HeightValMax && RadValMin<RadValMax) {
                    while (i.hasNext()) {
                        int id = FileLoader.getSpeciesUnder()[count].getID();
                        if (FileLoader.getSpcDraw()[id]) {

                            j = pdbUnder.get(count).iterator();
                            g.setColor(pdbUnder.get(count).get(0).getColor());
                            //if(heightSliderValue!=sliderVal){break;}
                            while (j.hasNext()) {
                                Plant plant = pdbUnder.get(count).get(count2);
                                //plant.updateVisualPosition(getWidth(),getHeight());
                                z = x * 2 * plant.getRad();
                                a = x * plant.getRectX() + at.getTranslateX(); //x transform
                                b = x * plant.getRectY() + at.getTranslateY(); //y transform
                                c = x * plant.getRad() / getWidth();    // ratio scaled radius to vizpanel
                                if (HeightValMin <= plant.getHeight() && HeightValMax >= plant.getHeight() && RadValMin<=plant.getCanopyRadius()
                                        && RadValMax>=plant.getCanopyRadius() && withinRadius(plant,withinRad) && a + z >= 0 && a <= getWidth() && b + z >= 0 && b <= getHeight()) {
                                    if (c > viewingThreshold) {                //draw large
                                        g.drawImage(FileLoader.getIMG(plant.getID()), plant.at, this);
//
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
            } else {
                UserView.setFilterLabel("<html>OH NO!<br>Your selected slider values overlap.<br>Please reselect an appropriate value.");
            }

            i = pdbCan.iterator();

            count = 0;
            count2 = 0;

            if (canopyCHB) {
                if (HeightValMin < HeightValMax && RadValMin<RadValMax) {
                    while (i.hasNext()) {
                        int id = FileLoader.getSpeciesCan()[count].getID();//get id of present species
                        if (FileLoader.getSpcDraw()[id]) {                //check if Species ID should be drawn
                            j = pdbCan.get(count).iterator();
                            if (pdbUnder.get(count).get(count2).getBurnt() == false) {
                                g.setColor(pdbUnder.get(count).get(count2).getColor());
                            } else {
                                g.setColor(new Color(1.0f, 1.0f, 1.0f, 0.3f));
                            }
                            //if(this.heightSliderValue!=sliderVal){break; }
                            while (j.hasNext()) {
                                Plant plant = pdbCan.get(count).get(count2);
                                //plant.updateVisualPosition(getWidth(),getHeight());
                                z = x * 2 * plant.getRad();
                                a = x * plant.getRectX() + at.getTranslateX(); //x transform
                                b = x * plant.getRectY() + at.getTranslateY(); //y transform
                                c = x * plant.getRad() / getWidth();    // ratio scaled radius to vizpanel
                                if (HeightValMin <= plant.getHeight() && HeightValMax >= plant.getHeight() && RadValMin<=plant.getCanopyRadius()
                                        && RadValMax>= plant.getCanopyRadius() && withinRadius(plant,withinRad) && a + z >= 0 && a <= getWidth() && b + z >= 0 && b <= getHeight()) {

                                    if (c > viewingThreshold) {
                                        //draw large
                                        g.drawImage(FileLoader.getIMG(plant.getID()), plant.at, this);
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
                } else {
                    UserView.setFilterLabel("<html>OH NO!<br>Your selected slider values overlap.<br>Please reselect an appropriate value.");
                }
            }

    }

    public boolean withinRadius(Plant plant, int rad){
        Ellipse2D circle = new Ellipse2D.Float();
        if (rad ==0) {
            return true;
        }
        else if (location!=null) {
            circle.setFrame(location.getX(), location.getY(), rad*2, rad*2);
            double distanceSq = ((float)plant.getPos().get(0) +plant.getRad() )*((float)plant.getPos().get(0) +plant.getRad())
                    + ((float)plant.getPos().get(1)+plant.getRad())*((float)plant.getPos().get(1)+plant.getRad());
            if (distanceSq <= rad*rad){
                return true;
            }
        }
        //UserView.viewingPlantsWithinRadius = false;
        return false;
    }


    /**
     * Returns details of the plants intersecting the clicked point
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        simStartX = mouseEvent.getX();
        //System.out.println(simStartX);
        //System.out.println(simStartY);
        simStartY = mouseEvent.getY();
        if(fire!=null) {
            fire.setStartX(simStartX);
            fire.setStartY(simStartY);
        }

        location = mouseEvent.getPoint();
        double x = (location.getX()-at.getTranslateX())/at.getScaleX();
        double y = (location.getY()-at.getTranslateY())/at.getScaleY();

        try{
            ArrayList<Plant> theChosenOnes = new ArrayList<>();

            for(ArrayList<Plant> alist : undergrowthList){
                for(Plant plant : alist){
                    if (plant.getShape().contains(x,y)) {
                        theChosenOnes.add(plant);
                        if(!Objects.requireNonNull(UserView.chbControlsList.getSelectedItem()).toString().equals("Simulation")){
                            UserView.localController.changeMenu("Plant Detail");
                        }


                    }
                }
            }

            for(ArrayList<Plant> alist : canopyList) {
                for (Plant plant : alist) {
                    if (plant.getShape().contains(x, y)) {
                        theChosenOnes.add(plant);
                        if(!Objects.requireNonNull(UserView.chbControlsList.getSelectedItem()).toString().equals("Simulation")){
                            UserView.localController.changeMenu("Plant Detail");
                        }

                    }
                }
            }

            Controller.updatePlantDetailText(theChosenOnes);
        }catch(NullPointerException e){
            // Do nothing, EcoViz has not been loaded.
        }



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
        UserView.updateZoomLevel();
    }

    public void zoomInTenPercent(){
        zoomer = true;
        zoomWithButton = true;
        zoomFactor = zoomFactor + 0.1;
        repaint();
    }

    public void zoomOutTenPercent(){
        if(zoomFactor>1){
            zoomer = true;
            zoomWithButton = true;
            zoomFactor = zoomFactor - 0.1;
            repaint();
        }else{
            zoomer = true;
            zoomWithButton = true;
            zoomFactor = 1;
            repaint();

        }

    }

    public String getZoomPercentage(){
        return String.format("%d%%",(int)(zoomFactor*100)-100);
    }

    public void setViewingThreshold(int value) {
        this.viewingThreshold = value/1000.0;
    }

    public int getTerrainRenderType() {
        return terrainRenderType;
    }

    public void setTerrainRenderType(int terrainRenderType) {
        this.terrainRenderType = terrainRenderType;
    }
}

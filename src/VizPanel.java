
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
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


    public VizPanel(Grid grid){
        super();
        complete = false;
        this.grid = grid;
        heightSliderValue = UserView.getPlantHeightMin();
        addMouseListeners();
    }

    //default constructor
    public VizPanel() {
        super();
        complete = false;
        heightSliderValue = UserView.getPlantHeightMin();
        addMouseListeners();
    }

    public void addMouseListeners(){
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addMouseListener(this);
    }

    public void setGrid(Grid grid){
        this.grid = grid;
        initialized = true;
        System.out.println("set to true");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Thread currentThread = Thread.currentThread();

        super.paintComponent(g);
        /* If files have been loaded, set initialized to true and enable these features*/
        if(initialized){

            Graphics2D g2 = (Graphics2D) g;

            if (zoomer) {
                at = new AffineTransform();

                double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
                double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

                double zoomDiv = zoomFactor / prevZoomFactor;

                xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
                yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;

                at.translate(xOffset, yOffset);
                at.scale(zoomFactor, zoomFactor);
                prevZoomFactor = zoomFactor;
                g2.transform(at);
                zoomer = false;
            }

            else if (dragger) {
                at = new AffineTransform();
                at.translate(xOffset + xDiff, yOffset + yDiff);
                at.scale(zoomFactor, zoomFactor);
                g2.transform(at);

                if (released) {
                    xOffset += xDiff;
                    yOffset += yDiff;
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
                if (true){//heightSliderValue>UserView.getPlantHeightMin()){  //changed != to >
                    //double now = System.currentTimeMillis();
                    filterHeight(heightSliderValue,g2);
                    //double then = System.currentTimeMillis()-now;
                    //System.out.println("Duration plantlayer: "+then+currentThread);
                }
                else{
                    int s = 2;//drawPlantLayer(g2); //for drawing plants over terrain
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{ g.drawString("Select Files > Load Files to start a visualization",getWidth()/2,getHeight()/2); }

    }

    /*public void drawPlantLayer(Graphics2D g) throws IOException {
        ArrayList<ArrayList<Plant>> pdbCan = FileLoader.getSpeciesListCan();
        ArrayList<ArrayList<Plant>> pdbUnder = FileLoader.getSpeciesListUnder();


        Iterator i = pdbUnder.iterator();
        Iterator j;
        int count = 0;
        int count2 = 0;

        while (i.hasNext()){
            j = pdbUnder.get(count).iterator();
            while (j.hasNext()){
                Plant plant = pdbUnder.get(count).get(count2);
                //plant.setColor(new Color(100,200,250, 30));
                Color color = plant.getColor();
                g.setColor(color);
                g.fillRect(plant.getRectX(),//Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                        plant.getRectY(),//Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
                        5,5);
                j.next();
                count2++;
            }
            i.next();
            count++;
            count2 = 0;
        }
        i = pdbCan.iterator();
        count = 0;
        count2 = 0;

        while (i.hasNext()){
            j = pdbCan.get(count).iterator();
            while (j.hasNext()){
                Plant plant = pdbCan.get(count).get(count2);
                //plant.setColor(new Color(count));
                Color color = plant.getColor();
                g.setColor(color);
                g.fillOval(plant.getRectX(),//Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                        plant.getRectY(),//Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
                        5,5);
                j.next();
                count2++;
            }
            i.next();
            count++;
            count2 = 0;
        }
    }*/

    private void drawBackground(Graphics2D g){
        try {
            Image vizscaled = grid.getGreyscale().getScaledInstance(
                    getWidth(),
                    getHeight(),
                    Image.SCALE_SMOOTH);
            g.drawImage(vizscaled,0,0,null);
            this.terrainLayer = vizscaled;
            UserView.localController.updateView();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void filterHeight(int sliderVal,Graphics2D g) throws IOException{

        ArrayList<ArrayList<Plant>> pdbCan = FileLoader.getSpeciesListCan();
        ArrayList<ArrayList<Plant>> pdbUnder = FileLoader.getSpeciesListUnder();

        Iterator i = pdbUnder.iterator();
        Iterator j;
        int count = 0;
        int count2 = 0;
        while(i.hasNext()){
            int id = FileLoader.getSpeciesUnder()[count].getID();
            if(FileLoader.getSpcDraw()[id]){

                j = pdbUnder.get(count).iterator();
                g.setColor(pdbUnder.get(count).get(0).getColor());
                //if(heightSliderValue!=sliderVal){break;}
                while (j.hasNext()) {
                    Plant plant = pdbUnder.get(count).get(count2);
                    if ((sliderVal <= plant.getHeight())) {
                        g.fill(plant.getShape());
                        //g.fillRect(plant.getRectX(),plant.getRectY(),5, 5);
                    }
                    j.next();
                    count2++;
                }
            }
            i.next();
            count++;
            count2 = 0;
        }

        i = pdbCan.iterator();

        count = 0;
        count2 = 0;

        while(i.hasNext()){
            int id = FileLoader.getSpeciesCan()[count].getID();//get id of present species
            if(FileLoader.getSpcDraw()[id]){                //check if Species ID should be drawn
                j = pdbCan.get(count).iterator();
                g.setColor(pdbCan.get(count).get(0).getColor());
                //if(this.heightSliderValue!=sliderVal){break; }
                while (j.hasNext()) {
                    Plant plant = pdbCan.get(count).get(count2);
                    if ((sliderVal <= plant.getHeight())) {
                        //g.fillOval((int)plant.getRectX(), (int)plant.getRectY(), 5, 5);
                        g.fill(plant.getShape());
                    }
                    j.next();
                    count2++;
                }
            }
            i.next();
            count++;
            count2 = 0;
        }
        UserView.localController.updateView();

    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

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
        if (mouseWheelEvent.getWheelRotation() > 0 && zoomFactor>=1) {
            zoomFactor /= 1.1;
            repaint();
        }
    }

    public void zoomInTenPercent(){
        zoomer = true;
        zoomFactor *= 1.1;
        repaint();
    }

    public void zoomOutTenPercent(){
        zoomer = true;
        zoomFactor /= 1.1;
        repaint();
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Iterator;

public class VizPanel extends JPanel /*implements Runnable*/{

    private static volatile boolean complete;
    private Grid grid;
    private boolean initialized = false;
    private Image terrainLayer;
    public int heightSliderValue;

    public VizPanel(Grid grid){
        super();
        complete = false;
        this.grid = grid;
        heightSliderValue = UserView.getPlantHeightMin();
    }

    //default constructor
    public VizPanel() {
        super();
        complete = false;
        heightSliderValue = UserView.getPlantHeightMin();
    }

    public void setGrid(Grid grid){
        this.grid = grid;
        initialized = true;
        System.out.println("set to true");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(initialized){
            drawBackground(g); //for drawing the terrain
            try {
                if (heightSliderValue!=UserView.getPlantHeightMin()){
                    filterHeight(heightSliderValue,g);
                }
                else{
                    drawPlantLayer(g); //for drawing plants over terrain
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{ g.drawString("Select Files > Load Files to start a visualization",getWidth()/2,getHeight()/2); }

    }

    public void drawPlantLayer(Graphics g) throws IOException {
        //For drawing plant shapes
       // g.setColor(Color.BLUE); //can create an array of colors for species
        //g.fillOval(60, 95, 60, 60);

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
                g.fillRect(Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                        Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
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
                g.fillOval(Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                        Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
                        5,5);
                j.next();
                count2++;
            }
            i.next();
            count++;
            count2 = 0;
        }
    }

    private void drawBackground(Graphics g){
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

    public void filterHeight(int sliderVal,Graphics g){

        ArrayList<ArrayList<Plant>> pdbCan = FileLoader.getSpeciesListCan();
        ArrayList<ArrayList<Plant>> pdbUnder = FileLoader.getSpeciesListUnder();

        Iterator i = pdbUnder.iterator();
        Iterator j;

        int count = 0;
        int count2 = 0;

        while(i.hasNext()){
            j = pdbUnder.get(count).iterator();
            while (j.hasNext()) {
                Plant plant = pdbUnder.get(count).get(count2);
                if ((sliderVal >= plant.getHeight())){
                    //plant.setColor(new Color(1,1,1,0));
                    g.setColor(plant.getColor());
                    g.fillRect(Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                            Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
                            5,5);
                }
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

        while(i.hasNext()){
            j = pdbCan.get(count).iterator();
            while (j.hasNext()) {
                Plant plant = pdbCan.get(count).get(count2);
                if ((sliderVal >= plant.getHeight())){
                    //plant.setColor(new Color(1,1,1,0));
                    g.setColor(plant.getColor());
                    g.fillOval(Math.round((float)plant.getPos().get(0)*getWidth()/(grid.getDimx()*(float)grid.getSpacing())),//*scalingFactorX/Math.round((float)grid.getSpacing()),
                            Math.round((float)plant.getPos().get(1)*getHeight()/(grid.getDimy()*(float)grid.getSpacing())),
                            5,5);
                }
                j.next();
                count2++;
            }
            i.next();
            count++;
            count2 = 0;
        }
        UserView.localController.updateView();

    }
/*
    //@Override
    public void run() {
        while (!complete){
            repaint();
        }
    }*/
}

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class VizPanel extends JPanel /*implements Runnable*/{

    private static volatile boolean complete;
    private Grid grid;
    private boolean initialized = false;

    public VizPanel(Grid grid){
        super();
        complete = false;
        this.grid = grid;
    }

    //default constructor
    public VizPanel() {
        super();
        complete = false;
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
                drawPlantLayer(g); //for drawing plants over terrian
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
        Iterator i = pdbCan.iterator();
        Iterator j;
        int count = 0;
        int count2 = 0;
        int scalingFactorX = getWidth() / grid.getGreyscale().getWidth();
        int scalingFactorY = getHeight() / grid.getGreyscale().getHeight();

        while (i.hasNext()){
            j = pdbCan.get(count).iterator();
            while (j.hasNext()){
                Plant plant = pdbCan.get(count).get(count2);
                plant.setColor(new Color(count));
                Color color = plant.getColor();
                g.setColor(color);
                g.fillOval(Math.round((float)plant.getPos().get(0))*scalingFactorX/Math.round((float)grid.getSpacing()),
                        Math.round((float)plant.getPos().get(1))*scalingFactorY/Math.round((float)grid.getSpacing()),
                        5,5);
                j.next();
                count2++;
            }
            i.next();
            count++;
            count2 = 0;
        }

        i = pdbUnder.iterator();
        count = 0;
        count2 = 0;

        while (i.hasNext()){
            j = pdbUnder.get(count).iterator();
            while (j.hasNext()){
                Plant plant = pdbUnder.get(count).get(count2);
                plant.setColor(new Color(100,200,250, 100));
                Color color = plant.getColor();
                g.setColor(color);
                g.fillRect(Math.round((float)plant.getPos().get(0))*scalingFactorX/Math.round((float)grid.getSpacing()),
                        Math.round((float)plant.getPos().get(1))*scalingFactorY/Math.round((float)grid.getSpacing()),
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
/*
    //@Override
    public void run() {
        while (!complete){
            repaint();
        }
    }*/
}

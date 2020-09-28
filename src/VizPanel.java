import javax.swing.*;
import java.awt.*;
import java.io.IOException;

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
            drawPlantLayer(g); //for drawing plants over terrian
        }else{ g.drawString("Select Files > Load Files to start a visualization",getWidth()/2,getHeight()/2); }

    }

    public void drawPlantLayer(Graphics g){
        //For drawing plant shapes
        g.setColor(Color.BLUE); //can create an array of colors for species
        g.fillOval(60, 95, 60, 60);
    }

    private void drawBackground(Graphics g){
        try {
            Image vizScaled = grid.getGreyscale().getScaledInstance(
                    UserView.getPnlVizualizer().getWidth(),
                    UserView.getPnlVizualizer().getHeight(),
                    Image.SCALE_SMOOTH);
            g.drawImage(vizScaled,0,0,null);
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

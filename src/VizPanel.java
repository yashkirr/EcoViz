import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class VizPanel extends JPanel implements Runnable{

    public static volatile boolean complete;
    public Grid grid;

    public VizPanel(Grid grid){
        complete = false;
        this.grid = grid;
    }

    @Override
    protected void paintComponent(Graphics g) {
        int dimX = grid.getDimx();
        int dimY = grid.getDimy();
        super.paintComponent(g);
        try {
            Image vizScaled = grid.getGreyscale().getScaledInstance(dimX,dimY, Image.SCALE_SMOOTH);
            g.drawImage(vizScaled,0,0,null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        while (!complete){
            repaint();
        }
    }
}

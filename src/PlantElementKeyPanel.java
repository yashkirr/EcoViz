import javax.swing.*;
import java.awt.*;

public class PlantElementKeyPanel extends JPanel {
    private Color color;
    private String plantName;

    public PlantElementKeyPanel(Color color, String plantName){
        this.color = color;
        this.plantName = plantName;
        this.setBackground(new Color(255,255,255));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(10,50,25,25);
        g.setColor(Color.DARK_GRAY);
        g.drawString(plantName,10+40,50+18);


    }

}

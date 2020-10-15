import javax.swing.*;
import java.awt.*;

public class ElevationKeyPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(new GradientPaint(new Point(0,0),Color.BLACK,new Point(getWidth(),0),Color.WHITE,false));
        g2.fillRect(0,0,getWidth(),getHeight());
    }
}

import javax.swing.*;
import java.awt.*;

public class LoadingSplashDialog extends JDialog {

    public LoadingSplashDialog(){
        setLayout(new GridBagLayout());
        setSize(250, 150);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setResizable(false);
        JLabel info = new JLabel("<html>" +
                "<b>Initializing EcoViz</b>" +
                "<br>Loading files, please wait...");
       add(info);
    }

}

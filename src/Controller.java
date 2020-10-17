import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Controller class for EcoViz, used to facilitate important information between View, VizPanel, and Stored Memory
 *
 * @author yashkir
 */
public class Controller {
    private static boolean initialized = false;
    private UserView localUserView;

    /**
     * Constructor for View Class
     */
    public Controller(UserView localUserView) {
        this.localUserView = localUserView;
    }

    /**
     * Constructor for classes which need utility methods
     */
    public Controller() {

    }

    /**
     * Receives list of plants and formats it into information text for displaying on Userview
     * plant on-demand detail
     *
     * @param theChosenOnes Arraylist<Plant>
     */
    public static void updatePlantDetailText(ArrayList<Plant> theChosenOnes) {
        String finalList = "";
        for (Plant plant :
                theChosenOnes) {
            String template = "<hr />\n" +
                    "<p><strong>English Name: </strong></p>\n" +
                    "<p>%1$s</p>\n" +
                    "<p><strong>Latin Name:</strong></p>\n" +
                    "<p>%2$s</p>\n" +
                    "<p><strong>Type:</strong></p>\n" +
                    "<p>%3$s</p>\n" +
                    "<p><strong>Location:</strong></p>\n" +
                    "<p>%4$s</p>\n" +
                    "<p><strong>Canopy Radius Size:&nbsp;</strong></p>\n" +
                    "<p>%5$.2f</p>\n" +
                    "<p><strong>Age:</strong></p>\n" +
                    "<p>%6$.1f</p>\n" +
                    "<p><strong>Height:</strong></p>\n" +
                    "<p>%7$.2f</p>\n" +
                    "<hr />";
            String englishName = (String) plant.detail().get("englishName");
            String latinName = (String) plant.detail().get("latinName");
            float height = (float) plant.detail().get("height");
            float age = (float) plant.detail().get("age");
            float canopyRadius = (float) plant.detail().get("canopyRadius");
            Vector<Float> location = (Vector<Float>) plant.detail().get("location");
            String type = (String) plant.detail().get("type");

            String currentPlant = String.format(template, englishName, latinName, type, location.toString(), canopyRadius, age, height);
            finalList = finalList + "<br>" + currentPlant;
        }
        UserView.setLblPlantDetails("<html>" + finalList);
    }

    /**
     * Utility method for detecting if dialog is open that is needed to be closed.
     * Used for closing loading dialog
     *
     * @Author Yashkir Ramsamy
     */
    public static void closeLoadingScreen() {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1
                        && dialog.getContentPane().getComponent(0) instanceof JOptionPane) {
                    dialog.dispose();
                }
            }
        }

    }

    /**
     * Takes file paths of data files and loads it in FileLoader
     *
     * @param s  .elv
     * @param s1 .spc
     * @param s2 .pdb (canopy)
     * @param s3 .pdb (undergrowth)
     */
    public static void loadFile(String s, String s1, String s2, String s3) {
       /* 0: .elv
        1: .spc
        2: .pdb (canopy)
        3: .pdb (undergrowth)*/
        FileLoader.readELV(s);
        FileLoader.readSPC(s1);
        FileLoader.readPdbCan(s2);
        FileLoader.convertTo1DCan();
        FileLoader.readPdbUnder(s3);
        FileLoader.convertTo1DUnder();
    }

    /**
     * Disables control panel and settings menu to restrict the user to only entering files or exiting
     *
     * @author Yashkir Ramsamy
     */
    public void restrictControls(boolean restricted) {
        UserView.chbControlsList.setEnabled(!restricted);

    }

    public void print(String s) {
        System.out.println("Controller: " + s);
    }

    /**
     * Generates the terrain visualisation and EcoViz menu settings
     *
     * @throws IOException
     * @Author Calley Ramcharan, Victor Bantchovski, Yashkir Ramsamy
     */
    public void initializeTerrainGrid() throws IOException {
        restrictControls(false);
        generateKey(FileLoader.getMinElevation(), FileLoader.getMaxElevation());
        UserView.pnlVizualizer.setGrid(new Grid(FileLoader.getDimx(), FileLoader.getDimy(), FileLoader.getSpacing(), FileLoader.getLatitude(), FileLoader.getTerrain()));
        updateView();
        closeLoadingScreen();
        UserView.pnlVizualizer.setPlants();
    }

    /**
     * Used to generate key on legend menu panel
     *
     * @param minElevation minimum elevation of terrain
     * @param maxElevation maximum elevation of terrain
     * @author Yashkir Ramsamy
     */
    private void generateKey(double minElevation, double maxElevation) {
        UserView.lblElevationHeightMin.setText(String.format("%.2fm", minElevation));
        UserView.lblElevationHeightMax.setText(String.format("%.2fm", maxElevation));
        String[][] key = FileLoader.getSpcKey();
        Color[] colors = FileLoader.getSpcColor();
        JPanel listItemPanel = new JPanel();
        listItemPanel.setLayout(new BoxLayout(listItemPanel, BoxLayout.Y_AXIS));
        for (int i = 0; i < key.length; i++) {
            PlantElementKeyPanel p = new PlantElementKeyPanel(colors[i], key[i][0]);
            p.setPreferredSize(new Dimension(UserView.pnlPlantLegendList.getWidth(), 100));
            listItemPanel.add(p);
        }
        UserView.pnlPlantLegendList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        UserView.pnlPlantLegendList.getViewport().add(listItemPanel);


    }

    /**
     * fetches the species list generated by the FileLoader class and displays it to the tabbed
     * Filter pane
     *
     * @param list
     */
    public void getSpeciesList(String[][] list) {
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (int i = 0; i < list.length; i++) {
            listModel.addElement(list[i][0]);
        }
        UserView.getlistFilterSpecies().setModel(listModel);
    }

    /**
     * Fetches the genus list generated by the FileLoader class and displays it to the tabbed
     * Filter pane
     *
     * @param list
     */
    public void getGenusList(String[][] list) {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < list.length; i++) {
            //System.out.println(list[i][1]);
            String genus = list[i][1].split(" ")[0];
            if (!listModel.contains(genus)) {
                listModel.addElement(genus);
            }
        }
        UserView.getlistFilterGenus().setModel(listModel);
    }

    /**
     * Used for changing cursor icons at runtime
     *
     * @param handCursor handCursor int
     * @author Yashkir Ramsamy
     */
    public void setVisualizerCursor(int handCursor) {
        UserView.pnlVizualizer.setCursor(Cursor.getPredefinedCursor(handCursor));
    }

    /**
     * Used to update EcoViz whenever critical repaint has to occur
     *
     * @Author Yashkir Ramsamy
     */
    public void updateView() {
        if (!initialized) {
            UserView.setPlantHeightSliderValues(FileLoader.getMinPlantHeight(), FileLoader.getMaxPlantHeight());
            UserView.setCanopyRadiusSliderValues(FileLoader.getMinCanopyRadius(), FileLoader.getMaxCanopyRadius());
            setVisualizerCursor(Cursor.HAND_CURSOR);
            initialized = true;
        }
        getGenusList(FileLoader.getSpcKey());
        getSpeciesList(FileLoader.getSpcKey());
        UserView.pnlVizualizer.revalidate();
        UserView.pnlVizualizer.repaint();

    }

    /**
     * Used to changed to different menus given a menu item string
     *
     * @param menu Drop-down Menu String
     * @author Yashkir Ramsamy
     */
    public void changeMenu(String menu) {
        CardLayout card = (CardLayout) UserView.pnlControls.getLayout();
        switch (menu) {
            case "Filters":
                card.show(UserView.pnlControls, "pnlFilters");
                UserView.chbControlsList.setSelectedIndex(1);
                break;
            case "Visibility":
                card.show(UserView.pnlControls, "pnlVisibility");
                UserView.chbControlsList.setSelectedIndex(3);
                break;
            case "Simulation":
                card.show(UserView.pnlControls, "pnlSimulation");
                UserView.chbControlsList.setSelectedIndex(4);
                break;
            case "Rendering":
                card.show(UserView.pnlControls, "pnlViewSettings");
                UserView.chbControlsList.setSelectedIndex(6);
                break;
            case "Legend":
                card.show(UserView.pnlControls, "pnlLegend");
                UserView.chbControlsList.setSelectedIndex(0);
                break;
            case "Plant Detail":
                card.show(UserView.pnlControls, "pnlPlantDetail");
                UserView.chbControlsList.setSelectedIndex(5);
                break;
            case "Help":
                card.show(UserView.pnlControls, "pnlHelp");
                UserView.chbControlsList.setSelectedIndex(7);
                break;
            case "Zoom Distance":
                card.show(UserView.pnlControls, "pnlZoom");
                UserView.chbControlsList.setSelectedIndex(2);
                break;
        }

    }
    public boolean getInit(){ return initialized;}
}




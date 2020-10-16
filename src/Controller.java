/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Vector;

/**
 * Controller class for EcoViz, used to facilitate important information between View, VizPanel, and Stored Memory
 * @author yashkir
 */
public class Controller {
    private UserView localUserView;
    private static boolean initialized = false;

    /**
     * Constructor for View Class
     */
    public Controller(UserView localUserView){
        this.localUserView = localUserView;
    }
    /**
     * Constructor for classes which need utility methods
     */
    public Controller() {

    }

    /**
     * Recieves list of plants and formats it into information text for displaying on Userview
     * plant on-demand detail
     * @param theChosenOnes Arraylist<Plant>
     */
    public static void updatePlantDetailText(ArrayList<Plant> theChosenOnes) {
        String finalList ="";
        for (Plant plant:
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
           String latinName= (String) plant.detail().get("latinName");
            float height= (float) plant.detail().get("height");
           float age= (float) plant.detail().get("age");
            float canopyRadius= (float) plant.detail().get("canopyRadius");
            Vector<Float> location = (Vector<Float>) plant.detail().get("location");
            String type= (String) plant.detail().get("type");

            String currentPlant = String.format(template,englishName,latinName,type,location.toString(),canopyRadius,age,height);
            finalList = finalList + "<br>"+currentPlant;
        }
        UserView.setLblPlantDetails("<html>"+finalList);
    }


    /**
     * Disables control panel and settings menu to restrict the user to only entering files or exiting
     * @author Yashkir Ramsamy
     */
    public void restrictControls(boolean restricted){
        UserView.chbControlsList.setEnabled(!restricted);

    }

    /**
     *Utility method for detecting if dialog is open that is needed to be closed.
     *Used for closing loading dialog
     * @Author Yashkir Ramsamy
     */
    public static void closeLoadingScreen() {
        //  ActionListener close = (ActionEvent e) -> {
        Window[] windows = Window.getWindows();
        for (Window window : windows) {
            if (window instanceof JDialog) {
                JDialog dialog = (JDialog) window;
                if (dialog.getContentPane().getComponentCount() == 1
                        && dialog.getContentPane().getComponent(0) instanceof JOptionPane){
                    dialog.dispose();
                }
            }
        }

    }


    public void print(String s){
        System.out.println("Controller: "+s);
    }

    /**
     * Generates the terrain visualisation and EcoViz menu settings
     * @Author Calley Ramcharan, Victor Bantchovski, Yashkir Ramsamy
     * @throws IOException
     */
    public void initializeTerrainGrid() throws IOException {
        restrictControls(false);
        generateKey(FileLoader.getMinElevation(),FileLoader.getMaxElevation());
        UserView.pnlVizualizer.setGrid(new Grid(FileLoader.getDimx(),FileLoader.getDimy(),FileLoader.getSpacing(),FileLoader.getLatitude(), FileLoader.getTerrain()));
        updateView();
        closeLoadingScreen();
        UserView.pnlVizualizer.setPlants();
    }

    /**
     * Used to generate key on legend menu panel
     * @author Yashkir Ramsamy
     * @param minElevation minimum elevation of terrain
     * @param maxElevation maximum elevation of terrain
     */
    private void generateKey(double minElevation, double maxElevation) {
        UserView.lblElevationHeightMin.setText(String.format("%.2fm",minElevation));
        UserView.lblElevationHeightMax.setText(String.format("%.2fm",maxElevation));
       String[][] key= FileLoader.getSpcKey();
       Color[] colors = FileLoader.getSpcColor();
        JPanel listItemPanel = new JPanel();
        listItemPanel.setLayout(new BoxLayout(listItemPanel, BoxLayout.Y_AXIS));
        for(int i = 0; i< key.length;i++){
           PlantElementKeyPanel p= new PlantElementKeyPanel(colors[i],key[i][0]);
           p.setPreferredSize(new Dimension(UserView.pnlPlantLegendList.getWidth(),100));
           listItemPanel.add(p);
        }
        UserView.pnlPlantLegendList.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        UserView.pnlPlantLegendList.getViewport().add(listItemPanel);


    }

    /**
     * fetches the species list generated by the FileLoader class and displays it to the tabbed
     * Filter pane
     * @param list
     */
    public void getSpeciesList(String[][] list){
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        for (int i = 0; i < list.length; i++) {
            listModel.addElement(list[i][0].toString());
        }
        UserView.getlistFilterSpecies().setModel(listModel);
    }
    /**
     * Fetches the genus list generated by the FileLoader class and displays it to the tabbed
     * Filter pane
     * @param list
     */
    public void getGenusList(String[][] list){
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (int i = 0; i < list.length; i++) {
            //System.out.println(list[i][1]);
            String genus = list[i][1].split(" ")[0];
            if(!listModel.contains(genus.toString())){
                listModel.addElement(genus);
            }
        }
        UserView.getlistFilterGenus().setModel(listModel);
    }

    /**
     * Used for changing cursor icons at runtime
     * @author Yashkir Ramsamy
     * @param handCursor handCursor int
     */
    public void setVisualizerCursor(int handCursor) {
        UserView.pnlVizualizer.setCursor(Cursor.getPredefinedCursor(handCursor));
    }


    /**
     * Used to update EcoViz whenever critical repaint has to occur
     * @Author Yashkir Ramsamy
     */
    public void updateView(){
        if(!initialized){
            UserView.setPlantHeightSliderValues(FileLoader.getMinPlantHeight(),FileLoader.getMaxPlantHeight());
            UserView.setCanopyRadiusSliderValues(FileLoader.getMinCanopyRadius(),FileLoader.getMaxCanopyRadius());
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
     * @author Yashkir Ramsamy
     * @param menu Drop-down Menu String
     */
    public void changeMenu(String menu){
        CardLayout card = (CardLayout) UserView.pnlControls.getLayout();
        switch (menu) {
            case "Filters":
                card.show(UserView.pnlControls,"pnlFilters");
                UserView.chbControlsList.setSelectedIndex(1);
                break;
            case "Visibility":
                card.show(UserView.pnlControls,"pnlVisibility");
                UserView.chbControlsList.setSelectedIndex(3);
                break;
            case "Simulation":
                card.show(UserView.pnlControls,"pnlSimulation");
                UserView.chbControlsList.setSelectedIndex(4);
                break;
            case "Rendering":
                card.show(UserView.pnlControls,"pnlViewSettings");
                UserView.chbControlsList.setSelectedIndex(6);
                break;
            case "Legend":
                card.show(UserView.pnlControls,"pnlLegend");
                UserView.chbControlsList.setSelectedIndex(0);
                break;
            case "Plant Detail":
                card.show(UserView.pnlControls,"pnlPlantDetail");
                UserView.chbControlsList.setSelectedIndex(5);
                break;
            case "Help":
                card.show(UserView.pnlControls,"pnlHelp");
                UserView.chbControlsList.setSelectedIndex(7);
                break;
            case "Zoom Distance":
                card.show(UserView.pnlControls,"pnlZoom");
                UserView.chbControlsList.setSelectedIndex(2);
                break;
        }

    }

    /**
     * Takes file paths of data files and loads it in FileLoader
     * @param s .elv
     * @param s1 .spc
     * @param s2 .pdb (canopy)
     * @param s3 .pdb (undergrowth)
     */
    public static void loadFile(String s, String s1, String s2, String s3) {
       /* 0: .elv
        1: .spc
        2: .pdb (canopy)
        3: .pdb (undergrowth)*/
        /*String[] list = {s,s1,s2,s3};
        String[] path = {"elv","spc","pdb","pdb"};
        ExecutorService service = Executors.newFixedThreadPool(4);
        for(int i =0; i<4;i++){
            int finalI = i;
            service.execute(new Runnable() {
                @Override
                public void run() {
                    FileLoader.director(list[finalI],path[finalI]);
                }
            });
        }
        service.shutdown();

            service.awaitTermination(1, TimeUnit.MINUTES);*/


                FileLoader.readELV(s);


                FileLoader.readSPC(s1);


                FileLoader.readPdbCan(s2);
                FileLoader.convertTo1DCan();


                FileLoader.readPdbUnder(s3);
                FileLoader.convertTo1DUnder();



    }
}




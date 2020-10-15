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
 *
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
     * Constructor for classes which need utiltiy methods
     */
    public Controller() {

    }

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
    * Connects FileLoader to FileLoaderDialog
    */
    public void loadFile(String path, String fileType) {
        if (fileType.equals("elv")) {
            FileLoader.readELV(path);
        } else if (fileType.equals("spc")) {
            FileLoader.readSPC(path);
        } else if (fileType.equals("pdb")) {
            if (path.contains("undergrowth")) {
                FileLoader.readPdbUnder(path);

            }
            else if (path.contains("canopy")){
                FileLoader.readPdbCan(path);
               ;
            }
        }
        System.gc(); //clean arbitrary trash to optimise performance

    }

    /**
     * Disables control panel and settings menu on start to restrict the user to only entering files or exiting
     */
    public void restrictControls(boolean restricted){
        UserView.chbControlsList.setEnabled(!restricted);

    }

    /**
    * Used in determining whether EcoViz has been opened for the first time
    * Setter for initialized boolean
    */
    public static void setInitialization(boolean value){
        initialized = value;
    }
    /**
    * Used in determining whether EcoViz has been opened for the first time
    * Getter for initialized boolean
    */
    public static boolean checkInitialization(){
        return initialized;
    }


    /**
     * Displays a loading screen window
     * @param parentFrame
     */
    public void showLoadingScreen(java.awt.Frame parentFrame) throws IOException {
        JDialog loadingDialog = new JDialog(parentFrame, "Initializing EcoViz");

        JLabel info = new JLabel("<html>" +
                "<b>Initializing EcoViz</b>" +
                "<br>Loading files, please wait...");
        loadingDialog.setLayout(new GridBagLayout());
        //loadingDialog.add((Component) UIManager.getIcon("OptionPane.informationIcon"));
        loadingDialog.add(info);
        loadingDialog.setSize(250, 150);
        loadingDialog.setLocationRelativeTo(null);
        loadingDialog.setUndecorated(true);
        loadingDialog.setResizable(false);
        loadingDialog.setVisible(true);


        //JOptionPane.showOptionDialog(parentFrame, "Loading files, please wait...","Initializing EcoViz", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
        //initializeTerrainGrid();
    }

    /**
     *Utility method for detecting if dialog is open that is needed to be closed.
     *Creates a new timer thread to emulate loading time
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
        // };

    }


    public void print(String s){
        System.out.println("Controller: "+s);
    }

    /**
     * Generates the terrain visualisation and embeds it in a JLabel
     * @throws IOException
     */
    public void initializeTerrainGrid() throws IOException {
        print("initializeTerrainGrid");
        restrictControls(false);
        generateKey(FileLoader.getMinElevation(),FileLoader.getMaxElevation());
        UserView.pnlVizualizer.setGrid(new Grid(FileLoader.getDimx(),FileLoader.getDimy(),FileLoader.getSpacing(),FileLoader.getLatitude(), FileLoader.getTerrain()));
        updateView();
        closeLoadingScreen();
        UserView.pnlVizualizer.setPlants();
    }

    private void generateKey(double minElevation, double maxElevation) {
        UserView.lblElevationHeightMin.setText(String.format("%.2fm",minElevation));
        UserView.lblElevationHeightMax.setText(String.format("%.2fm",maxElevation));
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

    public void setVisualizerCursor(int handCursor) {
        UserView.pnlVizualizer.setCursor(Cursor.getPredefinedCursor(handCursor));
    }


    public void updateView(){
        //print("updateView");
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

    public void changeMenu(String menu){
        CardLayout card = (CardLayout) UserView.pnlControls.getLayout();
        switch (menu) {
            case "Filters":
                card.show(UserView.pnlControls,"pnlFilters");
                break;
            case "Visibility":
                card.show(UserView.pnlControls,"pnlVisibility");
                break;
            case "Simulation":
                card.show(UserView.pnlControls,"pnlSimulation");
                break;
            case "Rendering":
                card.show(UserView.pnlControls,"pnlViewSettings");
                break;
            case "Legend":
                card.show(UserView.pnlControls,"pnlLegend");
                break;
            case "Plant Detail":
                card.show(UserView.pnlControls,"pnlPlantDetail");
                break;
            case "Help":
                card.show(UserView.pnlControls,"pnlHelp");
                break;
            case "Zoom Distance":
                card.show(UserView.pnlControls,"pnlZoom");
                break;
        }
    }

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

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                FileLoader.readELV(s);
            }
        });
        t.start();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                FileLoader.readSPC(s1);
            }
        });
        t1.start();
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                FileLoader.readPdbCan(s2);
                FileLoader.convertTo1DCan();
            }
        });
        t2.start();
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                FileLoader.readPdbUnder(s3);
                FileLoader.convertTo1DUnder();
            }
        });
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}




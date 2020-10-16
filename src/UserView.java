/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.TableHeaderUI;

/**
 *
 * @author yashkir
 */
public class UserView extends JFrame{

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDefaultThreshold;
    private javax.swing.JButton btnViewPlantsWithinRadius;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JComboBox<String> cbxSimulationType;
    private javax.swing.JCheckBox chbCanopy;
    private javax.swing.JCheckBox chbRealisticTerrain;
    private javax.swing.JCheckBox chbUndergrowth;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblControlPanel;
    private javax.swing.JLabel lblCurrentZoomLevel;

    private javax.swing.JLabel lblFilters;
    private javax.swing.JLabel lblHelp;
    private javax.swing.JLabel lblHelpGuide;
    private javax.swing.JLabel lblMaxCanopy;
    private javax.swing.JLabel lblMinCanopy;

    private javax.swing.JLabel lblPlantHeightSlider2;
    private javax.swing.JLabel lblPlantHeightSlider3;
    private javax.swing.JLabel lblSelectedVis;
    private javax.swing.JLabel lblSelectedVisible;
    private javax.swing.JLabel lblSimType;
    private javax.swing.JLabel lblSimulation;
    private javax.swing.JLabel lblThreshold;
    private javax.swing.JLabel lblViewSettings;
    private javax.swing.JLabel lblZoom;
    private javax.swing.JMenu mbFIleOption;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem miExit;
    private javax.swing.JMenuItem miLoadFIles;
    private javax.swing.JMenuItem miRestart;

    private javax.swing.JPanel pnlFIlters;
    private javax.swing.JPanel pnlHelp;
    private javax.swing.JPanel pnlLegend;
    private javax.swing.JPanel pnlPlantDetail;
    private javax.swing.JPanel pnlPlantDetailControls;
    private javax.swing.JPanel pnlPlantDetailsLabel;

    private javax.swing.JPanel pnlSelectedFilters;
    private javax.swing.JScrollPane pnlSelectedVis;
    private javax.swing.JPanel pnlSimControls;
    private javax.swing.JPanel pnlSimulation;
    private javax.swing.JPanel pnlViewSettings;
    private javax.swing.JPanel pnlVisibility;

    private javax.swing.JPanel pnlZoom;
    private javax.swing.JPanel pnlZoomContextMap;
    private javax.swing.JSpinner sldRenderingThreshold;
    private javax.swing.JScrollPane tabFilterPlants;
    private javax.swing.JScrollPane tabFilterSpecies;
    private javax.swing.JTabbedPane tabbedFilterPane;
    private javax.swing.JTextField txtRadius;
    // End of variables declaration//GEN-END:variables

    //Protected Static components
    protected static VizPanel pnlVizualizer;
    protected static JSlider sldPlantHeight;
    protected static JList<String> listFilterPlants;
    protected static JList<String> listFilterSpecies;
    protected static JPanel pnlControls;
    protected static JComboBox<String> chbControlsList;
    protected static JSlider sldMaxCanopyRadius;
    protected static JSlider sldMinCanopyRadius;
    protected static JSlider sldPlantHeightMin;
    protected static JLabel lblPlantHeightValue;
    protected static JLabel lblCurrentZoomValue;
    protected static  javax.swing.JLabel lblElevationHeightMax;
    protected static  javax.swing.JLabel lblElevationHeightMin;
    protected static ElevationKeyPanel pnlElevationHeight;
    protected static javax.swing.JScrollPane pnlPlantLegendList;
    private static javax.swing.JLabel lblPlantDetails;

    //Self-declared Variables
    public static Controller localController;
    private boolean selectedSimType = false;
    private static int windSpeed;
    private static int windDirection;
    private ArrayList<String> selectedVisibilityPlants;
    public static boolean viewingPlantsWithinRadius;


    //Enum Types
    protected static enum Theme{
        DARK_MODE,
        LIGHT_MODE;
    }

    /**
     * Creates new form UserView
     */
    public UserView() {

        initComponents(); //Initializes GUI Components
        setTheme(Theme.DARK_MODE);
        setupListeners(); //Connects ActionListeners to Components
        setupMisc();
        //getContentPane().setBackground(new Color(56,60,74));
        localController = new Controller(this);
        localController.setVisualizerCursor(Cursor.DEFAULT_CURSOR);
        localController.restrictControls(true);
    }

    private void setTheme(Theme theme){
        ArrayList<Component> components = getAllComponents(this);
        if(theme == Theme.DARK_MODE){
            for (Component component:
                    components) {
                component.setBackground(new Color(56,60,74));
                component.setForeground(new Color(255,255,255));
            }
        }else if(theme == Theme.LIGHT_MODE){
            for (Component component:
                    components) {
                component.setBackground(new Color(242,242,242));
                component.setForeground(new Color(0,0,0));
            }
        }

    }

    public static ArrayList<Component> getAllComponents(final Container c) {
        Component[] comps = c.getComponents();
        ArrayList<Component> compList = new ArrayList<Component>();
        for (Component comp : comps) {
            compList.add(comp);
            if (comp instanceof Container)
                compList.addAll(getAllComponents((Container) comp));
        }
        return compList;
    }

    private void setupListeners() {
        /* Sliders */
        sldPlantHeight.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent evt) {
                sldPlantHeightStateChanged(evt);
            }
        });
        sldPlantHeightMin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sldPlantHeightMinStateChanged(e);
            }
        });

        sldMinCanopyRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sldMinCanopyRadiusStateChanged(e);
            }
        });

        sldMaxCanopyRadius.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                sldMaxCanopyRadiusStateChanged(e);

            }
        });

        /* Spinners */
        sldRenderingThreshold.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                sldRenderingThresholdStateChanged(evt);
                localController.updateView();
            }
        });

        /* JLists */
        listFilterSpecies.setModel(new AbstractListModel<String>() {
            String[] strings = { "Plants will be ", "listed here once ", "visualization files", "have been loaded" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listFilterSpecies.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent evt) {
                jListSpeciesSelect(evt);
            }
        });

        listFilterPlants.setModel(new AbstractListModel<String>() {
            String[] strings = { "Plants will be ", "listed here once ", "visualization files", "have been loaded" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        listFilterPlants.addListSelectionListener(evt -> jListPlantSelect(evt));

        /* Buttons */

        btnDefaultThreshold.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                btnDefaultThresholdActionPerformed(actionEvent);
            }
        });

        btnZoomIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                zoomInButtonClick();
            }
        });
        btnZoomOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                zoomOutButtonClick();
            }
        });

        jButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jButton1ActionPerformed(actionEvent);
            }
        });

        btnViewPlantsWithinRadius.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                btnViewPlantsWithinRadiusActionPeformed(actionEvent);
            }
        });

        /* ComboBox */

        chbControlsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                chbControlsListActionPerformed();
            }
        });

        cbxSimulationType.setModel(new DefaultComboBoxModel<String>(new String[] { "None", "Fire" }));
        cbxSimulationType.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                cbxSimulationTypeActionPerformed(evt);
            }
        });

        /* Check Boxes */
        chbCanopy.doClick();
        pnlVizualizer.canopyCHB = true;
        chbCanopy.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chbCanopyActionPerformed(evt);
            }
        });

        chbUndergrowth.doClick();
        pnlVizualizer.undergrowthCHB = true;
        chbUndergrowth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                chbUndergrowthActionPerformed(evt);
            }
        });

        chbRealisticTerrain.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent itemEvent) {
                chbRealisticTerrainStateChange(itemEvent);
            }
        });

        /* Menu Items
        miLoadFIles.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                miLoadFIlesActionPerformed(evt);
            }
        });

        miRestart.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                miRestartActionPerformed(evt);
            }
        });

        miExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });*/

    }

    private void btnViewPlantsWithinRadiusActionPeformed(ActionEvent actionEvent) {
        viewingPlantsWithinRadius = true;
        pnlVizualizer.plantWithinRadVal = Integer.valueOf(txtRadius.getText());
        jButton1.setEnabled(true);
        localController.updateView();
    }

    private void jButton1ActionPerformed(ActionEvent actionEvent) {
        viewingPlantsWithinRadius = false;
        txtRadius.setText("0");
        pnlVizualizer.plantWithinRadVal = 0;
        jButton1.setEnabled(false);
        localController.updateView();
    }


    private void btnDefaultThresholdActionPerformed(ActionEvent actionEvent) {
        sldRenderingThreshold.setValue(1);
        localController.updateView();
    }

    private void chbRealisticTerrainStateChange(ItemEvent itemEvent) {
        if(itemEvent.getStateChange() == ItemEvent.SELECTED){
            pnlVizualizer.setTerrainRenderType(2);
        }else{
            pnlVizualizer.setTerrainRenderType(1);
        }

        localController.updateView();
    }

    private void setupMisc(){
        this.setLocationRelativeTo(null);
        selectedVisibilityPlants = new ArrayList<>();
        chbControlsList.setSelectedIndex(chbControlsList.getItemCount()-1);
        //sldRenderingThreshold.setValue(1);
    }

    private void chbControlsListActionPerformed() {
        CardLayout card = (CardLayout) pnlControls.getLayout();
        switch (Objects.requireNonNull(chbControlsList.getSelectedItem()).toString()) {
            case "Filters":
                card.show(pnlControls,"pnlFilters");
                break;
            case "Visibility":
                card.show(pnlControls,"pnlVisibility");
                break;
            case "Simulation":
                card.show(pnlControls,"pnlSimulation");
                break;
            case "Rendering":
                card.show(pnlControls,"pnlViewSettings");
                break;
            case "Legend":
                card.show(pnlControls,"pnlLegend");
                break;
            case "Plant Detail":
                card.show(pnlControls,"pnlPlantDetail");
                break;
            case "Help":
                card.show(pnlControls,"pnlHelp");
                break;
            case "Zoom Distance":
                card.show(pnlControls,"pnlZoom");
                break;
        }
    }


    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        pnlVizualizer = new VizPanel();
        jPanel2 = new javax.swing.JPanel();
        chbControlsList = new javax.swing.JComboBox<String>();
        lblControlPanel = new javax.swing.JLabel();
        pnlControls = new javax.swing.JPanel();
        pnlHelp = new javax.swing.JPanel();
        lblHelp = new javax.swing.JLabel();
        lblHelpGuide = new javax.swing.JLabel();
        pnlFIlters = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        lblPlantHeightSlider3 = new javax.swing.JLabel();
        sldPlantHeight = new javax.swing.JSlider();
        jPanel5 = new javax.swing.JPanel();
        sldPlantHeightMin = new javax.swing.JSlider();
        lblPlantHeightSlider2 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        sldMaxCanopyRadius = new javax.swing.JSlider();
        lblMaxCanopy = new javax.swing.JLabel();
        pnlSelectedFilters = new javax.swing.JPanel();
        lblPlantHeightValue = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        sldMinCanopyRadius = new javax.swing.JSlider();
        lblMinCanopy = new javax.swing.JLabel();
        lblFilters = new javax.swing.JLabel();
        pnlViewSettings = new javax.swing.JPanel();
        lblViewSettings = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnDefaultThreshold = new javax.swing.JButton();
        sldRenderingThreshold = new javax.swing.JSpinner();
        lblThreshold = new javax.swing.JLabel();
        chbRealisticTerrain = new javax.swing.JCheckBox();
        pnlSimulation = new javax.swing.JPanel();
        cbxSimulationType = new javax.swing.JComboBox<String>();
        lblSimType = new javax.swing.JLabel();
        pnlSimControls = new javax.swing.JPanel();
        lblSimulation = new javax.swing.JLabel();
        pnlVisibility = new javax.swing.JPanel();
        chbCanopy = new javax.swing.JCheckBox();
        chbUndergrowth = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        tabbedFilterPane = new javax.swing.JTabbedPane();
        tabFilterSpecies = new javax.swing.JScrollPane();
        listFilterSpecies = new javax.swing.JList<String>();
        tabFilterPlants = new javax.swing.JScrollPane();
        listFilterPlants = new javax.swing.JList<String>();
        lblSelectedVisible = new javax.swing.JLabel();
        pnlSelectedVis = new javax.swing.JScrollPane();
        lblSelectedVis = new javax.swing.JLabel();
        pnlPlantDetail = new javax.swing.JPanel();
        pnlPlantDetailControls = new javax.swing.JPanel();
        txtRadius = new javax.swing.JTextField();
        txtRadius.setText("0");
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnViewPlantsWithinRadius = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lblPlantDetails = new javax.swing.JLabel();
        pnlLegend = new javax.swing.JPanel();
        pnlElevationHeight = new ElevationKeyPanel();
        lblElevationHeightMin = new javax.swing.JLabel();
        lblElevationHeightMax = new javax.swing.JLabel();
        pnlPlantLegendList = new javax.swing.JScrollPane();
        pnlZoom = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        lblZoom = new javax.swing.JLabel();
        btnZoomIn = new javax.swing.JButton();
        btnZoomOut = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        lblCurrentZoomLevel = new javax.swing.JLabel();
        lblCurrentZoomValue = new javax.swing.JLabel();
        pnlZoomContextMap = new javax.swing.JPanel();
        menuBar = new javax.swing.JMenuBar();
        mbFIleOption = new javax.swing.JMenu();
        miLoadFIles = new javax.swing.JMenuItem();
        miRestart = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EcoViz");
        setPreferredSize(new java.awt.Dimension(1024, 800));

        pnlVizualizer.setName("");

        javax.swing.GroupLayout pnlVizualizerLayout = new javax.swing.GroupLayout(pnlVizualizer);
        pnlVizualizer.setLayout(pnlVizualizerLayout);
        pnlVizualizerLayout.setHorizontalGroup(
                pnlVizualizerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 718, Short.MAX_VALUE)
        );
        pnlVizualizerLayout.setVerticalGroup(
                pnlVizualizerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        chbControlsList.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "Legend", "Filters", "Zoom Distance", "Visibility", "Simulation", "Plant Detail", "Rendering", "Help" }));

        lblControlPanel.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblControlPanel.setText("Settings");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(chbControlsList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblControlPanel)
                                .addGap(102, 102, 102))
        );
        jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblControlPanel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(chbControlsList)
                                .addGap(26, 26, 26))
        );

        pnlControls.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlControls.setForeground(new java.awt.Color(56, 60, 74));
        pnlControls.setLayout(new java.awt.CardLayout());

        pnlHelp.setBorder(null);

        lblHelp.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblHelp.setText("Help Guide");

        lblHelpGuide.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblHelpGuide.setText("<html>\n<p style=\"text-align: center;\"><strong>EcoViz - Forest Ecosystem Visualizer</strong></p>\n<p style=\"text-align: center;\">&nbsp;Load files by clicking on File &gt; Load Files</p>\n<p style=\"text-align: justify;\">&nbsp;</p>\n<p style=\"text-align: justify;\"><strong>Settings</strong></p>\n<p style=\"text-align: justify;\">Select an option in the above drop-down menu to browse through visualizer options.</p>\n<p style=\"text-align: justify;\"><br /> <strong>Filters</strong></p>\n<p style=\"text-align: justify;\">Filter through the visualizer by plant height and canopy radii.</p>\n<p style=\"text-align: justify;\"><br /><strong> Visibility</strong></p>\n<p style=\"text-align: justify;\">View specific forest layers or specific species or genus.</p>\n<p style=\"text-align: justify;\"><br /><strong> Simulation</strong></p>\n<p style=\"text-align: justify;\">Start and control a fire simulation.</p>\n<p style=\"text-align: justify;\"><br /><strong> Legend</strong></p>\n<p style=\"text-align: justify;\">View the visualizer key.</p>\n<p style=\"text-align: justify;\"><br /><strong> Plant Detail</strong></p>\n<p style=\"text-align: justify;\">View specific plant details.&nbsp;</p>\n<p style=\"text-align: justify;\">&nbsp;</p>\n<p style=\"text-align: justify;\"><strong> Zoom</strong></p>\n<p style=\"text-align: justify;\">View the zoom level and context map.</p>\n<p style=\"text-align: justify;\"><br /><strong> Help</strong></p>\n<p style=\"text-align: justify;\">A helpful guideline for EcoViz functions.</p>");

        javax.swing.GroupLayout pnlHelpLayout = new javax.swing.GroupLayout(pnlHelp);
        pnlHelp.setLayout(pnlHelpLayout);
        pnlHelpLayout.setHorizontalGroup(
                pnlHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHelpLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(lblHelp)
                                .addContainerGap(89, Short.MAX_VALUE))
                        .addGroup(pnlHelpLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblHelpGuide, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                                .addContainerGap())
        );
        pnlHelpLayout.setVerticalGroup(
                pnlHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHelpLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblHelp)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblHelpGuide, javax.swing.GroupLayout.DEFAULT_SIZE, 589, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pnlControls.add(pnlHelp, "pnlHelp");

        pnlFIlters.setBorder(null);

        lblPlantHeightSlider3.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblPlantHeightSlider3.setText("<html>Max Plant<br> Height");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(lblPlantHeightSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel6Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblPlantHeightSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10))
        );

        sldPlantHeight.setPaintLabels(true);
        sldPlantHeight.setPaintTicks(true);
        sldPlantHeight.setValue(0);
        sldPlantHeight.setBorder(null);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sldPlantHeight, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(sldPlantHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        sldPlantHeightMin.setPaintLabels(true);
        sldPlantHeightMin.setPaintTicks(true);
        sldPlantHeightMin.setValue(0);
        sldPlantHeightMin.setBorder(null);

        lblPlantHeightSlider2.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblPlantHeightSlider2.setText("<html>Min Plant<br> Height");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addComponent(lblPlantHeightSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(sldPlantHeightMin, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sldPlantHeightMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblPlantHeightSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        sldMaxCanopyRadius.setPaintLabels(true);
        sldMaxCanopyRadius.setPaintTicks(true);
        sldMaxCanopyRadius.setValue(0);
        sldMaxCanopyRadius.setBorder(null);

        lblMaxCanopy.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblMaxCanopy.setText("<html>Max Canopy<br> Radius");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(lblMaxCanopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sldMaxCanopyRadius, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel7Layout.setVerticalGroup(
                jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sldMaxCanopyRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMaxCanopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pnlSelectedFilters.setLayout(new java.awt.GridBagLayout());

        lblPlantHeightValue.setText("Selected filters will appear here");
        pnlSelectedFilters.add(lblPlantHeightValue, new java.awt.GridBagConstraints());

        sldMinCanopyRadius.setPaintLabels(true);
        sldMinCanopyRadius.setPaintTicks(true);
        sldMinCanopyRadius.setValue(0);
        sldMinCanopyRadius.setBorder(null);

        lblMinCanopy.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblMinCanopy.setText("<html>Min Canopy<br> Radius");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel9Layout.createSequentialGroup()
                                .addComponent(lblMinCanopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sldMinCanopyRadius, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel9Layout.setVerticalGroup(
                jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel9Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(sldMinCanopyRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblMinCanopy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        lblFilters.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblFilters.setText("Filters");

        javax.swing.GroupLayout pnlFIltersLayout = new javax.swing.GroupLayout(pnlFIlters);
        pnlFIlters.setLayout(pnlFIltersLayout);
        pnlFIltersLayout.setHorizontalGroup(
                pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                .addGroup(pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(pnlSelectedFilters, javax.swing.GroupLayout.PREFERRED_SIZE, 297, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                                .addGap(119, 119, 119)
                                                .addComponent(lblFilters)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFIltersLayout.createSequentialGroup()
                                .addGroup(pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFIltersLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                                .addGap(12, 12, 12)
                                                .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                .addGap(57, 57, 57))
        );
        pnlFIltersLayout.setVerticalGroup(
                pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblFilters)
                                .addGap(76, 76, 76)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jPanel9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(pnlSelectedFilters, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE))
        );

        pnlControls.add(pnlFIlters, "pnlFilters");

        lblViewSettings.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblViewSettings.setText("Rendering");

        btnDefaultThreshold.setText("⟲");
        btnDefaultThreshold.setToolTipText("Reset to default");

        sldRenderingThreshold.setName(""); // NOI18N

        lblThreshold.setText("<html>Rendering<br>Threshold");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(lblThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(sldRenderingThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 66, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDefaultThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lblThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDefaultThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(sldRenderingThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );

        chbRealisticTerrain.setText("Gradient View");
        chbRealisticTerrain.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        chbRealisticTerrain.setIconTextGap(68);

        javax.swing.GroupLayout pnlViewSettingsLayout = new javax.swing.GroupLayout(pnlViewSettings);
        pnlViewSettings.setLayout(pnlViewSettingsLayout);
        pnlViewSettingsLayout.setHorizontalGroup(
                pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                .addGroup(pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                                .addGap(96, 96, 96)
                                                .addComponent(lblViewSettings))
                                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                                .addGap(44, 44, 44)
                                                .addGroup(pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(chbRealisticTerrain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addContainerGap(54, Short.MAX_VALUE))
        );
        pnlViewSettingsLayout.setVerticalGroup(
                pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblViewSettings)
                                .addGap(26, 26, 26)
                                .addComponent(chbRealisticTerrain, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(492, Short.MAX_VALUE))
        );

        pnlControls.add(pnlViewSettings, "pnlViewSettings");

        cbxSimulationType.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "None", "Fire" }));
        cbxSimulationType.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSimulationTypeActionPerformed(evt);
            }
        });

        lblSimType.setText("Type");

        javax.swing.GroupLayout pnlSimControlsLayout = new javax.swing.GroupLayout(pnlSimControls);
        pnlSimControls.setLayout(pnlSimControlsLayout);
        pnlSimControlsLayout.setHorizontalGroup(
                pnlSimControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlSimControlsLayout.setVerticalGroup(
                pnlSimControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 196, Short.MAX_VALUE)
        );

        lblSimulation.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblSimulation.setText("Simulations");

        javax.swing.GroupLayout pnlSimulationLayout = new javax.swing.GroupLayout(pnlSimulation);
        pnlSimulation.setLayout(pnlSimulationLayout);
        pnlSimulationLayout.setHorizontalGroup(
                pnlSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlSimulationLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlSimulationLayout.createSequentialGroup()
                                                .addComponent(lblSimType)
                                                .addGap(18, 18, 18)
                                                .addComponent(cbxSimulationType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(pnlSimControls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
                        .addGroup(pnlSimulationLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(lblSimulation)
                                .addContainerGap(83, Short.MAX_VALUE))
        );
        pnlSimulationLayout.setVerticalGroup(
                pnlSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlSimulationLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblSimulation)
                                .addGap(18, 18, 18)
                                .addGroup(pnlSimulationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbxSimulationType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblSimType))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(pnlSimControls, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(351, Short.MAX_VALUE))
        );

        pnlControls.add(pnlSimulation, "pnlSimulation");

        chbCanopy.setText("Canopy");

        chbUndergrowth.setText("Undergrowth");

        jLabel2.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        jLabel2.setText("Visibility");

        listFilterSpecies.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tabFilterSpecies.setViewportView(listFilterSpecies);

        tabbedFilterPane.addTab("Species", tabFilterSpecies);

        listFilterPlants.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        tabFilterPlants.setViewportView(listFilterPlants);

        tabbedFilterPane.addTab("Plants", tabFilterPlants);

        lblSelectedVisible.setText("Selected Options");

        pnlSelectedVis.setViewportView(lblSelectedVis);

        javax.swing.GroupLayout pnlVisibilityLayout = new javax.swing.GroupLayout(pnlVisibility);
        pnlVisibility.setLayout(pnlVisibilityLayout);
        pnlVisibilityLayout.setHorizontalGroup(
                pnlVisibilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(tabbedFilterPane, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                .addGroup(pnlVisibilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(pnlSelectedVis))
                                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                                .addGroup(pnlVisibilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                                                .addGap(30, 30, 30)
                                                                .addComponent(chbUndergrowth)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(chbCanopy))
                                                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                                                .addGap(111, 111, 111)
                                                                .addComponent(jLabel2)))
                                                .addGap(0, 37, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVisibilityLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(lblSelectedVisible)
                                .addGap(86, 86, 86))
        );
        pnlVisibilityLayout.setVerticalGroup(
                pnlVisibilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(jLabel2)
                                .addGap(29, 29, 29)
                                .addGroup(pnlVisibilityLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(chbCanopy)
                                        .addComponent(chbUndergrowth))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(tabbedFilterPane, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblSelectedVisible)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlSelectedVis, javax.swing.GroupLayout.DEFAULT_SIZE, 209, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pnlControls.add(pnlVisibility, "pnlVisibility");

        pnlPlantDetail.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnlPlantDetailControls.setPreferredSize(new java.awt.Dimension(0, 250));

        jLabel1.setText("m");

        jLabel4.setText("<html>View plants<br> within radius of:");

        btnViewPlantsWithinRadius.setText("View");

        jButton1.setText("\t⟲");

        javax.swing.GroupLayout pnlPlantDetailControlsLayout = new javax.swing.GroupLayout(pnlPlantDetailControls);
        pnlPlantDetailControls.setLayout(pnlPlantDetailControlsLayout);
        pnlPlantDetailControlsLayout.setHorizontalGroup(
                pnlPlantDetailControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPlantDetailControlsLayout.createSequentialGroup()
                                .addGap(0, 37, Short.MAX_VALUE)
                                .addGroup(pnlPlantDetailControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlPlantDetailControlsLayout.createSequentialGroup()
                                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txtRadius, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlPlantDetailControlsLayout.createSequentialGroup()
                                                .addComponent(jButton1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnViewPlantsWithinRadius)))
                                .addGap(4, 4, 4)
                                .addComponent(jLabel1)
                                .addGap(35, 35, 35))
        );
        pnlPlantDetailControlsLayout.setVerticalGroup(
                pnlPlantDetailControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlPlantDetailControlsLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(pnlPlantDetailControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(txtRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(pnlPlantDetailControlsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnViewPlantsWithinRadius)
                                        .addComponent(jButton1))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        lblPlantDetails.setText("<html>Selected Plants will<br> appear here ");
        jScrollPane1.setViewportView(lblPlantDetails);

        javax.swing.GroupLayout pnlPlantDetailLayout = new javax.swing.GroupLayout(pnlPlantDetail);
        pnlPlantDetail.setLayout(pnlPlantDetailLayout);
        pnlPlantDetailLayout.setHorizontalGroup(
                pnlPlantDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlPlantDetailLayout.createSequentialGroup()
                                .addComponent(pnlPlantDetailControls, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(pnlPlantDetailLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        pnlPlantDetailLayout.setVerticalGroup(
                pnlPlantDetailLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlPlantDetailLayout.createSequentialGroup()
                                .addComponent(pnlPlantDetailControls, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 522, Short.MAX_VALUE))
        );

        pnlControls.add(pnlPlantDetail, "pnlPlantDetail");

        pnlElevationHeight.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlElevationHeightLayout = new javax.swing.GroupLayout(pnlElevationHeight);
        pnlElevationHeight.setLayout(pnlElevationHeightLayout);
        pnlElevationHeightLayout.setHorizontalGroup(
                pnlElevationHeightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlElevationHeightLayout.setVerticalGroup(
                pnlElevationHeightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 34, Short.MAX_VALUE)
        );

        lblElevationHeightMin.setText("Min");

        lblElevationHeightMax.setText("Max");

        javax.swing.GroupLayout pnlLegendLayout = new javax.swing.GroupLayout(pnlLegend);
        pnlLegend.setLayout(pnlLegendLayout);
        pnlLegendLayout.setHorizontalGroup(
                pnlLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLegendLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(pnlLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(pnlPlantLegendList)
                                        .addComponent(pnlElevationHeight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlLegendLayout.createSequentialGroup()
                                                .addComponent(lblElevationHeightMin)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 218, Short.MAX_VALUE)
                                                .addComponent(lblElevationHeightMax)))
                                .addContainerGap())
        );
        pnlLegendLayout.setVerticalGroup(
                pnlLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLegendLayout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addGroup(pnlLegendLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblElevationHeightMin)
                                        .addComponent(lblElevationHeightMax))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlElevationHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(pnlPlantLegendList, javax.swing.GroupLayout.DEFAULT_SIZE, 542, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pnlControls.add(pnlLegend, "pnlLegend");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 94, Short.MAX_VALUE)
        );

        lblZoom.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblZoom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblZoom.setText("Zoom");

        btnZoomIn.setText("+");

        btnZoomOut.setText("-");

        lblCurrentZoomLevel.setText("Current Zoom Level:");

        lblCurrentZoomValue.setText("0%");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addContainerGap(51, Short.MAX_VALUE)
                                .addComponent(lblCurrentZoomLevel)
                                .addGap(39, 39, 39)
                                .addComponent(lblCurrentZoomValue)
                                .addGap(45, 45, 45))
        );
        jPanel8Layout.setVerticalGroup(
                jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel8Layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCurrentZoomLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lblCurrentZoomValue))
                                .addGap(0, 0, 0))
        );

        javax.swing.GroupLayout pnlZoomContextMapLayout = new javax.swing.GroupLayout(pnlZoomContextMap);
        pnlZoomContextMap.setLayout(pnlZoomContextMapLayout);
        pnlZoomContextMapLayout.setHorizontalGroup(
                pnlZoomContextMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlZoomContextMapLayout.setVerticalGroup(
                pnlZoomContextMapLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlZoomLayout = new javax.swing.GroupLayout(pnlZoom);
        pnlZoom.setLayout(pnlZoomLayout);
        pnlZoomLayout.setHorizontalGroup(
                pnlZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                .addGroup(pnlZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                                .addGap(119, 119, 119)
                                                .addComponent(lblZoom))
                                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                                .addGap(80, 80, 80)
                                                .addComponent(btnZoomOut, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(pnlZoomContextMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGap(236, 236, 236))
        );
        pnlZoomLayout.setVerticalGroup(
                pnlZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblZoom)
                                .addGap(18, 18, 18)
                                .addGroup(pnlZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnZoomOut)
                                        .addComponent(btnZoomIn))
                                .addGap(22, 22, 22)
                                .addGroup(pnlZoomLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap(441, Short.MAX_VALUE))
                                        .addGroup(pnlZoomLayout.createSequentialGroup()
                                                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(pnlZoomContextMap, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pnlControls.add(pnlZoom, "pnlZoom");

        menuBar.setName(""); // NOI18N
        JMenu mbPreferencesOption = new JMenu();
        mbPreferencesOption.setText("Preferences");
        JMenu miTheme = new JMenu();
        miTheme.setText("Theme");
        mbPreferencesOption.add(miTheme);
        JMenuItem darkMode = new JMenuItem();
        darkMode.setText("Dark");
        JMenuItem lightMode = new JMenuItem();
        lightMode.setText("Light");
        miTheme.add(darkMode);
        miTheme.add(lightMode);
        darkMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTheme(Theme.DARK_MODE);
            }
        });
        lightMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setTheme(Theme.LIGHT_MODE);
            }
        });
        mbFIleOption.setText("File");


        miLoadFIles.setText("Load Files");
        miLoadFIles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miLoadFIlesActionPerformed(evt);
            }
        });
        mbFIleOption.add(miLoadFIles);

        miRestart.setText("Restart");
        miRestart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miRestartActionPerformed(evt);
            }
        });
        mbFIleOption.add(miRestart);

        miExit.setText("Exit");
        miExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                miExitActionPerformed(evt);
            }
        });
        mbFIleOption.add(miExit);

        menuBar.add(mbFIleOption);
        menuBar.add(mbPreferencesOption);
        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(pnlVizualizer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(pnlControls, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(pnlVizualizer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(pnlControls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>     // </editor-fold>

    public static void setLblPlantDetails(String s){
        lblPlantDetails.setText(s);
    }

    private void sldRenderingThresholdStateChanged(ChangeEvent evt) {
        JSpinner source = (JSpinner) evt.getSource(); //gets the event type
        int value = (Integer) source.getValue();
        pnlVizualizer.setViewingThreshold(value);
    }

    private void zoomOutButtonClick() {
        pnlVizualizer.zoomOutTenPercent();
        lblCurrentZoomValue.setText(pnlVizualizer.getZoomPercentage());
    }

    private void zoomInButtonClick() {
        pnlVizualizer.zoomInTenPercent();
        lblCurrentZoomValue.setText(pnlVizualizer.getZoomPercentage());
    }

    public static void updateZoomLevel(){
        lblCurrentZoomValue.setText(pnlVizualizer.getZoomPercentage());
    }


    /**JList methods for filtering through species.
     * Access Fileloader static variables
     * @param evt
     */
    private void jListPlantSelect(ListSelectionEvent evt) {
        //set text on right here
        String s = (String) listFilterPlants.getSelectedValue();
        updateSelectedVisibilityPlants(s);
        if(s==null){return;}
        String[][] spc = FileLoader.getSpcKey();
        for(int i=0;i<spc.length;i++){
            if(s.equals(spc[i][1].split(" ")[0])&&!FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,true);
                break;
            }
            else if(s.equals(spc[i][1].split(" ")[0])&&FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,false);
                break;
            }
        }

        updateSelectedVisibilitySettings();
        localController.updateView();
    }
    private void jListSpeciesSelect(ListSelectionEvent evt) {
        //set text on right here
        String s = (String) listFilterSpecies.getSelectedValue();
        updateSelectedVisibilityPlants(s);
        if(s==null){return;}
        String[][] spc = FileLoader.getSpcKey();
        for(int i=0;i<spc.length;i++){
            if(s.equals(spc[i][0])&&!FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,true);

                break;
            }
            else if(s.equals(spc[i][0])&&FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,false);
                break;

            }
        }
        updateSelectedVisibilitySettings();
        localController.updateView();
    }

    /**
     * Construct and display label on pnlSelectedVis to show selected hidden plants
     * @author Yashkir Ramsamy
     */
    private void updateSelectedVisibilitySettings(){
        String label = "";
        for (String s:
             selectedVisibilityPlants) {
            if(s != null){
                label = label+"<br>"+s;
            }

        }
        System.out.println(label);
        lblSelectedVis.setText("<html>"+label);
    }

    /**
     * Used to determine whether an item has been added to the list of hidden plants
     * remove if already added
     * @author Yashkir Ramsamy
     * @param s
     */
    private void updateSelectedVisibilityPlants(String s){
        if(selectedVisibilityPlants.contains(s)){
            selectedVisibilityPlants.remove(s);
        }else{
            selectedVisibilityPlants.add(s);
        }

    }

    /**
     * onClickEventListener for "File" menu item
     * Opens FileLoaderDialog and sets it to visible
     * @param evt
     */
    private void miLoadFIlesActionPerformed(ActionEvent evt) {//GEN-FIRST:event_miLoadFIlesActionPerformed
        FileLoaderDialog dialog = new FileLoaderDialog(this, rootPaneCheckingEnabled);
        dialog.setVisible(true);// TODO add your handling code here:
    }//GEN-LAST:event_miLoadFIlesActionPerformed
    /**
     * onClickEventListener for "Exit" menu item
     * Exits application
     * @param evt
     */
    private void miExitActionPerformed(ActionEvent evt) {//GEN-FIRST:event_miExitActionPerformed
        System.exit(0);        // TODO add your handling code here:
    }//GEN-LAST:event_miExitActionPerformed
    /**
     * onClickEventListener for Simulation Combobox
     * Gets selected type and displays the relevant controls for the simulation
     * @param evt
     */
    private void cbxSimulationTypeActionPerformed(ActionEvent evt) {//GEN-FIRST:event_cbxSimulationTypeActionPerformed
        switch(cbxSimulationType.getSelectedItem().toString())
        {
            case "None":
                selectedSimType = false;
                for (Component component: pnlSimControls.getComponents()) {
                    if(component instanceof JButton){
                        pnlSimControls.remove(component);
                    }
                }
                pnlSimControls.revalidate();
                pnlSimControls.repaint();
                 break;
            case "Fire":

                if(!selectedSimType){
                    pnlSimControls.setLayout(new BoxLayout(pnlSimControls,BoxLayout.PAGE_AXIS));
                    JButton btnWind = new JButton("Set Wind");
                    JButton btnStartFire= new JButton("Start Fire");
                    btnWind.setAlignmentX(Component.CENTER_ALIGNMENT);
                    btnStartFire.setAlignmentX(Component.CENTER_ALIGNMENT);
                    btnStartFire.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent actionEvent) {
                            //pnlVizualizer.startFireClicked = true;
                            JButton pauseButton = new JButton("Pause");
                            JButton playButton = new JButton("Play");
                            JButton resetButton = new JButton("Reset");
                            JButton stopButton = new JButton("Stop");

                            pauseButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    pnlVizualizer.fire.paused = true;
                                    pnlVizualizer.fire.stopped = false;
                                }
                            });

                            playButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    pnlVizualizer.fire.paused = false;
                                    pnlVizualizer.fire.stopped = false;
                                }
                            });

                            resetButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    pnlVizualizer.fire.paused = false;
                                    pnlVizualizer.fire.stopped = true;
                                    pnlVizualizer.fire.resetFireLayer();
                                }
                            });

                            stopButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    pnlVizualizer.fire.paused = false;
                                    pnlVizualizer.fire.stopped = true;
                                }
                            });

                            pnlSimControls.add(playButton);
                            pnlSimControls.add(pauseButton);
                            //pnlSimControls.add(resetButton);
                            pnlSimControls.add(stopButton);

                            pnlSimControls.revalidate();
                            pnlSimControls.repaint();

                            //playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                            //pauseButton.setAlignmentX(Component.CENTER_ALIGNMENT);
                            //pauseButton.setVisible(true);
                            startFireSim();
                        }
                    });
                    btnWind.addActionListener(actionEvent -> {
                        WindSetDialog dialog = new WindSetDialog(UserView.this,rootPaneCheckingEnabled);
                        dialog.setVisible(true);
                        windSpeed = dialog.getwindSpeed();
                        windDirection = dialog.getwindDirection();

                    });
                    pnlSimControls.add(btnWind);
                    pnlSimControls.add(btnStartFire);
                    pnlSimControls.revalidate();
                    pnlSimControls.repaint();
                }
                selectedSimType = true;
                break;


        }// TODO add your handling code here:
    }//GEN-LAST:event_cbxSimulationTypeActionPerformed

    private void startFireSim() {
        Thread fire = new Thread(pnlVizualizer.fire);

        pnlVizualizer.fire.setWindX(UserView.getWindSpeed(),UserView.getWindDirection());
        pnlVizualizer.fire.setWindY(UserView.getWindSpeed(),UserView.getWindDirection());
        pnlVizualizer.startFireClicked=true;
        fire.start();

    }

    public static int getWindSpeed(){
        return windSpeed;
    }

    public static int getWindDirection(){
        return windDirection;
    }
    /**
    * onClickEventListener for "Restart" menu item
    * Restarts application 
    * @param evt
    */
    private void miRestartActionPerformed(ActionEvent evt) {//GEN-FIRST:event_miRestartActionPerformed
          if(JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?") == JOptionPane.YES_OPTION){
              // TODO: Write clear program code here
          }
        
    }//GEN-LAST:event_miRestartActionPerformed

    private void chbCanopyActionPerformed(ActionEvent evt) {//GEN-FIRST:event_chbCanopyActionPerformed
        // TODO add your handling code here:
        pnlVizualizer.canopyCHB = chbCanopy.isSelected();
        localController.updateView();
    }//GEN-LAST:event_chbCanopyActionPerformed

    private void chbUndergrowthActionPerformed(ActionEvent evt) {//GEN-FIRST:event_chbCanopyActionPerformed
        // TODO add your handling code here:
        pnlVizualizer.undergrowthCHB = chbUndergrowth.isSelected();
        localController.updateView();
    }//GEN-LAST:event_chbCanopyActionPerformed

    private void sldPlantHeightStateChanged(ChangeEvent evt) {
        JSlider source = (JSlider) evt.getSource(); //gets the event type
        pnlVizualizer.heightSliderValue = source.getValue();
        setSelectedFilterLabel();
        //sliders only work in integers
        if(source.getValue()<=sldPlantHeight.getMinimum()){
             //if slider value minimum, slider label resets to default
        }else{
            setSelectedFilterLabel();
        }
        pnlVizualizer.repaint();
    }
    private void setSelectedFilterLabel(){
        String plantHeightMax = Integer.toString(sldPlantHeight.getValue());
        String plantHeightMin = Integer.toString(sldPlantHeightMin.getValue());
        String canopyRadiusMin = Integer.toString(sldMinCanopyRadius.getValue());
        String canopyRadiusMax = Integer.toString(sldMaxCanopyRadius.getValue());
        String[] list = {plantHeightMin,plantHeightMax,canopyRadiusMin,canopyRadiusMax};
        for(int i = 0; i<4;i++){

            if(list[i].equals("-1")|| list[i].equals("0")){
                list[i] = "None";
            }
        }
        String label = "<html>"+"<p><strong>Plant Height: </strong></p>\n" +
                "<p>Min: %1$s</p>\n" +
                "<p>Max: %2$s</p>\n" +
                "<p><b>Range:</b> %1$s - %2$s</p>\n"+
                "<br>\n"+
                "<p><strong>Canopy Radius:</strong>&nbsp;</p>\n" +
                "<p><b>Min</b>: %3$s</p>\n" +
                "<p><b>Max</b>: %4$s</p>"+
                "<p><b>Range:</b> %3$s - %4$s</p>\n";
        lblPlantHeightValue.setText(String.format(label,list[0],list[1],list[2],list[3]));
    }

    public static void setFilterLabel(String s){
        lblPlantHeightValue.setText(s);
    }


    private void sldPlantHeightMinStateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource(); //gets the event type
        pnlVizualizer.heightMinSliderValue = source.getValue();
        //sliders only work in integers
        if(source.getValue()<=sldPlantHeightMin.getMinimum()){
            //if slider value minimum, slider label resets to default
        }else{
            setSelectedFilterLabel();
        }
        pnlVizualizer.repaint();
    }

    private void sldMinCanopyRadiusStateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource(); //gets the event type
        pnlVizualizer.canopyMinSliderValue = source.getValue();
        //sliders only work in integers
        if(source.getValue()<=sldMinCanopyRadius.getMinimum()){
            //if slider value minimum, slider label resets to default
        }else{
            setSelectedFilterLabel();
        }
        pnlVizualizer.repaint();
    }

    private void sldMaxCanopyRadiusStateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource(); //gets the event type
        pnlVizualizer.canopyMaxSliderValue = source.getValue();
        //sliders only work in integers
        if(source.getValue()<=sldMaxCanopyRadius.getMinimum()){
            //if slider value minimum, slider label resets to default
        }else{
            setSelectedFilterLabel();
        }
        pnlVizualizer.repaint();
    }

    //list filtration
    private void selectListSPC(ActionEvent evt){
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
        try {
            UIManager.setLookAndFeel( new FlatLightLaf());

        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        }
        //</editor-fold>
        System.setProperty("sun.java2d.opengl","True");
        //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        /* Create and display the form */
        EventQueue.invokeLater(() -> new UserView().setVisible(true));

    }

    public static void setPlantHeightSliderValues(float min,float max){
        sldPlantHeight.setMinimum(Math.round(min-1));
        sldPlantHeight.setValue(Math.round(max+1));
        sldPlantHeight.setMaximum(Math.round(max+1));
        sldPlantHeightMin.setMinimum(Math.round(min-1));
        sldPlantHeightMin.setValue(Math.round(min-1));
        sldPlantHeightMin.setMaximum(Math.round(max+1));
    }

    public static void setCanopyRadiusSliderValues(float minCanopyRadius, float maxCanopyRadius) {
        sldMaxCanopyRadius.setMinimum(Math.round(minCanopyRadius-1));
        sldMaxCanopyRadius.setValue(Math.round(maxCanopyRadius+1));
        sldMaxCanopyRadius.setMaximum(Math.round(maxCanopyRadius+1));
        sldMinCanopyRadius.setMinimum(Math.round(minCanopyRadius-1));
        sldMinCanopyRadius.setValue(Math.round(minCanopyRadius-1));
        sldMinCanopyRadius.setMaximum(Math.round(maxCanopyRadius+1));
    }

    public static int getPlantHeightMin(){
        if (sldPlantHeight !=null) {
            return sldPlantHeight.getMinimum();
        }
        else{
            return -1;
        }
    }

    public static int getCanopyRadiusMin() {
        if (sldMinCanopyRadius!=null){
            return sldMinCanopyRadius.getMinimum();
        }
        else{
            return -1;
        }
    }

    public static int getPlantHeightMax() {
        if (sldPlantHeight !=null) {
            return sldPlantHeight.getMaximum();
        }
        else{
            return 10000;
        }
    }

    public static int getCanopyRadiusMax() {
        if (sldMaxCanopyRadius!=null){
            return sldMinCanopyRadius.getMaximum();
        }
        else{
            return 10000;
        }
    }

    /**
     * Gets Jlabel with embedded image to display on visualizer panel
     * @param
     * @throws IOException
     */
    public static void setVisualizerScreen(JLabel vizLabel) throws IOException {
        vizLabel.setBounds(0, 0, pnlVizualizer.getWidth(), pnlVizualizer.getHeight());
        pnlVizualizer.add(vizLabel);
        pnlVizualizer.revalidate();
        pnlVizualizer.repaint();
    }
    /**
     * Accessor method for Visualizer Panel
     * @return
     */
    public static JPanel getPnlVizualizer(){
        return pnlVizualizer;
    }
    /**
     * Accessor method for Species JList Filter pane
     * @return
     */
    public static JList getlistFilterSpecies(){
        return listFilterSpecies;
    }
    /**
     * Accessor method for Genus JList Filter pane
     * @return
     */
    public static JList getlistFilterGenus(){
        return listFilterPlants;
    }

}

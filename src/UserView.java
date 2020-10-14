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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static java.lang.Thread.currentThread;

/**
 *
 * @author yashkir
 */
public class UserView extends JFrame{

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDefaultThreshold;
    private javax.swing.JButton btnZoomIn;
    private javax.swing.JButton btnZoomOut;
    private javax.swing.JComboBox<String> cbxSimulationType;
    private javax.swing.JCheckBox chbCanopy;
    private javax.swing.JComboBox<String> chbControlsList;
    private javax.swing.JCheckBox chbUndergrowth;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JLabel lblControlPanel;
    private javax.swing.JLabel lblHelp;
    private javax.swing.JLabel lblPlantHeightMinValue;
    private javax.swing.JLabel lblPlantHeightSlider2;
    private javax.swing.JLabel lblPlantHeightSlider3;
    private javax.swing.JLabel lblPlantHeightValue;
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
    private javax.swing.JPanel pnlControls;
    private javax.swing.JPanel pnlFIlters;
    private javax.swing.JPanel pnlHelp;
    private javax.swing.JScrollPane pnlSelectedVis;
    private javax.swing.JPanel pnlSimControls;
    private javax.swing.JPanel pnlSimulation;
    private javax.swing.JPanel pnlViewSettings;
    private javax.swing.JPanel pnlVisibility;
    private javax.swing.JSlider sldPlantHeightMin;
    private javax.swing.JSpinner sldRenderingThreshold;
    private javax.swing.JScrollPane tabFilterPlants;
    private javax.swing.JScrollPane tabFilterSpecies;
    private javax.swing.JTabbedPane tabbedFilterPane;
    // End of variables declaration//GEN-END:variables

    //Protected Static components
    protected static VizPanel pnlVizualizer;
    protected static javax.swing.JSlider sldPlantHeight;
    protected static javax.swing.JList<String> listFilterPlants;
    protected static javax.swing.JList<String> listFilterSpecies;

    //Self-declared Variables
    public static Controller localController;
    private boolean selectedSimType = false;
    private static int windX;
    private static int windY;

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
        getContentPane().setBackground(new Color(56,60,74));
        localController = new Controller(this);
        localController.setVisualizerCursor(Cursor.DEFAULT_CURSOR);
    }

    private void setTheme(Theme theme) {
        ArrayList<Component> components = getAllComponents(this);
        if(theme == Theme.DARK_MODE){
            for (Component component:
                    components) {
                component.setBackground(new Color(56,60,74));
                component.setForeground(new Color(255,255,255));
            }
        }else{

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

        /* Spinners */
        sldRenderingThreshold.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent evt) {
                sldRenderingThresholdStateChanged(evt);
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

        /* Menu Items */
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
        });

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

        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlVizualizer = new VizPanel();
        jPanel1 = new javax.swing.JPanel();
        btnZoomOut = new javax.swing.JButton();
        lblZoom = new javax.swing.JLabel();
        btnZoomIn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        chbControlsList = new javax.swing.JComboBox<String>();
        lblControlPanel = new javax.swing.JLabel();
        pnlControls = new javax.swing.JPanel();
        pnlHelp = new javax.swing.JPanel();
        lblHelp = new javax.swing.JLabel();
        pnlFIlters = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        sldPlantHeightMin = new javax.swing.JSlider();
        lblPlantHeightMinValue = new javax.swing.JLabel();
        lblPlantHeightSlider2 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        sldPlantHeight = new javax.swing.JSlider();
        lblPlantHeightValue = new javax.swing.JLabel();
        lblPlantHeightSlider3 = new javax.swing.JLabel();
        pnlViewSettings = new javax.swing.JPanel();
        lblViewSettings = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        btnDefaultThreshold = new javax.swing.JButton();
        lblThreshold = new javax.swing.JLabel();
        sldRenderingThreshold = new javax.swing.JSpinner();
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
        menuBar = new javax.swing.JMenuBar();
        mbFIleOption = new javax.swing.JMenu();
        miLoadFIles = new javax.swing.JMenuItem();
        miRestart = new javax.swing.JMenuItem();
        miExit = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EcoViz");
        setPreferredSize(new java.awt.Dimension(1024, 800));

        pnlVizualizer.setName("");

        btnZoomOut.setText("-");

        lblZoom.setFont(new java.awt.Font("Ubuntu", 0, 24)); // NOI18N
        lblZoom.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblZoom.setText("Zoom");

        btnZoomIn.setText("+");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblZoom)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(btnZoomOut, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnZoomIn, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(btnZoomIn)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnZoomOut)
                                        .addComponent(lblZoom))
                                .addContainerGap())
        );

        javax.swing.GroupLayout pnlVizualizerLayout = new javax.swing.GroupLayout(pnlVizualizer);
        pnlVizualizer.setLayout(pnlVizualizerLayout);
        pnlVizualizerLayout.setHorizontalGroup(
                pnlVizualizerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVizualizerLayout.createSequentialGroup()
                                .addGap(0, 581, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        pnlVizualizerLayout.setVerticalGroup(
                pnlVizualizerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlVizualizerLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
        );

        jPanel2.setBorder(null);

        chbControlsList.setModel(new javax.swing.DefaultComboBoxModel<String>(new String[] { "Filters", "Visibility", "Simulation", "Rendering", " " }));

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

        pnlControls.setBorder(new javax.swing.border.MatteBorder(null));
        pnlControls.setForeground(new java.awt.Color(56, 60, 74));
        pnlControls.setLayout(new java.awt.CardLayout());

        pnlHelp.setBorder(null);

        lblHelp.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblHelp.setText("Help Guide");

        javax.swing.GroupLayout pnlHelpLayout = new javax.swing.GroupLayout(pnlHelp);
        pnlHelp.setLayout(pnlHelpLayout);
        pnlHelpLayout.setHorizontalGroup(
                pnlHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHelpLayout.createSequentialGroup()
                                .addGap(90, 90, 90)
                                .addComponent(lblHelp)
                                .addContainerGap(91, Short.MAX_VALUE))
        );
        pnlHelpLayout.setVerticalGroup(
                pnlHelpLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlHelpLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblHelp)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlControls.add(pnlHelp, "pnlHelp");

        pnlFIlters.setBorder(null);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 228, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
                jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 124, Short.MAX_VALUE)
        );

        sldPlantHeightMin.setOrientation(javax.swing.JSlider.VERTICAL);
        sldPlantHeightMin.setPaintLabels(true);
        sldPlantHeightMin.setPaintTicks(true);
        sldPlantHeightMin.setValue(0);
        sldPlantHeightMin.setBorder(null);

        lblPlantHeightMinValue.setText("0");

        lblPlantHeightSlider2.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblPlantHeightSlider2.setText("<html><center>Min Plant<br> Height");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblPlantHeightSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addComponent(lblPlantHeightMinValue))))
                                        .addGroup(jPanel5Layout.createSequentialGroup()
                                                .addGap(32, 32, 32)
                                                .addComponent(sldPlantHeightMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
                jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel5Layout.createSequentialGroup()
                                .addContainerGap(30, Short.MAX_VALUE)
                                .addComponent(lblPlantHeightSlider2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPlantHeightMinValue)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sldPlantHeightMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        sldPlantHeight.setOrientation(javax.swing.JSlider.VERTICAL);
        sldPlantHeight.setPaintLabels(true);
        sldPlantHeight.setPaintTicks(true);
        sldPlantHeight.setValue(0);
        sldPlantHeight.setBorder(null);

        lblPlantHeightValue.setText("0");

        lblPlantHeightSlider3.setFont(new java.awt.Font("Ubuntu Light", 0, 15)); // NOI18N
        lblPlantHeightSlider3.setText("<html><center>Max Plant<br> Height");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(lblPlantHeightSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                                .addGap(29, 29, 29)
                                                                .addComponent(lblPlantHeightValue))))
                                        .addGroup(jPanel6Layout.createSequentialGroup()
                                                .addGap(32, 32, 32)
                                                .addComponent(sldPlantHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
                jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                                .addContainerGap(30, Short.MAX_VALUE)
                                .addComponent(lblPlantHeightSlider3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblPlantHeightValue)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(sldPlantHeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlFIltersLayout = new javax.swing.GroupLayout(pnlFIlters);
        pnlFIlters.setLayout(pnlFIltersLayout);
        pnlFIltersLayout.setHorizontalGroup(
                pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlFIltersLayout.createSequentialGroup()
                                .addContainerGap(38, Short.MAX_VALUE)
                                .addGroup(pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(32, 32, 32))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlFIltersLayout.createSequentialGroup()
                                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addContainerGap())))
        );
        pnlFIltersLayout.setVerticalGroup(
                pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlFIltersLayout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(91, 91, 91)
                                .addGroup(pnlFIltersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(57, Short.MAX_VALUE))
        );

        pnlControls.add(pnlFIlters, "pnlFilters");

        lblViewSettings.setFont(new java.awt.Font("Ubuntu Light", 0, 24)); // NOI18N
        lblViewSettings.setText("Rendering");

        btnDefaultThreshold.setToolTipText("");

        lblThreshold.setText("<html><center>Rendering<br>Threshold</center>");

        sldRenderingThreshold.setModel(new javax.swing.SpinnerNumberModel(0, 0, 100, 0));
        sldRenderingThreshold.setName(""); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel3Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(lblThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(sldRenderingThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDefaultThreshold, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
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

        javax.swing.GroupLayout pnlViewSettingsLayout = new javax.swing.GroupLayout(pnlViewSettings);
        pnlViewSettings.setLayout(pnlViewSettingsLayout);
        pnlViewSettingsLayout.setHorizontalGroup(
                pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addComponent(lblViewSettings)
                                .addContainerGap(91, Short.MAX_VALUE))
                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlViewSettingsLayout.setVerticalGroup(
                pnlViewSettingsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlViewSettingsLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addComponent(lblViewSettings)
                                .addGap(30, 30, 30)
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(503, Short.MAX_VALUE))
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
                                .addContainerGap(85, Short.MAX_VALUE))
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
                                .addContainerGap(337, Short.MAX_VALUE))
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
                                                .addGap(30, 30, 30)
                                                .addComponent(chbUndergrowth)
                                                .addGap(18, 18, 18)
                                                .addComponent(chbCanopy)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(pnlVisibilityLayout.createSequentialGroup()
                                                .addGap(111, 111, 111)
                                                .addComponent(jLabel2)
                                                .addGap(0, 88, Short.MAX_VALUE)))
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
                                .addComponent(pnlSelectedVis, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE)
                                .addContainerGap())
        );

        pnlControls.add(pnlVisibility, "pnlVisibility");

        menuBar.setName(""); // NOI18N

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
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(pnlControls, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        // </editor-fold>//GEN-END:initComponents

    private void sldRenderingThresholdStateChanged(ChangeEvent evt) {
        JSpinner source = (JSpinner) evt.getSource(); //gets the event type
        int value = (Integer) source.getValue();
        pnlVizualizer.setViewingThreshold(value);
    }

    private void zoomOutButtonClick() {
        pnlVizualizer.zoomOutTenPercent();
    }

    private void zoomInButtonClick() {
        pnlVizualizer.zoomInTenPercent();
    }


    /**JList methods for filtering through species.
     * Access Fileloader static variables
     * @param evt
     */
    private void jListPlantSelect(ListSelectionEvent evt) {
        //set text on right here
        String s = (String) listFilterPlants.getSelectedValue();
        if(s==null){return;}
        String[][] spc = FileLoader.getSpcKey();
        for(int i=0;i<spc.length;i++){
            if(s.equals(spc[i][1].split(" ")[0])&&!FileLoader.getSpcDraw()[i]){
                System.out.println(s);
                System.out.println(spc[i][1].split(" ")[0]);
                FileLoader.setSpcDraw(i,true);
                break;
            }
            else if(s.equals(spc[i][1].split(" ")[0])&&FileLoader.getSpcDraw()[i]){
                System.out.println(s);
                System.out.println(spc[i][1].split(" ")[0]);
                FileLoader.setSpcDraw(i,false);
                break;
            }
        }

        //UserView.pnlVizualizer.repaint();
        localController.updateView();
    }
    private void jListSpeciesSelect(ListSelectionEvent evt) {
        //set text on right here
        String s = (String) listFilterSpecies.getSelectedValue();
        if(s==null){return;}
        String[][] spc = FileLoader.getSpcKey();
        for(int i=0;i<spc.length;i++){
            if(s.equals(spc[i][0])&&!FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,true);
            }
            else if(s.equals(spc[i][0])&&FileLoader.getSpcDraw()[i]){
                FileLoader.setSpcDraw(i,false);
            }
        }
        //UserView.pnlVizualizer.repaint();
        localController.updateView();
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
                            //JButton resetButton = new JButton("Reset");
                            JButton stopButton = new JButton("Stop");

                            pauseButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                }
                            });

                            playButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

                                }
                            });

                           /* resetButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    pnlVizualizer.paused = false;
                                    pnlVizualizer.done = true;
                                }
                            });
                           */
                            stopButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {

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
                        windX = dialog.getwindX();
                        windY = dialog.getwindY();

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
        pnlVizualizer.startFireClicked=true;
    }

    public static int getWindX(){
        return windX;
    }

    public static int getWindY(){
        return windY;
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
        //sliders only work in integers
        if(source.getValue()<=sldPlantHeight.getMinimum()){
             //if slider value minimum, slider label resets to default
        }else{
            lblPlantHeightValue.setText(Double.toString(source.getValue()));
        }
        pnlVizualizer.repaint();
    }
    //list filtration
    private void selectListSPC(ActionEvent evt){
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel( new FlatLightLaf());

            /*for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }*/
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (InstantiationException ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(UserView.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        //</editor-fold>
        Thread currentThread = currentThread();
        System.out.println(currentThread());

        /* Create and display the form */
        EventQueue.invokeLater(() -> new UserView().setVisible(true));
    }

    public static void setPlantHeightSliderValues(float min,float max){
        sldPlantHeight.setMinimum(Math.round(min-1));
        sldPlantHeight.setValue(Math.round(min-1));
        sldPlantHeight.setMaximum(Math.round(max+1));
    }
    public static int getPlantHeightMin(){
        if (sldPlantHeight !=null) {
            return sldPlantHeight.getMinimum();
        }
        else{
            return -1;
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

package com.team4.ecoviz;

/**
 * Class for functionality of grid - Not used for prototype as yet.
 */
public class Grid {
    // Grid attributes
    private static int dimx;
    private static int dimy;
    private static double spacing;
    private static double latitude;
    private static float[][] terrain;

    /** default Constructor
     *
     */
    public Grid(){
        this.dimx = 0;
        this.dimy = 0;
        this.spacing = 0;
        this.latitude = 0;
    }

    /** Create grid object
     *
     * @param x
     * @param y
     * @param spacing
     * @param latitude
     * @param terrain
     */
    public Grid(int x, int y, float spacing, float latitude, float[][] terrain){
        this.dimx = x;
        this.dimy = y;
        this.spacing = spacing;
        this.latitude = latitude;
        this.terrain = terrain;
    }
}

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
    private static float[][] gradient;
    private static Block[][] grid;
    private static BufferedImage greyscale;

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
        greyscale = new BufferedImage(dimy,dimx,BufferedImage.TYPE_INT_ARGB);
    }


   // private int toRGB(float val){
    //    int part = Math.round(val*255);
    //    return part*0x10101;
    //}
    public static void setBlockDIm(int x, int y){
        grid = new Block[x][y];
    }
    public static Block getBlock(int x, int y){ return grid[x][y];}
    public static Block[][] getGrid(){ return grid;}

    //public static void fillBlock()

    public BufferedImage getGreyscale(int renderType) throws IOException {
        if (renderType==1){
            buildGreyscale();
        }
        if(renderType ==2){
            buildGreyscale2();
        }
        File out = new File("img.png");
        ImageIO.write(greyscale,"png",out);
        return greyscale;
    }

    public BufferedImage buildGreyscale() {
        float minElv =  100000;
        float maxElv = -100000;

        for (int yComp = 0; yComp<dimy ; yComp++){
             for (int xComp = 0 ; xComp<dimx ; xComp++){
                 grid[xComp][yComp] = new Block(terrain[yComp][xComp], xComp, yComp);
                 if (terrain[yComp][xComp]>maxElv){
                    maxElv = terrain[yComp][xComp];
                 }
                if (terrain[yComp][xComp]<minElv){
                    minElv = terrain[yComp][xComp];
                }
             }
        }

        for (int yComp = 0; yComp < dimy; yComp++) {
            for (int xComp = 0; xComp < dimx; xComp++) {
                float norm =((terrain[yComp][xComp] - minElv)/(maxElv-minElv));
                Color colour = new Color(norm, norm, norm, 1.0f);
                greyscale.setRGB(xComp, yComp, colour.getRGB());
            }
        }
        return greyscale;
    }
    public BufferedImage buildGreyscale2() {
        float maxSlope =  0;
        //float maxElv = -100000;
        gradient = new float[dimy][dimx];
        float dist  = (float)Math.sqrt(2*spacing);

        for (int y = 0; y<dimy-1 ; y++){
            for (int x = 0 ; x<dimx-1 ; x++){
                gradient[y][x]= Math.abs((terrain[y+1][x]+terrain[y][x+1]-2*terrain[y][x])/dist);
                if (gradient[y][x]>maxSlope){
                    maxSlope = gradient[y][x];
                }
            }
            for(int x = 0; x<dimx; x++){
                gradient[dimy-1][x]=gradient[dimy-2][x];
                gradient[x][dimy-1]=gradient[x][dimy-2];
            }
        }

        for (int y = 0; y < dimy; y++) {
            for (int x = 0; x < dimx; x++) {
                float norm = (1-(float)Math.sqrt(gradient[y][x]/(maxSlope)))*(float)0.95;//m
                Color colour = new Color(norm, norm, norm, 1.0f);
                greyscale.setRGB(x, y, colour.getRGB());
            }
        }
        return greyscale;
    }



    public int getDimx() {
        return dimx;
    }

    public void setDimx(int dimx) {
        this.dimx = dimx;
    }

    public int getDimy() {
        return dimy;
    }

    public void setDimy(int dimy) {
        this.dimy = dimy;
    }

    public double getSpacing(){
        return spacing;
    }
}

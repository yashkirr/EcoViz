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

    public BufferedImage getGreyscale() throws IOException {
        buildGreyscale();
        File out = new File("img.png");
        ImageIO.write(greyscale,"png",out);
        return greyscale;
    }

    public BufferedImage buildGreyscale() {
        float minElv =  100000;
        float maxElv = -100000;

        for (int yComp = 0; yComp<dimy ; yComp++){
             for (int xComp = 0 ; xComp<dimx ; xComp++){
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
                float norm = (terrain[yComp][xComp] - minElv)/(maxElv-minElv);
                Color colour = new Color(norm, norm, norm, 1.0f);
                greyscale.setRGB(xComp, yComp, colour.getRGB());
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
}

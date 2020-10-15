import java.awt.*;

public class Burn extends Thread{
    private int x;
    private int y;

    Burn(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void run(){
            Fire.fireLayer.setRGB(x,y, Color.red.getRGB());
            Fire.gBurn.drawImage(Fire.fireLayer, 0, 0, null);
            if(x>0) {
                Burn burn = new Burn(x - 1, y);
                burn.start();
            }
            if(y>0){
                Burn burn = new Burn(x, y - 1);
                burn.start();
            }
            if(x<UserView.pnlVizualizer.getWidth()){
                Burn burn = new Burn(x + 1, y);
                burn.start();
            }
            if(y<UserView.pnlVizualizer.getHeight()){
                Burn burn = new Burn(x, y + 1);
                burn.start();
            }

    }
}

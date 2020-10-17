import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Vector;

public class FileSaver {

    public static void savePDB(ArrayList<ArrayList<Plant>> canopy, ArrayList<ArrayList<Plant>> undergrowth){
        String sizeX = Integer.toString(BlockGrid.getDimX());
        String sizeY = Integer.toString(BlockGrid.getDimY());
        String filename = sizeX + "x" + sizeY + "_EcoViz_Changes";
        try{
            PrintWriter save = new PrintWriter(filename+"_Canopy.pdb");
            save.println(FileLoader.getNumCanopy());
            int i = 0;
            for(ArrayList<Plant> list: canopy){
                int ID = list.get(0).getID();
                String id = Integer.toString(ID);
                String min = Float.toString(FileLoader.getMinCanopyHeight(i));
                String max = Float.toString(FileLoader.getMaxCanopyHeight(i));
                String avgHR = Integer.toString(0);
                save.println(id + " " + min + " " + max + " " + avgHR);
                save.println(Integer.toString(list.size()));
                i++;
                for(Plant plant: list){
                    Vector<Float> pos = plant.getPos();
                    String x = Float.toString(pos.get(0));
                    String y = Float.toString(pos.get(1));
                    String z = Float.toString(pos.get(2));
                    String h = Float.toString(plant.getHeight());
                    String r = Float.toString(plant.getCanopyRadius());
                    save.println(x + " " + y + " " + z + " " + h + " " + r);
                }


            }
            save.close();
        }
        catch (FileNotFoundException e) {}
        try{
            PrintWriter save = new PrintWriter(filename+"_Undergrowth.pdb");
            save.println(FileLoader.getNumUndergrowth());
            int i = 0;
            for(ArrayList<Plant> list: undergrowth){
                int ID = list.get(0).getID();
                String id = Integer.toString(ID);
                String min = Float.toString(FileLoader.getMinUndergrowthHeight(i));
                String max = Float.toString(FileLoader.getMaxUndergrowthHeight(i));
                String avgHR = Integer.toString(0);
                save.println(id + " " + min + " " + max + " " + avgHR);
                save.println(Integer.toString(list.size()));
                i++;
                for(Plant plant: list){
                    Vector<Float> pos = plant.getPos();
                    String x = Float.toString(pos.get(0));
                    String y = Float.toString(pos.get(1));
                    String z = Float.toString(pos.get(2));
                    String h = Float.toString(plant.getHeight());
                    String r = Float.toString(plant.getCanopyRadius());
                    save.println(x + " " + y + " " + z + " " + h + " " + r);
                }


            }
            save.close();
        }
        catch (FileNotFoundException e) {}
    }
}

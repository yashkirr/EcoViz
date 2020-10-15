import javax.swing.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * SwingWorker class used to stop blocking on EDT when loading files
 * @author Yashkir Ramsamy
 */
public class FileLoadingWorker extends SwingWorker<Void,Void> {

    private ArrayList<String> filePathList;

   public FileLoadingWorker(ArrayList<String> filePathList){
        this.filePathList = filePathList;

    }

    @Override
    protected Void doInBackground() throws Exception {
        long startTime = System.nanoTime();
        Controller.loadFile(filePathList.get(0), filePathList.get(1),filePathList.get(2),filePathList.get(3));
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        System.out.println("FileLoadingWorker: "+ duration/1000000);
        return null;
    }

    @Override
    protected void done() {
        super.done();
    }

}

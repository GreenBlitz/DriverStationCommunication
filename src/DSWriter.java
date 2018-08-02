import org.json.JSONException;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by ofeke on 8/1/2018.
 */
public class DSWriter extends Thread {
    private String name;
    private Thread thread;
    private String mes;
    private PrintStream ps;

    public DSWriter(String name, PrintStream ps){
        this.name = name;
        this.ps = ps;
    }

    @Override
    public void run(){
        try {
            DSCommunicator com = DSCommunicator.init();
            while (com.isWorking()) {
                try {
                  //  System.out.println(com.getMessage());
                    ps.println(com.getMessage());
                }catch (JSONException x){
                    x.printStackTrace();
                }
                try {
                    Thread.sleep(500);
                }catch (InterruptedException x) {
                    x.printStackTrace();
                }
            }
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    @Override
    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }
}

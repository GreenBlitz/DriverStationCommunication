import java.io.PrintStream;

/**
 * Created by ofeke on 8/1/2018.
 */
public class DsServerWriter extends Thread {
    private String name;
    private Thread thread;
    private PrintStream ps;
    private String mes;

    public DsServerWriter(String message, PrintStream ps){
        this.name = "WritingThread";
        this.ps = ps;
        this.mes = message;
    }

    @Override
    public void run(){
        ps.println(mes);
    }

    @Override
    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }



}

import com.google.gson.Gson;
import com.sun.security.ntlm.Server;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ofeke on 8/4/2018.
 */
public class ServerDS extends Thread {
    private String name;
    private Thread thread;
    private int id;
    private Events robotState;
    private boolean working;
    private String extra;
    private int battery;
    private ServerSocket serverSocket;
    private static ServerDS[] instance;
    private String realRobotState;

    private ServerDS(int id){
        this.id = id;
        name = "ServerDriverStation"+id;
        extra = "";
        working = true;
        battery = 0;
        robotState = Events.PreGame;
    }

    public static ServerDS init(int id, ServerSocket serverSocket){
        if(instance == null){
            instance = new ServerDS[4];
            instance[id] = new ServerDS(id);
            instance[id].serverSocket = serverSocket;
        }
        if(instance[id] == null){
            instance[id] = new ServerDS(id);
            instance[id].serverSocket = serverSocket;
        }
        return instance[id];
    }

    @Override
    public void run(){
        try {
            Socket socket = serverSocket.accept();
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());
            DataInputStream input = new DataInputStream(socket.getInputStream());
            PrintStream ps = new PrintStream(output);
            Scanner scanner = new Scanner(input);

            while(!scanner.hasNext()){}

            try {
                JSONObject j = new JSONObject(scanner.next());
                System.out.println();
                battery = j.getInt("Battery");
            }catch (JSONException x){
                x.printStackTrace();
            }

            while (working){

                ps.println(getMessage());
                try {
                    Thread.sleep(100);
                }catch (InterruptedException x){
                    x.printStackTrace();
                }
            }
        }catch (IOException x){
            x.printStackTrace();
        }

    }

    public String getMessage(){
        DataDS.Data d = new DataDS.Data(id, robotState, extra);
        return ;
    }

    public void start(){
        if(thread == null){
            thread =new Thread(this, name);
            thread.start();
        }
    }

    public void setExtra(String str){
        extra = str;
    }

    public int getBattery(){
        return battery;
    }

    public void setRobotState(Events s){
        robotState = s;
    }

    public Events getRobotState(){
        return robotState;
    }
    public int getID(){
        return id;
    }
}

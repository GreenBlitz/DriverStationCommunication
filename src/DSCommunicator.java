import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ofeke on 8/1/2018.
 */
public class DSCommunicator extends Thread{
    private String name;
    private Thread thread;
    private String exeptedRobotState;
    private String curRobotState;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private PrintStream ps;
    private Scanner scan;
    private boolean working;
    private static DSCommunicator instance;
    private String ip = "127.0.0.1";
    private int port = 2212;
    private int battery;
    private int state;
    private DSWriter dsw;
    private Integer id;

    private DSCommunicator()throws IOException{
        name = "DriverStationCommunication";
        exeptedRobotState = "Disable";
        working = true;
    }

    public static DSCommunicator init()throws IOException{
        if(instance == null){
            instance = new DSCommunicator();
        }
        return instance;
    }

    public static DSCommunicator init(int id)throws IOException{
        if(instance == null){
            instance = new DSCommunicator();
            instance.id = id;
        }
        return instance;
    }

    public void setID(int id){
        if(this.id == null)
            this.id = id;
    }

    public int getID(){
        return id;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            scan = new Scanner(input);
            ps = new PrintStream(output);

            dsw = new DSWriter("DSWriter", ps);
            dsw.start();

            JSONObject jo;
            try {
                while (working) {
                    while (!scan.hasNext()) {}
                    String s = scan.next();
                  //  System.out.println("Server to Ds message: "+s);
                    if(s != "null"){
                        jo = new JSONObject(s);
                       // System.out.println(jo.getString("Event"));
                        exeptedRobotState = jo.getString("Event");
                        if(exeptedRobotState != curRobotState){
                            switch (exeptedRobotState){
                                case "PreGame":
                                    break;
                                case "PostGame":
                                    break;
                                case "FinishGame":
                                    break;
                                case "Tele":
                                    break;
                                case "Auto":
                                    break;
                                case "Enable":
                                    break;
                                case "Disable":
                                    break;
                                    //TODO need to write to the ev3 what to do!
                            }
                        }
                    }
                }
            }catch (JSONException x){
                x.printStackTrace();
            }
            ps.close();
            scan.close();
            output.close();
            input.close();
            socket.close();
        }catch (IOException x){
            x.printStackTrace();
        }
    }

    @Override
    public void finalize(){
        working = false;
    }

    @Override
    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }

    public boolean isWorking(){
        return working;
    }

    public String getMessage() throws JSONException{
        JSONObject j = new JSONObject();
        j.put("RobotState", getRobotState());
        j.put("Id", id);
        j.put("Battery", getBattery());
        j.put("Extra", getExtras());
        return j.toString();
    }


    private String getRobotState(){
        return exeptedRobotState;
    }

    public String getExtras(){
        return "null";
    }

    private int getBattery(){
        //Todo:BOM
        return 0;
    }

    public void setRobotState(String robotState){
        this.curRobotState = robotState;
    }

}

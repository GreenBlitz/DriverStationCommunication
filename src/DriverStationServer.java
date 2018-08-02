import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ofeke on 8/1/2018.
 */
public class DriverStationServer extends Thread {
    private String name;
    private Thread thread;
    private static DriverStationServer[] instances;
    private ServerSocket serSocket;
    private Socket socket;
    private DataInputStream input;
    private String exeptedRobotState;
    private DataOutputStream output;
    private PrintStream ps;
    private JSONObject jo;
    private String robotState;
    private Integer battery;
    private Integer id;
    private Scanner scan;
    private boolean haveBeenAcepted;
    DsServerWriter writer;
    private boolean working;

    private DriverStationServer(Integer id, ServerSocket serverSocket){
        this.name = "DriverStationServer"+id;
        this.id = id;
        this.serSocket = serverSocket;
        working = true;
        haveBeenAcepted = false;
    }

    /**
     * there are only 4 different driver stations so there only 4 existing instances of driver station server
     * @param id the current id of the driver station
     * @param serverSocket the global server socket of the driver stations, normally use port 2212
     * @return the requested instance
     */
    public static DriverStationServer init(Integer id, ServerSocket serverSocket){
        if(id>3 || id < 0){
            return null;
        }
        if(instances == null){
            instances = new DriverStationServer[4];
            instances[id] = new DriverStationServer(id, serverSocket);
        }
        if(instances[id] == null){
            instances[id] = new DriverStationServer(id, serverSocket);
        }
        return instances[id];
    }

    @Override
    public void run(){
        try{
            socket = serSocket.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            scan = new Scanner(input);
            ps = new PrintStream(output);

            haveBeenAcepted = true;

            try{
                while(working){
                    while(!scan.hasNext()){}

                    jo = new JSONObject(scan.next());
            //        System.out.println(jo.toString());
                    if(id != jo.getInt("Id")){
                        DriverStationServer  helper = instances[id];
                        instances[id] = instances[jo.getInt("Id")];
                        id = jo.getInt("Id");
                        instances[id] = helper;
            //            System.out.println("Driver station id have been updated!");
                    }

          //          System.out.println(jo.toString());

                    robotState = jo.getString("RobotState");


                    if(exeptedRobotState== null){
                        exeptedRobotState = "Disable";
                    }
                    if(robotState != exeptedRobotState){
                        writeMessage(exeptedRobotState);
                    }

                    battery = jo.getInt("Battery");
                    if(battery < 15){
                        //TODO print on server cmd
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

    public JSONObject getJson(){
        return new JSONObject(jo);
    }

    public String getRobotState() throws  JSONException{
        return robotState;
    }

    public boolean hasBeenAcepted(){
        return haveBeenAcepted;
    }

    public void writeMessage(String str){
        writer = new DsServerWriter("{\"Event\":"+str+"}", ps);
        writer.start();
    }

    public void setRobotState(String state){
        exeptedRobotState = state;
    }

    public Integer getBattery(){
        if(battery == null){
            return null;
        }
        return  battery;
    }

    private String getJson(String mes){
        return "{\"Event\":"+mes+"}";
    }

    @Override
    public void finalize(){
        turnOff();
    }

    public void turnOff(){
        working = false;
    }

    @Override
    public void start(){
        if(thread == null){
            thread = new Thread(this, name);
            thread.start();
        }
    }

}

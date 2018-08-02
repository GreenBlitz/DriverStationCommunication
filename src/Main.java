import java.net.ServerSocket;

/**
 * Created by ofeke on 8/1/2018.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(2212);

        DriverStationServer dss = DriverStationServer.init(0, serverSocket);
        dss.start();

        DSCommunicator com = DSCommunicator.init(0);
        com.start();

        while(!dss.hasBeenAcepted()){}

        Thread.sleep(100);

        print(dss.getBattery());

        print(com.getID());
    }

    public static <T> void print(T t ){
        System.out.println(t.toString());
    }
}

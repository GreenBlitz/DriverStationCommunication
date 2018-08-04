import java.net.ServerSocket;

/**
 * Created by ofeke on 8/1/2018.
 */
public class Main {
    public static void main(String[] args) throws Exception{
        ServerSocket serverSocket = new ServerSocket(2212);
        ServerDS serverDS = ServerDS.init(0, serverSocket);
        serverDS.start();
        Thread.sleep(5000);
        serverDS.kill();
    }

    public static <T> void print(T t ){
        System.out.println(t.toString());
    }
}

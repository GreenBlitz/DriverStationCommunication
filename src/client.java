import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by ofeke on 8/4/2018.
 */
public class client {
    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("127.0.0.1",2212);
        DataOutputStream output = new DataOutputStream(socket.getOutputStream());
        DataInputStream input = new DataInputStream(socket.getInputStream());
        PrintStream ps = new PrintStream(output);
        Scanner scanner = new Scanner(input);

        ps.println("{\"Battery\":50}");

        while (true){
            while (!scanner.hasNext()){}

            System.out.println(scanner.next());
        }

    }

}

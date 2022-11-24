import javax.swing.*;
import java.net.*;
import java.io.*;


public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 9090;


    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket(SERVER_IP, PORT);

        //ServerConnection serverConn = new ServerConnection(socket);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //(new Thread(serverConn)).start();

        //Get input from the User
        System.out.println("Enter your name in format: NODE name");

        while (true) {
            System.out.print("[CLIENT]: ");
            String command = keyboard.readLine();             //czytanie danych z klawiatury

            if (command.equals("quit")) break;

            out.println(command);

            String serverResponse = input.readLine();
            if (serverResponse != "") System.out.println("[SERVER]: " + serverResponse);
        }

        //socket.close();
        //System.exit(0);
    }
}
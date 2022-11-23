import javax.swing.*;
import java.net.*;
import java.io.*;


public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 5017;


    public static void main(String[] args) throws IOException
    {
        Socket socket = new Socket(SERVER_IP, PORT);

        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));

        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //Get input from the User
        System.out.println("Enter your name in format: NODE name");
        //keyboard.readLine();

        while (true) {
            System.out.print("[CLIENT]: ");
            String command = keyboard.readLine();             //czytanie wpisywanych danych z klawiatury

            if (command.equals("quit")) break;

            out.println(command);

            String serverResponse = input.readLine();
            System.out.println("[SERVER]: " + serverResponse);
        }

    }
}
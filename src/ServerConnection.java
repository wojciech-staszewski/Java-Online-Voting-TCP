import java.net.*;
import java.io.*;


public class ServerConnection implements Runnable{
    private Socket server;
    private BufferedReader in;


    public ServerConnection(Socket s) throws IOException
    {
        server = s;
        in = new BufferedReader(new InputStreamReader(server.getInputStream()));
    }

    @Override
    public void run()
    {
        try {
            while (true)
            {
                String serverResponse = in.readLine();
                if (serverResponse == null) break;
                System.out.println("[SERVER]: " + serverResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;

    //KONSTRUKTOR
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clients) throws IOException
    {
        this.client = clientSocket;
        this.clients = clients;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
    }

    @Override
    public void run() {
        boolean condition = true;
        try {
            while (condition) {
                String request = in.readLine();
                String votingName;
                String nodeName;
                String vote;

                if (request.startsWith("NODE"))
                {
                    out.println(Server.sayHello(request.substring(5)));
                }
                else if (request.startsWith("NEW"))
                {
                    String[] s = request.split(" ");
                    nodeName = this.getClass().getName();
                    votingName = s[1];
                    vote = s[2];
                    out.println(Server.newVoting(votingName, nodeName, vote));
                }
                else if (request.startsWith("VOTE"))
                {
                    String[] s = request.split(" ");
                    out.println(Server.addVote(s[1], s[2]));
                }
                else if (request.startsWith("get")) {
                    out.println(Server.getVotingsName());
                }
                else if (request.startsWith("PING")) {
                    out.println(Server.serverResponse("PONG"));
                }
                else if (request.contains("PONG")) {
                }
                else if (request.contains("quit")){
                    condition = false;
                } else {

                }


            }
        } catch(IOException e) {
            System.err.println("IOExcpetion in client handler");
            System.err.println(e.getStackTrace());
        } finally {
            try {
                out.close();
                in.close();
            } catch (IOException e)
            {

            }
        }

        System.exit(0);
    }
}

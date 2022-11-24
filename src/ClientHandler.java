import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private ArrayList<ClientHandler> clients;
    String nodeName = "";

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

        try {
            while (true) {
                String request = in.readLine();

                String votingName;
                String vote;

                if (request.startsWith("NODE"))
                {
                    String[] s = request.split(" ");
                    nodeName = s[1];
                    out.println(Server.sayHello(nodeName));
                }
                else if (request.startsWith("NEW"))
                {
                    String[] s = request.split(" ");
                    votingName = s[1];
                    vote = s[2];

                    if (Server.checkExsistingVoting(votingName) == true)
                    {
                        out.println("Voting of this name already exsists!");
                    } else {
                        out.println(Server.newVoting(votingName, nodeName, vote));
                    }
                }
                else if (request.startsWith("VOTE"))
                {
                    String[] s = request.split(" ");
                    votingName = s[1];
                    if (Server.checkExsistingVoting(votingName))
                    {
                        out.println(Server.addVote(s[1], s[2]));
                    } else {
                        out.println("There is no voting of this name!");
                    }
                }
                else if (request.startsWith("get"))
                {
                    out.println(Server.getVotingsName());
                }
                else if (request.startsWith("PING"))
                {
                    out.println(Server.serverResponse("PONG"));
                }
                else if (request.startsWith("PONG"))
                {
                    out.println(Server.serverResponse(""));
                }
                else if (request.equals("quit"))
                {
                    break;
                } else {
                    out.println("Command is not correct!");
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

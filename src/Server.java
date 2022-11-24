import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static final String SERVER_IP = "localhost";
    private static final int PORT = 9090;
    private static final int numberOfClients = 3;

    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(numberOfClients);
    private static String[] votings  = new String[0];
    private static String[] nodes = new String[numberOfClients];
    private static int id = 0;


    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(PORT);
        //Socket socket = new Socket(SERVER_IP, PORT);

        //BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            System.out.println("[SERVER]: Waiting for client connection...");
            System.out.print("[SERVER]: ");

            //String command = keyboard.readLine();
            //out.println(command);

            Socket client = ss.accept();
            System.out.println("Client connected!");
            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
    }

    public static String sayHello(String name) throws IOException
    {
        boolean exists = true;

        //CHECKING FOR DUPLICATES IN nodes[]
        for (int i = 0; i < nodes.length; i++) {
            if (name.equals(nodes[i])) {
                exists = true;
                break;
            } else {
                exists = false;
            }
        }

        //CREATE node
        if (exists == false) {
            nodes[id] = name;
            id++;
            return "Hello " + name;
        }

        return "Node of this name already exists!";
    }

    public static String serverResponse(String answer)
    {
        return answer;
    }

    public static String newVoting(String votingName, String nodeName, String vote) throws IOException
    {
        votings = Arrays.copyOf(votings, votings.length + 1);
        if (vote.equals("Y")) votings[votings.length - 1] = votingName + " Y:1 N:0";
        if (vote.equals("N")) votings[votings.length - 1] = votingName + " Y:0 N:1";


        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Type content:");
        String content = keyboard.readLine();

        return "[UPDATE]: NEW " + nodeName + " " + votingName + " " + content;
    }

    public static boolean checkExsistingVoting(String votingName)
    {
        for (int i = 0; i < votings.length; i++)
        {
            String[] s = votings[i].split(" ");
            if (votingName.equals(s[0])) return true;
        }
        return false;
    }

    public static String addVote(String votingName, String vote)
    {
        for (int i = 0; i < votings.length; i++)
        {
            String[] s = votings[i].split(" ");

            //CASE WHEN VOTED FOR 'YES'
            if (s[0].equals(votingName))
            {
                if (vote.equals("Y"))
                {
                    String[] s1 = s[1].split(":");
                    s1[1] = (Integer.parseInt(s1[1]) + 1) + "";
                    String result = String.join(":", s1[0], s1[1]);
                    votings[i] = String.join(" ", s[0], result, s[2]);
                }
                //CASE WHEN VOTED FOR 'NO'
                if (vote.equals("N"))
                {
                    String[] s2 = s[2].split(":");
                    s2[1] = (Integer.parseInt(s2[1]) + 1) + "";
                    String result = String.join(":", s2[0], s2[1]);
                    votings[i] = String.join(" ", s[0], s[1], result);
                }
                return "Voted";
            }
        }

        return "No voting of this name";
    }

    public static String getVotingsName()
    {
        //String[] result = new String[votings.length];
        for (int i = 0; i < votings.length; i++) {
            System.out.println(votings[i]);
        }
        for (int i = 0; i < nodes.length; i++) {
            System.out.println(nodes[i]);
        }
        return "";
    }
}

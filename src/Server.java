import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static final int PORT = 5017;
    private static ArrayList<ClientHandler> clients = new ArrayList<>();
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
    private static String[] votings  = new String[0];
    private static String[] nodes = new String[3];
    private static int id = 0;


    public static void main(String[] args) throws IOException
    {
        ServerSocket ss = new ServerSocket(PORT);


        while (true) {
            System.out.println("[SERVER]: Waiting for client connection...");
            Socket client = ss.accept();
            System.out.println("[SERVER]: Client connected!");

            ClientHandler clientThread = new ClientHandler(client, clients);
            clients.add(clientThread);

            pool.execute(clientThread);
        }
    }

    public static String sayHello(String name)
    {
        //"To start new vote enter: NEW nazwa_glosowania glos_w_formacie_YN tresc"
        //"To vote enter: VOTE nazwa_glosowania glos_w_formacie_YN"
        nodes[id] = name;
        id++;
        return "Hello " + name;
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

        return "NEW " + nodeName + " " + votingName + " " + content;
    }

    public static String addVote(String votingName, String vote)
    {
        for (int i = 0; i < votings.length; i++)
        {
            String[] s = votings[i].split(" ");
            if (s[0].equals(votingName))
            {
                if (vote.equals("Y"))
                {
                    String[] s1 = s[1].split(":");
                    s1[1] = (Integer.parseInt(s1[1]) + 1) + "";
                    String result = String.join(":", s1[0], s1[1]);
                    votings[i] = String.join(" ", s[0], result, s[2]);
                }
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
        for (int i = 0; i < votings.length; i++) {
            System.out.println(votings[i]);
        }
        return "";
    }
}

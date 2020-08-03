package Query;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static Query.SearchServer.getBooleanArray;
import static Query.SearchServer.makeTree;

public class SearchQueryServerGC {

    static List<BTreeByte> leafNodes;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        int NUMTESTS = Integer.parseInt(args[1]);
        int numNucleotide = Integer.parseInt(args[0]);
        List<BTreeByte> leafNodes = new ArrayList<>();
        BTreeByte root = makeTree(numNucleotide + ".hash", new BTreeByte(true), true, leafNodes);
        System.out.println(leafNodes.size());
        Socket socket = null;
        ServerSocket server = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            server = new ServerSocket(50001);
//            System.out.println("Server started");

//            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            String line = "";

            Runtime rt = Runtime.getRuntime();
            int position = 0;
            BTreeByte currentNode = root;
            // reads message from client until "Over" is sent
            long time1 = new Date().getTime();
            while (!line.equals("done")) {
                try {

                    line = in.readUTF();

//                    System.out.println(line);
                    if (!line.equals("done")) {
                        position = Integer.parseInt(line);
                        if (position == 0) {
                            out.writeUTF("send");
                            currentNode = root;
                            System.out.println("Initiated new Query");

                        }
                        if (position < 128) {


                            Process process = rt.exec("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 1 10000 0");
//                        System.out.println("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 1 10000 0");
                            BufferedReader inputCMD = new BufferedReader(new InputStreamReader(process.getInputStream()));
                            String cmd = null, output = "";
                            while ((cmd = inputCMD.readLine()) != null) {
                                output += cmd;
                            }
//                            System.out.println(output);
                            if (output.contains("same 1") && currentNode.getLeft() != null) {
                                out.writeUTF("send");
                                currentNode = currentNode.getLeft();
                            } else if (output.contains("same 0") && currentNode.getRight() != null) {
                                out.writeUTF("send");
                                currentNode = currentNode.getRight();

                            } else {
                                out.writeUTF("NF");
                                System.out.println("ending query from server");
                                currentNode = root;
                            }

                        }
//                System.out.println(Arrays.toString(res.sequences));
//                    assert (Arrays.toString(res.sequences).contains("" + queryIDS[ind]));
                    }

                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
            out.close();
            System.out.println("Server Difference " + (TimeUnit.SECONDS.convert((new Date().getTime() - time1), TimeUnit.SECONDS)) / (NUMTESTS * 1.0));
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}

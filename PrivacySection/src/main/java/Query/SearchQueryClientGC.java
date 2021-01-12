package Query;

import java.io.*;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static Query.SearchServer.getBooleanArray;
import static genomehashtree.ReverseMerkleWODB.addString;

public class SearchQueryClientGC {
    static int NUMTESTS = 1;
    static Socket socket = null;
    static DataInputStream input = null;
    static DataOutputStream out = null;
    final static byte[] randBytes = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20, -2, -10, -20, -52, 20};
    static int numNucleotide = 500;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        numNucleotide = Integer.parseInt(args[0]);

        NUMTESTS = Integer.parseInt(args[1]);
        String[] sequences = new String[numNucleotide];
        //original input
//        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
//        int index = 0;
//        while (sc.hasNext()) {
//            sequences[index++] = sc.nextLine();
//        }
        Random random = new Random(); //java.util.Random
        for (int i = 0; i < numNucleotide; i++) {
            StringBuilder test = new StringBuilder();
            for (int j = 0; j < numNucleotide; j++) {
                test.append(random.nextBoolean() ? "1" : "0");
            }
            sequences[i] = test.toString();
        }
        int length = 300;
//        boolean[][] querySequences = new boolean[NUMTESTS][length];
        String[] querySequences = new String[NUMTESTS];
        long time1 = new Date().getTime();

        //make random queries for SMM and PVSMM
        for (int ind = 0; ind < NUMTESTS; ind++) {
            int queryID = random.nextInt(numNucleotide);
            String test = sequences[queryID].substring(numNucleotide - length);
//            String randHashStr = Arrays.toString(randBytes);
//            String currentHashStr = addString(test, numNucleotide - length, randHashStr);
//            currentHashStr = currentHashStr.replace("[", "").trim();
//            currentHashStr = currentHashStr.replace("]", "");
////                        System.out.println(currentHashStr);
//            querySequences[ind] = getBooleanArray(currentHashStr);
            querySequences[ind] = test;
        }
        System.out.println("QP time " + (TimeUnit.SECONDS.convert((new Date().getTime() - time1), TimeUnit.SECONDS)) / (NUMTESTS * 1.0));
        //36.93
        time1 = new Date().getTime();
//        String randHashStr = Arrays.toString(randBytes);

        String IP ="127.0.0.1";// "44.224.230.229";//44.224.230.229,127.0.0.1
        sendGCData(IP, 50001, querySequences);


        System.out.println(" Q time " + (TimeUnit.SECONDS.convert((new Date().getTime() - time1), TimeUnit.SECONDS)) / (NUMTESTS * 1.0));

        socket.close();

    }

    private static void sendGCData(String address, int port, String[] queries) {

        String result = "";
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal

            input = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            // sends output to the socket


        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

//        // string to read message from input
//        String line = "";
//
//        // keep reading until "Over" is input

        try {
            Runtime rt = Runtime.getRuntime();
            int queryPos = 0;
            for (int i = 0; i < queries.length && queryPos < queries[i].length(); ) {
                int hashPosition = 0;
                boolean found = true;
                String query = queries[i].substring(queryPos);
                String randHashStr = Arrays.toString(randBytes);
                String currentHashStr = addString(query, queryPos, randHashStr);
                currentHashStr = currentHashStr.replace("[", "").trim();
                currentHashStr = currentHashStr.replace("]", "");
                System.out.println(queryPos);
                boolean[] queryBool = getBooleanArray(currentHashStr);

                out.writeUTF(hashPosition + "");//position 0
//                System.out.println(queries.length);
                result = input.readUTF();
//                System.out.println(result);

                while (result.equals("send") && hashPosition < queryBool.length && numNucleotide > queryPos) {//< query.length() - 1// && query.length() - queryStartPosition > (int) query.length() * .75//Threshold
                    Process process = rt.exec("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 2 10000 " + (queryBool[hashPosition] ? "1" : "0"));
//                    System.out.println("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 2 10000 " + (query[position] ? "1" : "0"));
                    BufferedReader inputCMD = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String cmd = null, output = "";
                    while ((cmd = inputCMD.readLine()) != null) {
                        output += cmd;
                    }
//                    System.out.println(output);
                    result = input.readUTF();
                    if (result.equals("NF")) {
                        queryPos++;
                        found = false;
                        break;

                    }
//                    System.out.println(result);
                    hashPosition++;
//                    System.out.println(position);
                    out.writeUTF(hashPosition + "");

                }
                if (found)
                    i += 1;

            }
            System.out.println("ending query");
            out.writeUTF("done");
            input.close();
            out.close();
        } catch (IOException | NoSuchAlgorithmException ex) {
            System.out.println(ex.getMessage());
        }


        return;
    }

    private static void sendGCData(String address, int port, boolean[][] queries) {

        String result = "";
        // establish a connection
        try {
            socket = new Socket(address, port);
            System.out.println("Connected");

            // takes input from terminal

            input = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            // sends output to the socket


        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }

//        // string to read message from input
//        String line = "";
//
//        // keep reading until "Over" is input

        try {
            Runtime rt = Runtime.getRuntime();

            for (boolean[] query : queries) {
                int position = 0;
                out.writeUTF(position + "");//position 0
//                System.out.println(queries.length);
                result = input.readUTF();
//                System.out.println(result);

                while (result.equals("send") && position < query.length) {//< query.length() - 1// && query.length() - queryStartPosition > (int) query.length() * .75//Threshold
                    Process process = rt.exec("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 2 10000 " + (query[position] ? "1" : "0"));
//                    System.out.println("/dspSharedData2/MominFiles/TreeCodes/GarbledCircuit/emp-sh2pc/bin/match 2 10000 " + (query[position] ? "1" : "0"));
                    BufferedReader inputCMD = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String cmd = null, output = "";
                    while ((cmd = inputCMD.readLine()) != null) {
                        output += cmd;
                    }
//                    System.out.println(output);
                    result = input.readUTF();
                    if (result.equals("NF")) break;
//                    System.out.println(result);
                    position++;
//                    System.out.println(position);
                    out.writeUTF(position + "");

                }


            }
            System.out.println("ending query");
            out.writeUTF("done");
            input.close();
            out.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }


        return;
    }
}

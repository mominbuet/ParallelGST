package Query;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static genomehashtree.ReverseMerkleWODB.addString;


public class SearchClient {
    final static int NUMTESTS = 10;
    static Socket socket = null;
    static DataInputStream input = null;
    static DataOutputStream out = null;
    final static byte[] randBytes = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20, -2, -10, -20, -52, 20};

    public static void closeConnection() {// close the connection
        try {
//            out = new DataOutputStream(socket.getOutputStream());
//            out.writeUTF("done");
//            out.close();
            socket.close();

        } catch (IOException i) {
            System.out.println(i.getMessage());
        }
    }

    public static String sendData(String address, int port, String[] queries) {

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
            for (String query : queries) {
                int queryStartPosition = 0;
                String randHashStr = Arrays.toString(randBytes);
                String currentHashStr = addString(query.substring(queryStartPosition), queryStartPosition, randHashStr);
                out.writeUTF(currentHashStr);
                result = input.readUTF();
//                System.out.println(result);

                while (result.equals("NF") && queryStartPosition==0 ) {//< query.length() - 1// && query.length() - queryStartPosition > (int) query.length() * .75//Threshold
                    queryStartPosition += 1;
                    currentHashStr = addString(query.substring(queryStartPosition), queryStartPosition, randHashStr);
                    out.writeUTF(currentHashStr);
                    result = input.readUTF();
//                    System.out.println(result);

                }
            }
            out.writeUTF("done");
            input.close();
            out.close();
        } catch (IOException | NoSuchAlgorithmException i) {
            System.out.println(i.getMessage());
        }


        return result;
    }


//    // constructor to put ip address and port
//    public static String sendDataOld(String address, int port, String[] queries) {
//        Socket socket = null;
//        DataInputStream input = null;
//        DataOutputStream out = null;
//        String result = "";
//        // establish a connection
//        try {
//            socket = new Socket(address, port);
//            System.out.println("Connected");
//
//            // takes input from terminal
//
//            input = new DataInputStream(
//                    new BufferedInputStream(socket.getInputStream()));
//            out = new DataOutputStream(socket.getOutputStream());
//            // sends output to the socket
//
//
//        } catch (IOException exception) {
//            System.out.println(exception.getMessage());
//        }
//
////        // string to read message from input
////        String line = "";
////
////        // keep reading until "Over" is input
//        for (String q : queries) {
//            try {
//
//                out.writeUTF(q);
//                result = input.readUTF();
//
//            } catch (IOException i) {
//                System.out.println(i.getMessage());
//            }
//        }
//
//        // close the connection
//        try {
//            out.writeUTF("done");
//            input.close();
//            out.close();
//            socket.close();
//        } catch (IOException i) {
//            System.out.println(i.getMessage());
//        }
//        return result;
//    }

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        int numNucleotide = 1000;

        String[] sequences = new String[numNucleotide];
        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
        }
        Random random = new Random(); //java.util.Random


        String[] querySequences = new String[NUMTESTS];
        int[] lengths = new int[]{0, 500, 600, 700};
        long time1 = new Date().getTime();

        //make random queries for SMM and PVSMM
        for (int ind = 0; ind < NUMTESTS; ind++) {
//            int length = 400;//random.nextInt(numNucleotide);
//                StringBuilder test = new StringBuilder();
//                for (int i = 0; i < length; i++) {
//                    test.append(random.nextBoolean() ? "1" : "0");
//                }
//                querySequences[ind] = test.toString();
            int queryID = random.nextInt(numNucleotide);
            querySequences[ind] = sequences[queryID].substring(lengths[2]);
        }
        //  make EM queries
//        int[] queryIDS = new int[NUMTESTS];
//        for (int ind = 0; ind < NUMTESTS; ind++) {
//            int queryID = random.nextInt(numNucleotide);
//            querySequences[ind] = sequences[queryID];
////            String currentHashStr = Arrays.toString(randBytes);
////            int queryStartPosition = random.nextInt(test.length() - 1);
////            currentHashStr = addString(test.substring(queryStartPosition), queryStartPosition, currentHashStr);
////            querySequences[ind] = currentHashStr;
////            queryIDS[ind] = queryID;
//
//        }
        System.out.println("QP time " + (TimeUnit.SECONDS.convert((new Date().getTime() - time1), TimeUnit.SECONDS)) / (NUMTESTS * 1.0));
        //36.93
        time1 = new Date().getTime();
//        String randHashStr = Arrays.toString(randBytes);

        String IP = "Server IP here";//127.0.0.1
        sendData(IP, 50001, querySequences);


        System.out.println(" Q time " + (TimeUnit.SECONDS.convert((new Date().getTime() - time1), TimeUnit.SECONDS)) / (NUMTESTS * 1.0));

        closeConnection();
    }
}

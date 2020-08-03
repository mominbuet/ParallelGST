/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Query;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author azizmma
 */
public class SearchServer {

    public static boolean[] convert(byte[] bits, int significantBits) {
        boolean[] retVal = new boolean[significantBits];
        int boolIndex = 0;
        for (int byteIndex = 0; byteIndex < bits.length; ++byteIndex) {
            for (int bitIndex = 7; bitIndex >= 0; --bitIndex) {
                if (boolIndex >= significantBits) {
                    // Bad to return within a loop, but it's the easiest way
                    return retVal;
                }

                retVal[boolIndex++] = (bits[byteIndex] >> bitIndex & 0x01) == 1 ? true
                        : false;
            }
        }
        return retVal;
    }

    /**
     * Converts a {@code byte[]} to a {@code boolean[]}. It is assumed that the
     * values are in most-significant-bit first order. Meaning that most
     * significant bit of the 0th byte of {@code bits} is the first boolean
     * value.
     *
     * @param bits a binary array of boolean values stored as a {@code byte[]}.
     * @return a {@code boolean[]} containing the same boolean values as the
     * {@code byte[]}
     */
    public static boolean[] convert(byte[] bits) {
        return convert(bits, bits.length * 8);
    }

    private static boolean[] convertToBooleanArray(byte[] bytes) {
        boolean[] result = new boolean[bytes.length * 8];

        for (int i = 0; i < bytes.length; i++) {
            int index = i * 8;
            result[index + 0] = (bytes[i] & 0x80) != 0;
            result[index + 1] = (bytes[i] & 0x40) != 0;
            result[index + 2] = (bytes[i] & 0x20) != 0;
            result[index + 3] = (bytes[i] & 0x10) != 0;
            result[index + 4] = (bytes[i] & 0x8) != 0;
            result[index + 5] = (bytes[i] & 0x4) != 0;
            result[index + 6] = (bytes[i] & 0x2) != 0;
            result[index + 7] = (bytes[i] & 0x1) != 0;
        }

        return result;
    }

    static byte[] getByteArray(String arr) {
        byte[] byteArr = new byte[16];
        String[] vals = arr.split(",");
        for (int i = 0; i < byteArr.length; i++) {
            byteArr[i] = Byte.parseByte(vals[i].trim());
        }
//        assert (Arrays.toString(byteArr).equals(arr));
        return byteArr;
    }

    static boolean[] getBooleanArray(String arr) {
        byte[] byteArr = new byte[16];
        String[] vals = arr.split(",");
        for (int i = 0; i < byteArr.length; i++) {
            byteArr[i] = Byte.parseByte(vals[i].trim());
        }
//        assert (Arrays.toString(byteArr).equals(arr));
        return convert(byteArr);
    }

//    static List<BTreeByte> leafNodes;
    static Map<Integer, List<byte[]>> substrMap;

    static BTreeByte makeTree(String fileName, BTreeByte root, boolean isInLeafNodes, List<BTreeByte> leafNodes) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(fileName));
        while (sc.hasNext()) {
            String line = sc.nextLine();
            String arr = line.split("]")[0];
            arr = arr.replace("[", "").trim();
            boolean[] booleanArr = getBooleanArray(arr);

            BTreeByte currentNode = root;
            for (int i = 0; i < booleanArr.length; i++) {

                BTreeByte tmp;
                if (booleanArr[i] && currentNode.right != null) {
                    tmp = currentNode.right;
                } else if (!booleanArr[i] && currentNode.left != null) {
                    tmp = currentNode.left;
                } else {
                    tmp = new BTreeByte(booleanArr[i], i == booleanArr.length - 1);

                    if (booleanArr[i]) {
                        currentNode.right = tmp;
                    } else {
                        currentNode.left = tmp;
                    }

                }
//                if (arr.equals("41, -66, 65, -99, 4, -102, -85, -6, -80, 24, 18, -85, -83, 91, 66, -28") || arr.equals("41, 106, -121, 80, -48, -11, -58, -34, -10, -12, 116, 120, -15, 26, 27, -45")) {
//                    System.out.print(tmp.content + ",");
//                }
                if (tmp.isLeaf && isInLeafNodes) {
                    tmp.sequences = line.split("]")[1].trim().split(",");
                    for (String seq : tmp.sequences) {
                        int postition = Integer.parseInt(seq.split(":")[1]);
//                        if (!substrMap.containsKey(postition)) {
//                            substrMap.put(postition, new ArrayList<>());
//                        }
//                        substrMap.get(postition).add(getByteArray(arr));
                    }
                    leafNodes.add(tmp);
                }

                currentNode = tmp;

            }

        }
        sc.close();
        return root;
    }



    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {
        int NUMTESTS = Integer.parseInt(args[1]);
        int numNucleotide = Integer.parseInt(args[0]);
//        Scanner sc = new Scanner(new File(numNucleotide + ".hash"));
//        BTreeByte root = new BTreeByte(true);
        List<BTreeByte> leafNodes = new ArrayList<>();
        BTreeByte root = makeTree(numNucleotide + ".hash", new BTreeByte(true), true ,leafNodes);
//        File[] listOfFiles = new File("Y:\\MominFiles\\TreeCodes\\GenomeMerkle\\hash500").listFiles();
//
//        for (File file : listOfFiles) {
//            root = makeTree(file.getAbsolutePath(), root,false);
//        }
        System.out.println(leafNodes.size());


        Socket socket = null;
        ServerSocket server = null;
        DataInputStream in = null;
        DataOutputStream out = null;
        try {
            server = new ServerSocket(50001);
            System.out.println("Server started");

            System.out.println("Waiting for a client ...");

            socket = server.accept();
            System.out.println("Client accepted");

            // takes input from the client socket
            in = new DataInputStream(
                    new BufferedInputStream(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
            String line = "";

            // reads message from client until "Over" is sent
            long time1 = new Date().getTime();
            while (!line.equals("done")) {
                try {
                    line = in.readUTF();
//                    System.out.println(line);
                    if (!line.equals("done")) {
                        String currentHashStr = line;
                        currentHashStr = currentHashStr.replace("[", "").trim();
                        currentHashStr = currentHashStr.replace("]", "");
//                        System.out.println(currentHashStr);
                        boolean[] testQuery = getBooleanArray(currentHashStr);
                        BTreeByte res = root.find(testQuery);
                        if (res != null) {
                            out.writeUTF(Arrays.toString(res.sequences));

                        } else {
                            out.writeUTF("NF");
                        }

                    }
//                System.out.println(Arrays.toString(res.sequences));
//                    assert (Arrays.toString(res.sequences).contains("" + queryIDS[ind]));


                } catch (IOException ex) {
                    System.out.println(ex.getMessage());
                }
            }
            System.out.println("Closing connection");

            // close connection
            socket.close();
            in.close();
            out.close();
            System.out.println("Difference " + (TimeUnit.NANOSECONDS.convert((new Date().getTime() - time1), TimeUnit.NANOSECONDS)) / (NUMTESTS * 1.0));
        } catch (IOException ex) {
            System.out.println(ex);
        }

        //make queries


    }
}

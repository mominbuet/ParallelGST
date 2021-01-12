/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import static genomehashtree.PrefixTree.appendBytes;
import static genomehashtree.UpdateHash.fromString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author azizmma
 */
public class ReverseMerkleWODB {

    public static final String ALGORITHM = "MD5";
    public static final String secretKey = "SecretKey!!!";

    public static String addStringWOPosition(String test, int position, String prevHashData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
//        String currHashString = "";
        char[] chrs = test.toCharArray();
        byte[] prev_hash = fromString(prevHashData);
        for (int i = 0; i < chrs.length; i++) {
//            byte[] prev_hash = fromString(prevHashData);
            byte[] current_hash = md.digest((chrs[i] + "").getBytes(StandardCharsets.UTF_8));
            prev_hash = md.digest(appendBytes(current_hash, prev_hash));
//            prevHashData = Arrays.toString(new_hash);

        }
//        byte[] current_hash = md.digest(("S" + position).getBytes(StandardCharsets.UTF_8));
//        prev_hash = md.digest(appendBytes(current_hash, prev_hash));
        return Arrays.toString(prev_hash);
    }

    public static String addStringFile(String test, int sequenceID, int position, String prevHashData) throws NoSuchAlgorithmException, UnsupportedEncodingException, FileNotFoundException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        Map<byte[], String> allHashVals = new HashMap<>();
//        String currHashString = "";
        char[] chrs = test.toCharArray();
        byte[] prev_hash = fromString(prevHashData);
        for (int i = 0; i < chrs.length; i++) {
//            byte[] prev_hash = fromString(prevHashData);
            byte[] current_hash = md.digest((chrs[i] + "").getBytes(StandardCharsets.UTF_8));
            prev_hash = md.digest(appendBytes(current_hash, prev_hash));
//            prevHashData = Arrays.toString(new_hash);
            if (!allHashVals.containsKey(prev_hash)) {
                allHashVals.put(prev_hash, position + ":" + i);
            }

        }
//        PrintWriter pw = new PrintWriter("hash\\" + sequenceID + "_" + position + ".txt");
//        for (byte[] q : allHashVals.keySet()) {
//            pw.println(Arrays.toString(q) + "\t" + allHashVals.get(q));
//        }
//        pw.close();
//        pw.flush();

        byte[] current_hash = md.digest(("S" + position).getBytes(StandardCharsets.UTF_8));
        prev_hash = md.digest(appendBytes(current_hash, prev_hash));
        return Arrays.toString(prev_hash);
    }

    public static String addString(String test, int position, String prevHashData) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        List<byte[]> allHashVals = new ArrayList<>();
//        String currHashString = "";
        char[] chrs = test.toCharArray();
        byte[] prev_hash = fromString(prevHashData);
        for (int i = 0; i < chrs.length; i++) {
//            byte[] prev_hash = fromString(prevHashData);
            byte[] current_hash = md.digest((chrs[i] + "").getBytes(StandardCharsets.UTF_8));
            prev_hash = md.digest(appendBytes(current_hash, prev_hash));
//            prevHashData = Arrays.toString(new_hash);
            if (!allHashVals.contains(prev_hash)) {
                allHashVals.add(prev_hash);
            }

        }

        byte[] current_hash = md.digest(("S" + position).getBytes(StandardCharsets.UTF_8));
        prev_hash = md.digest(appendBytes(current_hash, prev_hash));
        return Arrays.toString(prev_hash);
    }

    public static void main(String[] args) throws Exception {
        int numNucleotide = 1000;

        String[] sequences = new String[numNucleotide];
        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
        }
//        try (PrintWriter pw = new PrintWriter(new File(numNucleotide + "x" + sequences.length + ".txt"))) {
//            for (int i = 0; i < sequences.length; i++) {
//                sequences[i] = "";
//                for (int j = 0; j < numNucleotide; j++) {
//                    sequences[i] += getNucleotide();
//                }
//            pw.println(sequences[i]);
//            }
//            pw.flush();
//            pw.close();
//        }
//        Node root = new Node();
        byte[] randBytes = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20,-2, -10, -20,-52, 20};
        Map<String, List<String>> hashValueMap = new HashMap<>();

//        List<String> hashVals = new ArrayList<>();
//        List<SuffixTreePlainMerkle> leafNodes = new ArrayList<>();
        int dup = 0;//46
        for (int i = 0; i < sequences.length; i++) {
            String test = sequences[i];
//            System.out.println("Processing " + i);
            for (int j = test.length() - 1; j >= 0; j--) {
                String currentHashStr = Arrays.toString(randBytes);
                currentHashStr = addString(test.substring(j),  j, currentHashStr);
                String encryptedString = Arrays.toString(AESUtils.encrypt(currentHashStr, secretKey));

                if (!hashValueMap.containsKey(encryptedString)) {
                    hashValueMap.put(encryptedString, new ArrayList<>());
                }
                hashValueMap.get(encryptedString).add(i + ":" + j);
//                if (hashValueMap.get(currentHashStr).size()>1)
//                    System.out.println(String.join(",", hashValueMap.get(currentHashStr)));
            }
            if (i % 10 == 0) {
                System.out.println("done " + i);
            }

        }
        PrintWriter pw = new PrintWriter(new File(sequences.length + ".hash"));
        for (Map.Entry<String, List<String>> entrySet : hashValueMap.entrySet()) {
            pw.println(entrySet.getKey() + "\t" + String.join(",", entrySet.getValue()));

        }
        pw.flush();
        pw.close();

//        System.out.println("Duplicates " + dup);
//        root = new Node();
//        Node query = addStringMerkle(root, sequences[0].substring(200), "query1");
//        System.out.println(Arrays.toString(query.getHash()));
//
//        leafNodes.stream().filter((n) -> (n.equals(query))).forEach((n) -> {
//            System.out.println("Match " + n.getId());
//        });
    }
}

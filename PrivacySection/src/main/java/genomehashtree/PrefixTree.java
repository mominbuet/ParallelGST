/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import DB.PrefixTreePlain;
import DB.QueryDB;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author azizmma
 */
public class PrefixTree {

    static final String ALGORITHM = "MD5";

    /**
     * @param args the command line arguments
     */
    public static byte[] appendBytes(byte[] a, byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public static byte[] addBytes(byte[] a, byte[] b) {

        byte[] tmp = new byte[a.length];
        for (int i = 0; i < ((a.length < b.length) ? a.length : b.length); i++) {
            tmp[i] = (byte) (a[i] ^ b[i]);
        }
        return tmp;
    }

    static PrefixTreePlain addStringDB(PrefixTreePlain previous_node, String test, String id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        char[] chrs = test.toCharArray();
        QueryDB queryDB = new QueryDB();
        boolean createAlways = false;
        for (int i = 0; i < chrs.length; i++) {
            String substr = test.substring((i > 20) ? i - 20 : 0, (i > 0) ? i - 1 : 0);

            PrefixTreePlain current_node = (!createAlways) ? queryDB.getPrefixTreePlain(i, chrs[i] + "", previous_node) : null;
            if (current_node == null) {
                createAlways = true;
                current_node = new PrefixTreePlain();
                current_node.setData(chrs[i] + "");
                current_node.setLevel(i);
                current_node.setParentId(previous_node.getId());
                current_node.setHashData(addBytes(md.digest((chrs[i] + "").getBytes()), md.digest(substr.getBytes())).toString());
                current_node.setIsLeaf(i == chrs.length - 1);
                current_node.setRowId(id + ",");
                queryDB.insertGeneric(current_node);
//                System.out.println("Successfully inserted " + current_node.getId());
            } else {
                queryDB.updateRowId(current_node, id);
            }

            //one char
            //current_node.setHash(addBytes(previous_node.getHash(), md.digest((current_node.getData() + "").getBytes(StandardCharsets.UTF_8))));
//substring
//            previous_node.addChildren(current_node);
            previous_node = current_node;
        }
        return previous_node;
    }

    static Node addString(Node previous_node, String test) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        char[] chrs = test.toCharArray();

        for (int i = 0; i < chrs.length; i++) {
            String substr = test.substring((i > 20) ? i - 20 : 0, (i > 0) ? i - 1 : 0);

            Node current_node = previous_node.findChildren(chrs[i]);
            if (current_node == null) {
                current_node = new Node(chrs[i], "");
            }

            current_node.setParent(previous_node);
            //one char
            //current_node.setHash(addBytes(previous_node.getHash(), md.digest((current_node.getData() + "").getBytes(StandardCharsets.UTF_8))));
//substring

            current_node.setHash(addBytes(md.digest((chrs[i] + "").getBytes()), md.digest(substr.getBytes())));
            previous_node.addChildren(current_node);
            previous_node = current_node;
        }
        return previous_node;
    }

    static char getNucleotide() {
        double val = new Random().nextDouble();
        if (val < .5) {
            return '0';
        } //        else if (val < .5) {
        //            return '1';
        //        } else if (val < .75) {
        //            return 'T';
        //        } 
        else {
            return '1';
        }
    }

    public static void main1(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
        int numNucleotide = 10000;
        String[] sequences = new String[10000];
//        PrintWriter pw = new PrintWriter(new File(numNucleotide + "x" + sequences.length + ".txt"));
//        for (int i = 0; i < sequences.length; i++) {
//            sequences[i] = "";
//            for (int j = 0; j < numNucleotide; j++) {
//                sequences[i] += getNucleotide();
//            }
//            pw.println(sequences[i]);
//        }
//        pw.flush();
//        pw.close();

        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
        }

        PrefixTreePlain root = new QueryDB().searchRoot();
        if (root == null) {
            root = new PrefixTreePlain();
            root.setData("root");
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            root.setHashData(md.digest((root.getData() + "").getBytes(StandardCharsets.UTF_8)).toString());
            root.setLevel(-1);
            root.setRowId("-1");
            root.setParentId(null);
            root.setIsLeaf(false);
            new QueryDB().insertGeneric(root);
        }
        //top down approach     
        List<PrefixTreePlain> leafNodes = new ArrayList<>();
        List<String> hashVals = new ArrayList<>();
        int dup = 0;
        for (int i = 0; i < sequences.length; i++) {
            String test = sequences[i];
            System.out.println("Processing " + i);
            PrefixTreePlain testNode = addStringDB(root, test, i + "");
            String tmp = testNode.getHashData();
            if (!hashVals.contains(tmp)) {
                hashVals.add(tmp);
            } else {

                dup++;
            }
            leafNodes.add(testNode);
        }
        //find duplicate

        System.out.println("Duplicate found " + dup);

//        System.out.println(Arrays.toString(addString(root, sequences[0]).getHash()));
//        System.out.println(Arrays.toString(leafNodes.get(0).getHash()));
//        
//        Node test = addString(root, sequences[0].substring(0, 100));
//        test = addString(test, sequences[0].substring(101, sequences[0].length() - 2));
//        System.out.println(Arrays.toString(test.getHash()));
    }

}

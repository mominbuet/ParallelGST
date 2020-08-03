/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import DB.PrefixTreePlain;
import DB.QueryDB;
import DB.SuffixTreePlain;
import static genomehashtree.PrefixTree.ALGORITHM;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import static genomehashtree.PrefixTree.ALGORITHM;
import static genomehashtree.PrefixTree.addBytes;

/**
 *
 * @author azizmma
 */
public class SuffixTree {

    static SuffixTreePlain addStringDB(SuffixTreePlain previous_node, String test, String id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        char[] chrs = test.toCharArray();
        QueryDB queryDB = new QueryDB();
        boolean createAlways = false;
        for (int i = 0; i < chrs.length; i++) {
            String substr = test.substring((i > 20) ? i - 20 : 0, (i > 0) ? i - 1 : 0);

            SuffixTreePlain current_node = (!createAlways) ? queryDB.getSuffixTreePlain(i, chrs[i] + "", previous_node) : null;
            if (current_node == null) {
                createAlways = true;
                current_node = new SuffixTreePlain();
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

    public static void main1(String[] args) throws NoSuchAlgorithmException, FileNotFoundException {
        int numNucleotide = 10000;
        String[] sequences = new String[10000];
        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
        }
        SuffixTreePlain root = new QueryDB().searchRootSuffix();
        if (root == null) {
            root = new SuffixTreePlain();
            root.setData("root");
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            root.setHashData(md.digest((root.getData() + "").getBytes(StandardCharsets.UTF_8)).toString());
            root.setLevel(-1);
            root.setRowId("-1");
            root.setParentId(null);
            root.setIsLeaf(false);
            new QueryDB().insertGeneric(root);
        }

        List<SuffixTreePlain> leafNodes = new ArrayList<>();
//        List<String> hashVals = new ArrayList<>();
//        int dup = 0;
        for (int i = 0; i < sequences.length; i++) {
            String test = sequences[i];

            for (int j = test.length()-100; j >= 0; j--) {
                System.out.println("Processing " + i + "[" + j + ":" + test.length() + "]");
//                System.out.println(test.substring((test.length() - j), test.length())+"$");
                SuffixTreePlain testNode = addStringDB(root, test.substring(j, test.length()) + "$", i + "[" + j + ":" + test.length() + "]");

                leafNodes.add(testNode);
            }
        }

    }
}

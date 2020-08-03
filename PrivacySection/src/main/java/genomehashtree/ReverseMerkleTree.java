/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import DB.QueryDB;
import DB.StreeMap;
import DB.SuffixTreePlainMerkle;
import static genomehashtree.PrefixTree.appendBytes;
import static genomehashtree.UpdateHash.fromString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author azizmma
 */
public class ReverseMerkleTree {

    public static final String ALGORITHM = "MD5";

    public static Node addStringMerkle(Node previous_node, String test, String id) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        char[] chrs = test.toCharArray();

        for (int i = 0; i < chrs.length; i++) {
//            String substr = test.substring((i > 20) ? i - 20 : 0, (i > 0) ? i - 1 : 0);

            Node current_node = previous_node.findChildren(chrs[i]);
            if (current_node == null) {
                current_node = new Node(chrs[i], "");
            }

            current_node.setParent(previous_node);
            current_node.setId(id);
            //one char
            //current_node.setHash(addBytes(previous_node.getHash(), md.digest((current_node.getData() + "").getBytes(StandardCharsets.UTF_8))));
//substring

            current_node.setHash(md.digest(appendBytes(md.digest((chrs[i] + "").getBytes()), previous_node.getHash())));
            previous_node.addChildren(current_node);
            previous_node = current_node;
        }
        return previous_node;
    }
//    static List<SuffixTreePlainMerkle> toInsert = new ArrayList<>();
//    static Map<String, SuffixTreePlainMerkle> uniqueConstraintMap = new HashMap<>();

//    static int node_id = 1;
    public static SuffixTreePlainMerkle addStringDB(SuffixTreePlainMerkle previous_node, String test, String rowID, boolean toInsert, int sequenceID,int position) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);

        char[] chrs = test.toCharArray();
        QueryDB queryDB = new QueryDB();
        boolean createAlways = false;
        for (int i = 0; i < chrs.length; i++) {
//            String key = new String(md.digest((i + ":" + chrs[i] + ":" + previous_node.getId()).getBytes(StandardCharsets.UTF_8)));
//            SuffixTreePlainMerkle current_node = (!createAlways) ? uniqueConstraintMap.containsKey(key) ? uniqueConstraintMap.get(key) : null : null;
            SuffixTreePlainMerkle current_node = (createAlways) ? null : queryDB.getSuffixTreePlainMerkle(i, chrs[i] + "", previous_node.getId(), i == chrs.length - 1);
            if (current_node == null) {
                createAlways = true;
                current_node = new SuffixTreePlainMerkle();
                current_node.setData(chrs[i] + "");
                current_node.setLevel(i);
                current_node.setParentId(previous_node.getId());
                byte[] prev_hash = fromString(previous_node.getHashData());
                byte[] current_hash = md.digest((chrs[i] + "").getBytes(StandardCharsets.UTF_8));
                byte[] new_hash = md.digest(appendBytes(current_hash, prev_hash));
                current_node.setHashData(Arrays.toString(new_hash));
//                current_node.setHashData(addBytes(md.digest((chrs[i] + "").getBytes()), md.digest(substr.getBytes())).toString());
                current_node.setIsLeaf(i == chrs.length - 1);
                current_node.setRowId(rowID + ",");
                if (toInsert) {
                    queryDB.insertGeneric(current_node);
                    queryDB.updateRowId(current_node, sequenceID,position);
                }
//                toInsert.add(current_node);
//                uniqueConstraintMap.put(key, current_node);
//                queryDB.insertGeneric(current_node);
                //                System.out.println("Successfully inserted " + current_node.getId());
            } else {

//                current_node.setRowId(current_node.getRowId() + rowID + ",");
                if (toInsert) {
                    queryDB.updateRowId(current_node, sequenceID,position);
                    
                }
            }

            //one char
            //current_node.setHash(addBytes(previous_node.getHash(), md.digest((current_node.getData() + "").getBytes(StandardCharsets.UTF_8))));
//substring
//            previous_node.addChildren(current_node);
            previous_node = current_node;
        }
        return previous_node;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, UnsupportedEncodingException {
        int numNucleotide = 1000;

        String[] sequences = new String[1000];
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
        byte[] randBytes = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20};
        SuffixTreePlainMerkle root = new QueryDB().searchRootSuffixMerkle();
        if (root == null) {
            root = new SuffixTreePlainMerkle();
            root.setData("root");
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            root.setHashData(Arrays.toString(md.digest(appendBytes(randBytes, root.getData().getBytes(StandardCharsets.UTF_8)))));
            root.setLevel(-1);
            root.setParentId(0);
            root.setRowId("-1");
            root.setIsLeaf(false);
            new QueryDB().insertGeneric(root);
        }
//        List<String> hashVals = new ArrayList<>();
//        List<SuffixTreePlainMerkle> leafNodes = new ArrayList<>();
        int dup = 0;//46
        for (int i = 62; i < sequences.length; i++) {
            String test = sequences[i];
//            System.out.println("Processing " + i);

            for (int j = test.length() - 1; j >= 0; j--) {

//                if (j > 802) {
//                    continue;
//                }
                SuffixTreePlainMerkle testNode = addStringDB(root, test.substring(j, test.length()), i + ":" + j, true,i,j);
                if (testNode!=null)
                    System.out.println("Inserted " + i + "[" + j + ":" + test.length() + "]");
//                String tmp = testNode.getHashData();
//                if (!hashVals.contains(tmp)) {
//                    hashVals.add(tmp);
//                } else {
//
//                    dup++;
//                }
//                if (j % 1 == 0) {
//                    new QueryDB().insertGeneric(toInsert);
//                    toInsert = new ArrayList<>();
//                }
//                leafNodes.add(testNode);

//                if (i == 2) {
//                    break;
//                }
            }
        }

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

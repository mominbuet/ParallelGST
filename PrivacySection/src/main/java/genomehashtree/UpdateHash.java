/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import DB.QueryDB;
import DB.SuffixTreePlainMerkle;
import static genomehashtree.PrefixTree.appendBytes;
import static genomehashtree.ReverseMerkleTree.ALGORITHM;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author azizmma
 */
public class UpdateHash {

    public static final String ALGORITHM = "MD5";

    public static byte[] fromString(String input) {
        input = input.replace("[", "");
        input = input.replace("]", "");
        String[] bytes = input.split(",");
        byte[] result = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            result[i] = Byte.valueOf(bytes[i].trim());
        }
        return result;
    }

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, UnsupportedEncodingException {
        int numNucleotide = 1000;

        String[] sequences = new String[1000];
        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
        }
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        QueryDB queryDB = new QueryDB();
        SuffixTreePlainMerkle root = queryDB.searchRootSuffixMerkle();
//        System.out.println(Arrays.toString(md.digest(root.getData().getBytes(StandardCharsets.UTF_8))));
//        System.out.println(Arrays.toString(md.digest(root.getData().getBytes(StandardCharsets.UTF_8))));

        byte[] randBytes = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20};
        byte[] root_hash = md.digest(appendBytes(randBytes,
                root.getData().getBytes(StandardCharsets.UTF_8)));
        System.out.println(Arrays.toString(root_hash));
//        root.setHashData(Arrays.toString(root_hash));
//        queryDB.updateHash(root);
        for (int i = 0; i < 999; i++) {

        }
    }
}

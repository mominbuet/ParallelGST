/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Query;

import DB.QueryDB;
import DB.SuffixTreePlainMerkle;
import static genomehashtree.PrefixTree.appendBytes;
import genomehashtree.ReverseMerkleTree;
import static genomehashtree.ReverseMerkleTree.ALGORITHM;
import static genomehashtree.UpdateHash.fromString;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author azizmma
 */
public class SearchQuery {
    
    static String getHash(String query, byte[] old_hash) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        
        char[] chrs = query.toCharArray();
        for (int i = 0; i < chrs.length; i++) {
            
            byte[] current_hash = md.digest((chrs[i] + "").getBytes(StandardCharsets.UTF_8));
            old_hash = md.digest(appendBytes(current_hash, old_hash));
            
        }
//        String arrayString = Arrays.toString(old_hash);
//        System.out.println(arrayString);
//        System.out.println(Arrays.toString(old_hash.toString().getBytes(StandardCharsets.UTF_8)));
        return Arrays.toString( old_hash);
    }
    
    public static void main(String[] args) throws FileNotFoundException, NoSuchAlgorithmException, UnsupportedEncodingException {
        int numNucleotide = 1000;
        
        String[] sequences = new String[1000];
        Scanner sc = new Scanner(new File(numNucleotide + "x" + sequences.length + ".txt"));
        int index = 0;
        
        while (sc.hasNext()) {
            sequences[index++] = sc.nextLine();
            if (index > 40) {
                break;
            }
        }
        
        Random rand = new Random();
        int randomSequence = 3;//rand.nextInt(40);
        int randomPosition = 670;//rand.nextInt(sequences.length);
        String query = sequences[randomSequence].substring(randomPosition);
        SuffixTreePlainMerkle root = new QueryDB().searchRootSuffixMerkle();
        
//        System.out.println(query);
//        System.out.println(root.getHashData());
//        byte[] rootBytes = md.digest((root.getData()+"").getBytes(StandardCharsets.UTF_8));
//        System.out.println(md.digest((root.getData()+"").getBytes(StandardCharsets.UTF_8)).toString());
//        System.out.println(md.digest((root.getData()+"").getBytes(StandardCharsets.UTF_8)).toString());
//        System.out.println(root.getHashData());
        
        String queryHash = getHash(query,fromString(root.getHashData()));
        System.out.println(queryHash);
    }
}

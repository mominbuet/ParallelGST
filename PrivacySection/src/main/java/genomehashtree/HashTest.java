/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import static genomehashtree.PrefixTree.addBytes;
import static genomehashtree.PrefixTree.appendBytes;
import static genomehashtree.ReverseMerkleWODB.ALGORITHM;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 *
 * @author azizmma
 */
public class HashTest {

    public static void main(String[] args) throws NoSuchAlgorithmException, FileNotFoundException, UnsupportedEncodingException {
        MessageDigest md = MessageDigest.getInstance(ALGORITHM);
        byte[] randBytes1 = new byte[]{12, 10, 14, 54, 56, 25, 2, 51, 1, 54, 20,10,1, 54, 20,10};
        byte[] randBytes2 = new byte[]{-12, -10, -14, 54, 56, 25, 2, 51, 1, 54, 20,54,11, 54, 20,10};
        byte[] current_hash = md.digest(("A").getBytes(StandardCharsets.UTF_8));
        byte[] new_hash = md.digest(appendBytes(current_hash, randBytes1));

        byte[] CSReturn = new byte[new_hash.length];
        for (int i = 0; i < new_hash.length; i++) {
            CSReturn[i] = (byte) (new_hash[i] ^ randBytes2[i]);
        }

        System.out.println(Arrays.toString(new_hash));
        System.out.println(Arrays.toString(CSReturn));
        for (int i = 0; i < new_hash.length; i++) {
            CSReturn[i] = (byte) (CSReturn[i] ^ randBytes2[i]);
        }
        System.out.println(Arrays.toString(CSReturn));

    }
}

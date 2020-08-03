/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package genomehashtree;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author azizmma
 */
public class Node {

    private char data;
    private byte[] hash;
    private String sequence, id;
    private Node parent;
    private List<Node> children;

    public Node() {
        data = 'r';
        this.id = "root";
        this.children = new ArrayList<>();
        this.hash = new byte[]{100, 20, 82, -56, -54, 49, -110, -1, -61, 90, -91, -69, -98, 28, -49, 30};
    }

    public Node(char data, String sequence) {
        this.data = data;
        this.sequence = sequence;
//            this.hash = getSHA(data + "");
        this.children = new ArrayList<>();
    }

    public char getData() {
        return data;
    }

    public void setData(char data) {
        this.data = data;
    }

    public byte[] getHash() {
        return hash;
    }

    public static byte[] getSHA(String input) throws NoSuchAlgorithmException {
        // Static getInstance method is called with hashing SHA  
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        // digest() method called  
        // to calculate message digest of an input  
        // and return array of byte 
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node findChildren(char ch) {
        for (Node n : this.children) {
            if (n.data == ch) {
                return n;
            }
        }
        return null;
    }

    public void addChildren(Node child) {
        this.children.add(child);
    }

    public List<Node> getChildren() {
        return children;
    }

    public void setChildren(List<Node> children) {
        this.children = children;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Node other = (Node) obj;
        if (!Arrays.equals(this.hash, other.hash)) {
            return false;
        }
        return true;
    }

}

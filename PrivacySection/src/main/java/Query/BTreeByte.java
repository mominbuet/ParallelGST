/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Query;

import java.util.List;

/**
 *
 * @author azizmma
 */
public class BTreeByte {

    BTreeByte left, right;
    boolean content, isLeaf, isRoot;
    public String[] sequences;

    public BTreeByte(boolean content, boolean isLeaf) {
        this.content = content;
        this.isLeaf = isLeaf;
        this.isRoot = false;
        this.sequences = null;
    }

    BTreeByte(boolean b) {
        this.isRoot = b;
    }

    public BTreeByte getLeft() {
        return left;
    }

    public void setLeft(BTreeByte left) {
        this.left = left;
    }

    public BTreeByte getRight() {
        return right;
    }

    public void setRight(BTreeByte right) {
        this.right = right;
    }

    public boolean isContent() {
        return content;
    }

    public void setContent(boolean content) {
        this.content = content;
    }

    public boolean isIsRoot() {
        return isRoot;
    }

    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    BTreeByte find(boolean[] testQuery) {
        BTreeByte res = this;
        for (int i = 0; i < testQuery.length; i++) {
//            System.out.println(i);
            if (testQuery[i]) {
                res = res.right;
            } else {
                res = res.left;
            }
            if (res == null) {
                return null;
            }
        }
        return res;
    }

}

package src.niccolaibalica;

import java.lang.Math;

public class avlTree<V> implements dictionary<V> {

    private class avlNode {
        avlNode left, right;
        V data;
        String key;
        int height;

        /* Constructor */
        protected avlNode(V value, String k) {
            left = null;
            right = null;
            data = value;
            key = k;
            height = 0;
        }

        public String getKey() {
            return key;
        }

        public V getData() {
            return data;
        }

        public void setData(V data) {
            this.data = data;
        }

        public avlNode getLeft() {
            return left;
        }

        public void setLeft(avlNode left) {
            this.left = left;
        }

        public avlNode getRight() {
            return right;
        }

        public void setRight(avlNode right) {
            this.right = right;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int h) {
            this.height = h;
        }
    }

    private avlNode root;
    private int nNodes;
    final Class<V> param;

    /* Constructor */
    public avlTree(Class<V> p)
    {
        root = null;
        nNodes = 0;
        this.param = p;
    }
    /* Function to check if tree is empty */
    public boolean isEmpty()
    {
        return root == null;
    }
    /* Make the tree logically empty */
    public void makeEmpty()
    {
        root = null;
    }
    
    //ricerca, torna null se non trova elemento

    public V search(String searchKey)
    {
        return search(searchKey, root).getData();
    }
    
    private avlNode search(String searchKey, avlNode r)
    {
        avlNode found = null;

        while (r != null)
        {
            String rkey = r.getKey();
            if (rkey.compareToIgnoreCase(searchKey) < 0)
                r = r.left;
            else if (rkey.compareToIgnoreCase(searchKey) > 0)
                r = r.right;
            else
            {
                found = r;
                break;
            }
            found = search(searchKey, r);
        }
        return found;
    }

    public void insert(String k, V data) {
        root = insert(root, k, data);
    }

    private avlNode insert(avlNode node, String k, V data) {
        if (node == null) {
            return new avlNode(data, k);
        }

        if (node.getKey().compareToIgnoreCase(k) < 0) {
            node.setLeft(insert(node.getLeft(), k, data));
        } else {
            node.setRight(insert(node.getRight(), k, data));
        }

        node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);

        return checkBalanceAndRotate(k, node);// (!!! non finito)
    }
    
    
    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ R O T A Z I O N I $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    avlNode rightRotation(avlNode node) { // input C
        avlNode newParentNode = node.getLeft(); // newParentNode = B
        avlNode mid = newParentNode.getRight(); // store B's right node 'mid' (B < mid < C)

        newParentNode.setRight(node); // C now becomes right node of B
        node.setLeft(mid); // 'mid' now becomes left node of C

        node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);
        newParentNode.setHeight(Math.max(getHeight(newParentNode.getLeft()), getHeight(newParentNode.getRight())) + 1);

        return newParentNode; // return B as the parent of A and C
    }

    avlNode leftRotation(avlNode node) { // input A
        avlNode newParentNode = node.getRight(); // newParentNode = B
        avlNode mid = newParentNode.getLeft(); // store B's left node 'mid' (A < mid < B)

        newParentNode.setLeft(node); // A now becomes left node of B
        node.setRight(mid); // 'mid' now becomes right node of A

        node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);
        newParentNode.setHeight(Math.max(getHeight(newParentNode.getLeft()), getHeight(newParentNode.getRight())) + 1);

        return newParentNode; // return B as the parent of A and C
    }

    avlNode leftRightRotation(avlNode node) { // input C
        // leftRotation(A) -> result B, then set B as left node of C
        node.setLeft(leftRotation(node.getLeft()));

        // rightRotation(C)
        return rightRotation(node);
    }

    avlNode rightLeftRotation(avlNode node) { // input A
        // rightRotation(C) -> result B, then set B as right node of A
        node.setRight(rightRotation(node.getRight()));

        // leftRotation(A)  
        return leftRotation(node);
    }

    private int getHeight(avlNode node) {

        if (node == null) {
            return -1;
        }

        return node.getHeight();
    }
}
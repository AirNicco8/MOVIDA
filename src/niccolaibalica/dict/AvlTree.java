package src.niccolaibalica.dict;

import java.lang.Math;
import java.lang.reflect.Array;

public class AvlTree<V> implements Dictionary<V> {

	private class AvlNode<V> {
		AvlNode left, right;
		V data;
		String key;
		int height;

		/* Constructor */
		protected AvlNode(V value, String k) {
			left = null;
			right = null;
			data = value;
			key = k;
			height = 0;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String k) {
			this.key = k;
		}

		public V getData() {
			return data;
		}

		public void setData(V data) {
			this.data = data;
		}

		public AvlNode getLeft() {
			return left;
		}

		public void setLeft(AvlNode left) {
			this.left = left;
		}

		public AvlNode getRight() {
			return right;
		}

		public void setRight(AvlNode right) {
			this.right = right;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int h) {
			this.height = h;
		}

		@Override
		public String toString() {
			return "" + this.getKey();
		}
	}

	private AvlNode root;
	final Class<V> param;

	/* Constructor */
	public AvlTree(Class<V> p)
	{
		root = null;
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

	public int count()
	{
		return (size(root));
	}

	private int size(AvlNode node) {
		if (node == null) return(0);
		else {
			return(size(node.getLeft()) + 1 + size(node.getRight()));
		}
	}

	//ricerca, torna null se non trova elemento

	public V search(String searchKey) throws ExceptionKeyNotFound
	{
		AvlNode n = search(searchKey, root);
		if(n != null)
			return (V) n.getData();
		else
			return null;
	}

	private AvlNode search(String searchKey, AvlNode r) throws ExceptionKeyNotFound
	{
		AvlNode found = null;

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

	// inserimento

	private AvlNode insert(AvlNode node, String k, V data) {
		if (node == null) {
			return new AvlNode(data, k);
		}

		if (node.getKey().compareToIgnoreCase(k) < 0) {
			node.setLeft(insert(node.getLeft(), k, data));
		} else {
			node.setRight(insert(node.getRight(), k, data));
		}

		node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);

		return checkBalanceAndRotate(k, node);
	}

	private AvlNode checkBalanceAndRotate(String key, AvlNode node) {
		int balance = getBalance(node);

		// left-left
		if ((balance > 1) && (node.getLeft().getKey().compareToIgnoreCase(key) < 0)) {
			return rightRotation(node);
		}

		// right-right
		if ((balance < -1) && node.getRight().getKey().compareToIgnoreCase(key) > 0) {
			return leftRotation(node);
		}

		// left-right
		if ((balance > 1) && (node.getLeft().getKey().compareToIgnoreCase(key) < 0)) {
			node.setLeft(leftRotation(node.getLeft()));
			return rightRotation(node);
		}

		// right-left
		if ((balance < -1) && node.getRight().getKey().compareToIgnoreCase(key) > 0) {
			node.setRight(rightRotation(node.getRight()));
			return leftRotation(node);
		}

		return node;
	}

	// cancellazione

	public void delete(String k) throws ExceptionKeyNotFound{
		root = delete(root, k);
	}

	private AvlNode delete(AvlNode node, String key) throws ExceptionKeyNotFound {
		if (node == null)
			throw new ExceptionKeyNotFound();//return node;

		if (node.getKey().compareToIgnoreCase(key) < 0) { // go to the left recursively
			node.setLeft(delete(node.getLeft(), key));
		} else if (node.getKey().compareToIgnoreCase(key) > 0) { // go to the right recursively
			node.setRight(delete(node.getRight(), key));
		} else { // find node

			if (node.getLeft() == null && node.getRight() == null) {
				//remove leaf node
				return null;
			}

			if (node.getLeft() == null) {
				//remove the right child
				AvlNode tempNode = node.getRight();
				node = null;
				return tempNode;
			} else if (node.getRight() == null) {
				//remove the left child
				AvlNode tempNode = node.getLeft();
				node = null;
				return tempNode;
			}

			AvlNode tempNode = getPredecessor(node.getLeft());

			node.setKey(tempNode.getKey());
			node.setData(tempNode.getData());
			node.setLeft(delete(node.getLeft(), tempNode.getKey()));
		}

		node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);

		// have to check on every delete operation whether the tree has become
		// unbalanced or not !!!
		return checkBalanceAndRotate(node);
	}

	private AvlNode checkBalanceAndRotate(AvlNode node) {
		int balance = getBalance(node);

		// left heavy -> left-right heavy or left-left heavy
		if (balance > 1) {
			// if left-right: left rotation before right rotation
			if (getBalance(node.getLeft()) < 0) {
				node.setLeft(leftRotation(node.getLeft()));
			}

			// left-left
			return rightRotation(node);
		}

		// right heavy -> left-right heavy or right-right heavy
		if (balance < -1) {
			// if right-left: right rotation before left rotation
			if (getBalance(node.getRight()) > 0) {
				node.setRight(rightRotation(node.getRight()));
			}

			// right-right
			return leftRotation(node);
		}

		return node;
	}

	// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ R O T A Z I O N I $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	AvlNode rightRotation(AvlNode node) { // input C
		AvlNode newParentNode = node.getLeft(); // newParentNode = B
		AvlNode mid = newParentNode.getRight(); // store B's right node 'mid' (B < mid < C)

		newParentNode.setRight(node); // C now becomes right node of B
		node.setLeft(mid); // 'mid' now becomes left node of C

		node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(getHeight(newParentNode.getLeft()), getHeight(newParentNode.getRight())) + 1);

		return newParentNode; // return B as the parent of A and C
	}

	AvlNode leftRotation(AvlNode node) { // input A
		AvlNode newParentNode = node.getRight(); // newParentNode = B
		AvlNode mid = newParentNode.getLeft(); // store B's left node 'mid' (A < mid < B)

		newParentNode.setLeft(node); // A now becomes left node of B
		node.setRight(mid); // 'mid' now becomes right node of A

		node.setHeight(Math.max(getHeight(node.getLeft()), getHeight(node.getRight())) + 1);
		newParentNode.setHeight(Math.max(getHeight(newParentNode.getLeft()), getHeight(newParentNode.getRight())) + 1);

		return newParentNode; // return B as the parent of A and C
	}

	AvlNode leftRightRotation(AvlNode node) { // input C
		// leftRotation(A) -> result B, then set B as left node of C
		node.setLeft(leftRotation(node.getLeft()));

		// rightRotation(C)
		return rightRotation(node);
	}

	AvlNode rightLeftRotation(AvlNode node) { // input A
		// rightRotation(C) -> result B, then set B as right node of A
		node.setRight(rightRotation(node.getRight()));

		// leftRotation(A)
		return leftRotation(node);
	}

	// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ U T I L S $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	private int getHeight(AvlNode node) {

		if (node == null) {
			return -1;
		}

		return node.getHeight();
	}

	private AvlNode getPredecessor(AvlNode node) {

		AvlNode predecessor = node;

		while (predecessor.getRight() != null)
			predecessor = predecessor.getRight();

		return predecessor;
	}

	private int getBalance(AvlNode node) {
		if (node == null) {
			return 0;
		}
		return getHeight(node.getLeft()) - getHeight(node.getRight());
	}

	// TEST

	public void traverse() {
		if (root == null)
			return;

		System.out.println("traverse from node [" + root.getKey() + "]");
		inOrderTraversal(root);
	}

	private void inOrderTraversal(AvlNode node) {
		if (node.getLeft() != null)
			inOrderTraversal(node.getLeft());

		System.out.println(node);

		if (node.getRight() != null)
			inOrderTraversal(node.getRight());
	}

	public void printTree(){
		printTree(root);
	}

	private void printTree(AvlNode t){
		if(t != null){
			System.out.print("(");
			printTree(t.left);
			System.out.print(")");
			System.out.print(t.getKey() + " ");
			System.out.print("(");
			printTree(t.right);
			System.out.print(")");
		}
	}

//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ A R R A Y $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

	public V[] toArray(){
		int n = count();

		V[] arr = null;
		arr = (V[]) Array.newInstance(param, n);

		int a = 0;

		AvlNode u = root;
		int boh = inorder(u, arr, a);
		return arr;
	}

	private int inorder(AvlNode temp, V[] a, int i) {
	// base case
	if (temp == null) return i;

	// go to the right of tree
	i = inorder(temp.getRight(), a, i);


	// copy node to array
	a[i] =(V) temp.getData();
	i++;

	// go to the left of tree
	i = inorder(temp.getLeft(), a, i);
	return i;
	}

	public String[] toArrayKeys(){ 
		int n = count();

		String[] arr = new String[n];

		int a = 0;

		AvlNode u = root;
		inorderK(u, arr, a);
		return arr;
	}

	private int inorderK(AvlNode temp, String[] a, int i) {
	// base case
	if (temp == null) return i;

	// go to the right of tree
	i = inorderK(temp.getRight(), a, i);


	// copy node to array
	a[i] = temp.getKey();
	i++;

	// go to the left of tree
	i = inorderK(temp.getLeft(), a, i);
	return i;
	}

}

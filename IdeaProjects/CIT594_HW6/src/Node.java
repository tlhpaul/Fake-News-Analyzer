/**
 * Creates a node data structure with contents and a pointer to another node
 *
 * @param <T> is a generic that the node can take
 */
public class Node<T> {

    private Integer contents;
    private Node<Integer> next = null;

	/**
	 * Constructor for a node
	 *
	 * @param contents are the contents of a node
	 */
	public Node(Integer contents){
        this.contents  = contents;
    }

	/**
	 * Gets the contents of a node
	 *
	 * @return the contents of a node
	 */
	public Integer getContents() {
		return contents;
	}

	/**
	 * Gets the next node if there is one
	 *
	 * @return the next node
	 */
	public Node<Integer> getNext() {
		return next;
	}

	/**
	 * Sets the next node
	 *
	 * @param node that is set to be the next node
	 */
	public void setNext(Node<Integer> node) {
		this.next = node;
	}
}

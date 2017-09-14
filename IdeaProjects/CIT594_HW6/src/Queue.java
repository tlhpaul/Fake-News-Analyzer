/**
 * Creates a Queue that adds and removes nodes at opposite ends
 *
 * @param <T> is the generic type that Queues can take
 */
public class Queue<T> {

    private Node<Integer> head = null;
    private Integer size = 0;


    /**
     * Adds a node at the end of the Queue
     *
     * @param node is the node being added
     */
    public void add(Node<Integer> node){
        if (head == null) {
            head = node;
        } else {
        	Node<Integer> i = head;
        	while(i.getNext() != null) {
        		i = i.getNext();
        	}
        	i.setNext(node);
        }
        size++;
    }

    /**
     * Deletes the node that is the head
     *
     * @return the node being deleted
     */
    public Node<Integer> delete(){
        if (head == null) {
        	System.out.println("head is null");
            return null;
        }
        Node<Integer> firstNode = head;
        head = head.getNext();
        size--;
        return firstNode;
    }

    /**
     * Gets the node at an index
     *
     * @param index that the node is at
     * @return the node that exists at the index
     */
    public Node<Integer> indexAt(int index){
        int currentIndex = 0;
        Node<Integer> i = head;
        while(i != null && currentIndex < index){
            i = i.getNext();
            currentIndex ++;
        }
        if (i == null) {
        	System.out.println("i== null");
        }
        return i;
    }
    
    public Integer getSize() {
    	return size;
    }
}

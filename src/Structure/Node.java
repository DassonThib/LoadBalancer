package Structure;

/**
 * Created by Thibault on 01/06/2016.
 */
public class Node<T> {
    private T value;
    private Node nextElement;

    public Node(T v, Node n){
        this.value = v;
        this.nextElement = n;
    }

    public T getValue(){
        return this.value;
    }

    public Node getNext(){
        return this.nextElement;
    }

    public void setValue(T v){
        this.value = v;
    }

    public void setNextElement(Node n){
        this.nextElement = n;
    }
}

package Structure;

/**
 * Created by Thibault on 01/06/2016.
 */
public class WaitingList {
    private Node first;

    public WaitingList(Node f){
        this.first = f;
    }

    public void push(Node n){
        if(first != null){
            first.setNextElement(n);
        }
    }

    public Node remove(){
        if(first.getNext() != null){
            Node temp = first;
            first = first.getNext();
            temp.setNextElement(null);
            return temp;
        } else {
            return null;
        }
    }
}

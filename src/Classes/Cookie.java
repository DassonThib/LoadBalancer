package Classes;

/**
 * Created by Thibault on 30/05/2016.
 */
public class Cookie {
    private String name;
    private String value;

    String getName(){
        return this.name;
    }

    String getValue(){
        return this.value;
    }

    void setName(String n) {
        this.name = n;
    }

    void setValue(String v){
        this.value = v;
    }
}

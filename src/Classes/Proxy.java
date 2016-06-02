package Classes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 626 on 01/06/2016.
 */

public class Proxy {

    private List<String> serverList;
    private List<Integer> serverPort;
    int compteur = 0;

    public Proxy() throws IOException {
        serverList = new ArrayList<>();
        serverPort = new ArrayList<>();

    }


    public String getHotst(int position){
        return serverList.get(position);
    }
    public  Integer getPort(int position){
        return serverPort.get(position);
    }

    public void setServer(String host, Integer port){
        serverList.add(host);
        serverPort.add(port);
    }
    public int getPosition() {
        compteur++;
        if (compteur == serverList.size()+1) {
            compteur = 0;
            return compteur;
        }
        return compteur - 1;
    }
}

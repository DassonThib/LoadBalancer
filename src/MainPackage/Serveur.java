package MainPackage;

import java.io.*;
/**
 * Created by Thibault on 30/05/2016.
 */
public class Serveur {

    public static void main(String[] args) throws IOException{

        HttpMyServer server = new HttpMyServer(81);
        server.initProxy();
        server.run();
    }
}

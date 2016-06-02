package MainPackage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import Classes.HttpRequest;
import Classes.HttpResponse;
import Classes.HttpService;
import Classes.Proxy;
import Thread.ThreadPool;

/**
 * Created by Thibault on 01/06/2016.
 */
public class HttpMyServer {
    public int port ;
    ServerSocket socket = null;
    public Socket clientSocket;
    Proxy myProxy;


    public HttpMyServer() throws IOException {
       //
    }

    public HttpMyServer(int port) throws IOException {
        this.port = port;
    }

    public ThreadPool pool;

    public void initProxy() throws IOException {
        myProxy = new Proxy();
        try {
            Properties prop = new Properties();
            prop.load(new FileInputStream(new File("src/resources/conf.properties")));

            String[] vhostsNames = ((String) prop.getOrDefault("vhosts", "*")).split(",");

            for(String vhostName : vhostsNames) {
                vhostName = vhostName.trim();
                // Host information
                String hostname = (String) prop.getOrDefault(vhostName + ".path", "*");
                int portConf = Integer.valueOf(((String) prop.getOrDefault(vhostName + ".port", "80")));

                myProxy.setServer(hostname,new Integer(portConf));
            }
        } catch (Exception e) {
            System.out.println("Could not load configuration file " );
            e.printStackTrace();
        }
    }
    public void run() throws IOException{

        socket = new ServerSocket(port);
        System.out.println("Creating server socket on port " + port);
        pool = new ThreadPool(16);

        while(true){
            clientSocket = socket.accept();
            pool.submit(1, () -> {
                try {
                    startForward();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
       }
    }

    public void startForward() throws IOException {

        System.out.println("serveur sur le port   ==> " + this.port);
        System.out.println(Thread.currentThread().getName());
        int serverNumber = myProxy.getPosition();
        Socket forward = new Socket(myProxy.getHotst(serverNumber),myProxy.getPort(serverNumber));

        PrintWriter writer = new PrintWriter(forward.getOutputStream(),true);
        BufferedOutputStream writer2 = new BufferedOutputStream(clientSocket.getOutputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        BufferedInputStream br2 = new BufferedInputStream(forward.getInputStream());

        String str;

        while ((str = br.readLine()) != null )
        {
            writer.println(str);
            if (str.equals("") )
                break;
        }

        int i =0;
        byte[] buffer = new byte[4096];
        int bytesRead;
        while ((bytesRead = br2.read(buffer)) != -1) {
            writer2.write(buffer, 0, bytesRead);
        }
        /*while ((str = br2.readLine()) != null )
        {
            writer2.println(str);
            if (str.equals("") ) {
                i++;
                if(i >1 )
                break;
            }
        }*/
        writer2.close();
        forward.close();
        clientSocket.close();

    }

    public void handleRequest(Socket socket){
        HttpRequest request;
        HttpResponse response;
        System.out.println("serveur sur le port   ==> " + this.port);
        try{
            System.out.println(Thread.currentThread().getName());
            request = new HttpRequest(socket);
            response = new HttpResponse(socket);
            response.SetMethod(request.getMethod());
            String[] coukie = request.getCookiesNames();
            for(String aCookieName:coukie){
                response.setCookie(aCookieName, (String) request.getCookie(aCookieName));
            }
            new HttpService().service(request, response);
            socket.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}

package Classes;
import Interface.IHttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.WritableByteChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Thibault on 30/05/2016.
 */
public class HttpRequest implements IHttpRequest {

    private Socket socket;
    private Map<String, String> cookies;
    private Map<String, String> parameters;
    private Map<String, String> params;
    private String method;

    private String relativePath, absolutePath;
    private String name;
    private BufferedReader br;
    private boolean upload;


    public HttpRequest(Socket s) throws IOException{
        int i = 0;
        upload = false;
        this.cookies = new HashMap<>();
        this.parameters = new HashMap<>();
        this.params = new HashMap<>();
        this.socket = s;
        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String str = br.readLine();
        if(str != null) {
            String[] tab = str.split(" ");
            this.method = tab[0];
            //System.out.println(method);
            String val = tab[1];
            String[] vals = val.split("\\?");
            this.relativePath = vals[0];
            String fileToDownload = tab[tab.length-2];
            if( !fileToDownload.substring(fileToDownload.length()-1).equals("/") ) {
                if(!fileToDownload.contains(".ht")){
                    String fileSave = "";
                    int j;
                    for(j=fileToDownload.length()-1; j>=0;j--)
                    {
                        if(fileToDownload.charAt(j) == '/')
                            break;
                    }
                    fileSave = fileToDownload.substring(j+1,fileToDownload.length());
                    downloadFile( fileSave );
                }
            }

            String[] param;
            do {
                System.out.println(str);
                if ((param = str.split(":")).length > 1) {

                    this.parameters.put(param[0], param[1]);
                    //System.out.println(param[0]);
                    if (param[0].equals("Host")){
                        String[] path;
                        if((path = param[1].split("\\.")).length > 1){
                            this.name = path[1];
                        }
                    }
                    if (param[0].equals("Content-Type"))
                        i++;
                    if (param[0].equals("Content-Type") && param[1].contains("multipart/form-data")){
                        String[] tmp = param[1].split(";");
                        String [] tmp2 = tmp[1].split("=");
                        parameters.put(tmp2[0],tmp2[1]);
                        upload = true;
                    }
                    if (param[0].equals("Content-Disposition") && param[1].contains("form-data")){
                        String[] tmp = param[1].split(";");
                        String [] tmp2 = tmp[2].split("=");
                        parameters.put(tmp2[0],tmp2[1].replace("\"",""));
                    }
                    //System.out.println("Clé : " + param[0] + " ,Valeur : " + param[1]);
                }
                if (str.equals("") ) {

                    if (method.contains("POST") && i >= 2)
                        // if( (br.readLine() == null)) {
                        break;
                    if(method.contains("GET"))
                        break;
                }
                // }
            } while ((str = br.readLine()) != null );
            if(method.contains("POST") && i >1)
                doPost();
        }
    }

    @Override
    public Object getCookie(String name) {
        return this.cookies.get(name);
    }

    @Override
    public String[] getCookiesNames() {
        return new String[0];
    }

    @Override
    public Object getParameter(String name) {
        return this.parameters.get(name);
    }

    @Override
    public String[] getParametersNames() {
        String[] names = new String[this.parameters.size()];
        Iterator it = this.parameters.keySet().iterator();
        int i = 0;
        while(it.hasNext() != false){
            names[i] = (String) it.next();
            i++;
        }

        return names;
    }

    @Override
    public String getMethod() {
        return this.method;
    }

    @Override
    public String getRelativePath() {
        return this.relativePath;
    }

    @Override
    public String getAbsolutePath() {
        return "C:/www/"+name+this.relativePath;
    }

    public String getRootPath()
    {
        return "C:/www/"+name;
    }

    public void doPost(){
        String str = "";

        File yourFile = new File(getRootPath()+"\\files\\"+(String)getParameter(" filename"));
        if(!yourFile.exists()) {
            try {
                yourFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileOutputStream outputStream = null;
        try {
            outputStream =
                    new FileOutputStream(yourFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            br.readLine();
            while((str = br.readLine()) != null)
            {
                System.out.println(str);
                if (str.contains((String) getParameter(" boundary")))
                    break;
                str+= "\r\n";
                outputStream.write(str.getBytes());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static final int BUFFER_SIZE = 4096;

    public void downloadFile(String fileName) {
        downloadFile(fileName, getRootPath());
    }

    public void downloadFile(String fileName,String path) {
        InputStream inputStream = null;
        try {
            File file = new File(path+getRelativePath());
            if( file.isDirectory() || !file.exists() ) {
                System.out.println("No file to download.");
                return;
            }
            inputStream = new FileInputStream(file);
            String saveFilePath = getRootPath()+"\\dl\\" + fileName;
            File saved = new File(saveFilePath);
            saved.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(saved);
            int bytesRead = -1;
            byte[] buffer = new byte[BUFFER_SIZE];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            System.out.println("File downloaded");
        } catch (IOException ex) {
//            Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if( inputStream != null )
                    inputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(HttpRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
package Classes;

import Interface.IHttpRequest;
import Interface.IHttpResponse;
import Interface.IHttpService;

import javax.naming.Context;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by Thibault on 30/05/2016.
 */
public class HttpService implements IHttpService {
    @Override
    public void service(IHttpRequest request, IHttpResponse response) {
        String path = request.getAbsolutePath();
        //System.out.println("Absolute path : "+path);
        PrintWriter writer = response.getPrintWriter();
        StringBuilder sb = new StringBuilder();
        if(Files.exists(Paths.get(path))) {
            writer.println("HTTP/1.1 200");
            String[] param = request.getParametersNames();
            for(int i = 0; i<param.length; i++){
                writer.println(request.getParameter(param[i]));
            }

            //System.out.println(path);

            if (!Files.isDirectory(Paths.get(path))) {
                try {
//                    Files.lines(Paths.get(path)).forEach(writer::println);
                    FileInputStream input;
                    writer.println("content-type:" + Files.probeContentType(Paths.get(request.getAbsolutePath())));
                    writer.println("");
                    input = new FileInputStream(new File(request.getAbsolutePath()));
                    BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
                    BufferedInputStream reader = new BufferedInputStream(input);
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = reader.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                    reader.close();
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                writer.println("");
                File file = new File(path);
                //System.out.println(path);
                String requestedURI = path.substring(path.length());
                //File[] listOfFiles = folder.listFiles();
                writer.println("<html><body>");
                writer.print("<h1>Directory " + path + "</h1>");
                writer.print("<ul>");


                if(!path.equals(request.getRootPath()+"/"))
                    writer.print("<li><a href=\"" + requestedURI + "..\">..</a></li>");

                // Directory
                for(File f : file.listFiles(new FileFilter() {

                    public boolean accept(File f) {	return f.isDirectory(); }
                })) {
                    writer.print("<li><a href=\"" + requestedURI + f.getName() + "/\">" + f.getName() + "</a> (directory)</li>");
                }


                // Files
                for(File f : file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File f) {	return !f.isDirectory(); }
                })) {
                    writer.print("<li><a href=\"" + requestedURI + f.getName() + "\">" + f.getName() + "</a> (" + f.length() + " bytes)</li>");
                }
                writer.println("</ul></body></html>");
            }
        } else {
            writer.print("<html><body><h2>404 NOT FOUND</h2></body></html>");
        }
        writer.close();
    }
}

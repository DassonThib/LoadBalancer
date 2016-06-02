package Classes;
import Interface.IHttpResponse;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.stream.Stream;

/**
 * Created by Thibault on 30/05/2016.
 */
public class HttpResponse implements IHttpResponse{

    private String method;
    private Stream stream;
    private OutputStream ops;
    private Writer writer;
    private PrintWriter pw;
    private Cookie cookie;
    private Socket socket;

    public HttpResponse(Writer w, Stream s, String m, Cookie c){
        this.writer = w;
        this.stream = s;
        this.method = m;
        this.cookie = c;
    }

    public HttpResponse(Socket s) throws IOException{
        this.socket = s;
    }

    @Override
    public Writer getWriter() {
        return this.writer;
    }

    @Override
    public Stream getStream() {
        return this.stream;
    }

    @Override
    public PrintWriter getPrintWriter() {
        try {
            return new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public OutputStream getOutputStream() {
        return this.ops;
    }

    @Override
    public void SetMethod(String method) {
        this.method = method;
    }

    @Override
    public void setCookie(String name, String value) {
        this.cookie.setName(name);
        this.cookie.setValue(value);
    }
}

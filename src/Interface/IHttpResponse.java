package Interface;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.stream.Stream;

/**
 * Created by Thibault on 30/05/2016.
 */
public interface IHttpResponse {
    Writer getWriter();
    PrintWriter getPrintWriter();
    OutputStream getOutputStream();
    Stream getStream();
    void SetMethod(String method);
    void setCookie(String name, String value);
}

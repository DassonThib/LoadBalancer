package Interface;

/**
 * Created by Thibault on 30/05/2016.
 */
public interface IHttpRequest {
    Object getCookie(String name);
    String[] getCookiesNames();
    Object getParameter(String name);
    String[] getParametersNames();
    String getMethod();
    String getRelativePath();
    String getAbsolutePath();
    String getRootPath();
}

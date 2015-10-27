package webserver;

/**
 * Created by goznauk on 15. 10. 21..
 */
public interface POSTCallback {
    byte[] execute(String url, byte[] body) throws CallbackException;
}

package webserver;

/**
 * Created by goznauk on 15. 10. 27..
 */
public interface GETCallback {
    byte[] execute(String url) throws CallbackException;
}

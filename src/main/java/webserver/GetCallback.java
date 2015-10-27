package webserver;

/**
 * Created by goznauk on 15. 10. 27..
 */
public interface GetCallback {
    byte[] execute(String url) throws CallbackException;
}

package webserver;

import java.io.FileNotFoundException;

/**
 * Created by goznauk on 15. 10. 21..
 */
public interface Callback {
    byte[] execute(String url, byte[] body) throws CallbackException;
}

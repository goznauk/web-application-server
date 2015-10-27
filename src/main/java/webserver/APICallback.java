package webserver;

import java.io.FileNotFoundException;

/**
 * Created by goznauk on 15. 10. 21..
 */
public class APICallback implements Callback {
    @Override
    public byte[] execute(String url) throws FileNotFoundException {
        return new byte[0];
    }
}

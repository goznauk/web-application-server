package webserver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Created by goznauk on 15. 10. 21..
 */
public class FileCallback implements Callback {
    @Override
    public byte[] execute(String url) throws FileNotFoundException {
        byte[] body;

        File bodyFile = new File("./webapp" + url);

        if (bodyFile.exists()) {
            try {
                body = Files.readAllBytes(bodyFile.toPath());
                return body;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            throw new FileNotFoundException();
        }
        return null;
    }
}

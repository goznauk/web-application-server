package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;

/**
 * Created by goznauk on 15. 10. 21..
 */
public class Dispatcher {
    private static final Logger log = LoggerFactory.getLogger(Dispatcher.class);

    private static Dispatcher instance;
    private static HashMap<String, Callback> getHandleMap;
    private static HashMap<String, Callback> postHandleMap;


    private void setHandleMap() {
        // Routing Codes Here...
        get("/index.html", url -> getFile(url));

        post("/create", new POSTCallback() {
            @Override
            public byte[] execute(String url, byte[] body) throws CallbackException {
                return new byte[0];
            }
        });
    }



    private byte[] getFile(String url) {
        File bodyFile = new File("./webapp" + url);
        String response = "";

        if (bodyFile.exists()) {
            try {
                byte[] filebytes = Files.readAllBytes(bodyFile.toPath());
                response += response200Header(filebytes.length);
                response += responseBody(filebytes);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            byte[] notFound = "404 Not Found : No Page Exists".getBytes();
            response += response404Header(notFound.length);
            response += responseBody(notFound);
        }
        return response.getBytes();
    }

    private String response200Header(int lengthOfBodyContent) {
        String header = "";
        header += "HTTP/1.1 200 OK \r\n";
        header += "Content-Type: text/html;charset=utf-8\r\n";
        header += "Content-Length: " + lengthOfBodyContent + "\r\n";
        header += "\r\n";
        return header;
    }

    private String response404Header(int lengthOfBodyContent) {
        String header = "";
        header += "HTTP/1.1 404 Not Found \r\n";
        header += "Content-Type: text/html;charset=utf-8\r\n";
        header += "Content-Length: " + lengthOfBodyContent + "\r\n";
        header += "\r\n";
        return header;
    }

    private String responseBody(byte[] body) {
        return new String(body, 0, body.length);
    }










    private Dispatcher() {
        getHandleMap = new HashMap<>();
        postHandleMap = new HashMap<>();
        setHandleMap();
    }

    public static Dispatcher getInstance() {
        if(instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    public Callback getCallback(String method, String url) {
        switch (method) {
            case "GET":
                return getHandleMap.get(getRouteKey(url));
            case "POST":
                return postHandleMap.get(getRouteKey(url));
            default:
                return null;
        }
    }

    private String getRouteKey(String url) {
        // Parse url and return key for getHandleMap

        return "/index.html";
    }


    private void get(String routeKey, GETCallback getCallback) {
        getHandleMap.put(routeKey, (url, body) -> getCallback.execute(url));
    }

    private void post(String routeKey, POSTCallback postCallback) {
        postHandleMap.put(routeKey, (url, body) -> postCallback.execute(url, body));
    }


}

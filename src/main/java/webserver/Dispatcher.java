package webserver;

import java.util.HashMap;

/**
 * Created by goznauk on 15. 10. 21..
 */
public class Dispatcher {
    public static final int REQ_FILE = 1;
    public static final int REQ_API = 2;
    private static Dispatcher instance;
    private static HashMap<String, Callback> getHandleMap;
    private static HashMap<String, Callback> postHandleMap;

    private Dispatcher() {
        getHandleMap = new HashMap<>();
        postHandleMap = new HashMap<>();

        // Routing Codes Here...
        get("/index.html", new GetCallback() {
            @Override
            public byte[] execute(String url) throws CallbackException {
                return new byte[0];
            }
        });

        post("/create", new Callback() {
            @Override
            public byte[] execute(String url, byte[] body) throws CallbackException {
                return new byte[0];
            }
        });







    }

    public static Dispatcher getInstance() {
        if(instance == null) {
            instance = new Dispatcher();
        }
        return instance;
    }

    public Callback getCallback(String method, String url) {
        if(method.equals("GET")) {
            return getHandleMap.get(getRouteKey(url));
        } else if (method.equals("POST")) {
            return postHandleMap.get(getRouteKey(url));
        } else {
            return null;
        }
    }

    private String getRouteKey(String url) {
        // Parse url and return key for getHandleMap

        return "";
    }


    public void get(String routeKey, GetCallback getCallback) {
        getHandleMap.put(routeKey, (url, body) -> getCallback.execute(url));
    }

    public void post(String routeKey, Callback callback) {
        postHandleMap.put(routeKey, callback);
    }


}

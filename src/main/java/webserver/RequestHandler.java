package webserver;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RequestHandler extends Thread {
	private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
	
	private Socket connection;

    private Callback controller;

	public RequestHandler(Socket connectionSocket) {
		this.connection = connectionSocket;
	}

	public void run() {
		log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(), connection.getPort());
		
		try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
			// TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            ArrayList<String> requests = new ArrayList<>();
            for(String s = bufferedReader.readLine(); !s.equals(""); s = bufferedReader.readLine()) { requests.add(s); }

            String method = requests.get(0).split(" ")[0];
            String url = requests.get(0).split(" ")[1];

            // TODO : change test code
            byte[] requestBody = "userId=javajigi&password=password&name=%EB%B0%95%EC%9E%AC%EC%84%B1&email=javajigi%40slipp.net".getBytes();


            Dispatcher dispatcher = Dispatcher.getInstance();
            byte[] response = dispatcher.getCallback(method, url).execute(url, requestBody);

            DataOutputStream dos = new DataOutputStream(out);

            dos.write(response, 0, response.length);
            dos.writeBytes("\r\n");
            dos.flush();

		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (CallbackException e) {
            log.error(e.getMessage());
        }
    }
}

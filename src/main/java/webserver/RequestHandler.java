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
            byte[] requestBody = "body".getBytes();

            if(url.equals("create")) {
                controller = Dispatcher.getInstance().getCallback(Dispatcher.REQ_API);
            } else {
                controller = Dispatcher.getInstance().getCallback(Dispatcher.REQ_FILE);
            }

            try {
                controller.execute(url);
            } catch (FileNotFoundException e) {
                log.info(e.toString());
            }




            Dispatcher dispatcher = Dispatcher.getInstance();
            byte[] response = dispatcher.getCallback(method, url).execute(url, requestBody);

            DataOutputStream dos = new DataOutputStream(out);

            dos.write(response, 0, response.length);
            dos.writeBytes("\r\n");
            dos.flush();


            File bodyFile = new File("./webapp" + url);

            if (bodyFile.exists()) {
                byte[] body = Files.readAllBytes(bodyFile.toPath());
                response200Header(dos, body.length);
                responseBody(dos, body);
            } else {
                byte[] body = "404 Not Found\nPage Does Not Exist".getBytes();
                response404Header(dos, body.length);
                responseBody(dos, body);
            }
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (CallbackException e) {
            e.printStackTrace();
        }
    }

	private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
		try {
			dos.writeBytes("HTTP/1.1 200 OK \r\n");
			dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
			dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
			dos.writeBytes("\r\n");
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

    private void response404Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
	
	private void responseBody(DataOutputStream dos, byte[] body) {
		try {
			dos.write(body, 0, body.length);
			dos.writeBytes("\r\n");
			dos.flush();
		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}
}

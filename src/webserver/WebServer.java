package webserver;

import in2011.http.RequestMessage;
import in2011.http.ResponseMessage;
import in2011.http.StatusCodes;
import in2011.http.EmptyMessageException;
import in2011.http.MessageFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.*;
import java.util.Date;
import org.apache.http.client.utils.DateUtils;

public class WebServer {

    private int port;
    private String rootDir;
    private boolean logging;
    private ServerSocket serverSocket;

    public WebServer(int port, String rootDir, boolean logging) {
        this.port = port;
        this.rootDir = rootDir;
        this.logging = logging;
        
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        while(true){
            //listen for a new connection on the socket
            Socket connection = serverSocket.accept();
            //process an HTTP request over the new connection
            //Data sent over OutputStream
            OutputStream outputS = connection.getOutputStream();
            //send the response
            ResponseMessage message = new ResponseMessage(200);
            message.write(outputS);
            outputS.write(" A rote message ".getBytes());
           
           connection.close();
        }
}

    public static void main(String[] args) throws IOException {
        String usage = "Usage: java webserver.WebServer 1091 /Users/alessandrobisiani/Desktop/webserver (\"0\" | \"1\")";
        if (args.length != 3) {
            throw new Error(usage);
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new Error(usage + "\n" + "<port-number> must be an integer");
        }
        String rootDir = args[1];
        boolean logging;
        if (args[2].equals("0")) {
            logging = false;
        } else if (args[2].equals("1")) {
            logging = true;
        } else {
            throw new Error(usage);
        }
        WebServer server = new WebServer(port, rootDir, logging);
        server.start();
    }
}

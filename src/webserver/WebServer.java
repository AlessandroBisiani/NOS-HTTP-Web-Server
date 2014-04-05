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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.client.utils.DateUtils;

public class WebServer {

    private int port;
    private String rootDir;
    private boolean logging;
    private ServerSocket serverSocket;
    private InputStream inputStream;
    private static long requestID;

    
    
    
   
    public WebServer(int port, String rootDir, boolean logging) {
        this.port = port;
        this.rootDir = rootDir;
        this.logging = logging;
    }

    
    
    
    /** NOTES
     * The ConnectionInstance class (which really needs a better fucking name) holds the http request handler code.
     * The infinite while loop waits for something to try and connect on port then .accept() the connection and puts 
     *  in connection field.
     * connection is passed to ConnectionInstance which deals which the content of that connection.
     */
    
    public void start() throws IOException {
        
        
        serverSocket = new ServerSocket(port);
        
        
        while(true){
            //listen for a new connection on the socket
            Socket connection = serverSocket.accept();
            new ConnectionInstance(connection, rootDir).run();
        }
        
        
        
            //process an HTTP request over the new connection
            //data sent from the client
            /*
            inputStream = connection.getInputStream();
            try {
                RequestMessage requestMsg = RequestMessage.parse(inputStream);
                if(requestMsg.getMethod().equals("GET")){
                    System.out.println("halp! I'm a tast!");
                    OutputStream outputS = connection.getOutputStream();
                    ResponseMessage message = new ResponseMessage(200);
                    message.write(outputS);
                    outputS.write(" Aw yeah. ".getBytes());
                } else {
                    OutputStream outputS = connection.getOutputStream();
                    ResponseMessage message = new ResponseMessage(200);
                    message.write(outputS);
                    outputS.write(" not implemented ".getBytes());
                }
                } catch (MessageFormatException ex) {
                Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
                }
            /*
            //Data sent over OutputStream
            OutputStream outputS = connection.getOutputStream();
            //send the response
            ResponseMessage message = new ResponseMessage(200);
            message.write(outputS);
            outputS.write(" Ok ".getBytes());
            */

            //connection.close();
    }  

    
    public static void main(String[] args) throws IOException {
        String usage = "Usage: java webserver.WebServer 1091 C:\\WebServer (\"0\" | \"1\")";
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webserver;

import in2011.http.MessageFormatException;
import in2011.http.RequestMessage;
import in2011.http.ResponseMessage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Alessandro
 */
public class ConnectionInstance implements Runnable{

    private Socket connection;
    private String rootDir;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    public ConnectionInstance(Socket conn, String rootdir ){
        connection = conn;
        rootDir = rootdir;
    };
    
    
    @Override
    public void run() {
        
        try {
            inputStream = connection.getInputStream();
            outputStream = connection.getOutputStream();
            RequestMessage requestMsg = RequestMessage.parse(inputStream);
            
            if(requestMsg.getMethod().equals("GET") || requestMsg.getMethod().equals("HEAD")){
                //System.out.println("halp! I'm a tast!");
                ResponseMessage message = new ResponseMessage(200);
                message.write(outputStream);
                outputStream.write(" Aw yeah. ".getBytes());
            } else {
                OutputStream outputS = connection.getOutputStream();
                ResponseMessage message = new ResponseMessage(200);
                message.write(outputS);
                outputS.write(" not implemented ".getBytes());
            }
        } catch (MessageFormatException ex) {
            Logger.getLogger(WebServer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ConnectionInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(ConnectionInstance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //readfile()
    //writeFile()
    //fileExists()
   
}

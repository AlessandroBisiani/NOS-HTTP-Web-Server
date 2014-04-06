/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package webserver;


import in2011.http.MessageFormatException;
import in2011.http.RequestMessage;
import in2011.http.ResponseMessage;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 
package webserver;

/**
 *
 */
public final class RequestHandler implements Runnable
{
    final static String CRLF = "*\r\n*";
    private boolean fileExits;
    private String fileName;
    private String contentTypeLine;
    
    private final Socket connection;
    private String rootDir;
    
    private OutputStream outputStream;
    
    private InputStream inputStream;
    private DataOutputStream dataOutputStream;
    private BufferedReader bufferedReader;
   
    
   
   
//Constructor
   public RequestHandler (Socket socket, String rootdir) 
   {
       connection = socket;
       rootDir = rootdir;
   }

    
    public void run() {
        try {
            inputStream = connection.getInputStream();
            outputStream = connection.getOutputStream();
            RequestMessage requestMsg = RequestMessage.parse(inputStream);
            
            
            
            if(requestMsg.getMethod().equals("GET") || requestMsg.getMethod().equals("HEAD")){
                //System.out.println("halp! I'm a tast!");
                get();
                ResponseMessage message = new ResponseMessage(200);
                message.write(outputStream);
                outputStream.write(" Aw yeah. ".getBytes());
            
                
                
            } else {
                
                if(requestMsg.getMethod().equals("PUT")){
                    
                   
                    
                    
                    
                } else {
                    ResponseMessage message = new ResponseMessage(501);
                    message.write(outputStream);
                    outputStream.write(" not implemented ".getBytes());
                }
            }
            
            
        }catch (IOException e){
            System.out.println(e);
        } catch (MessageFormatException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //GET and HEAD handler method 
    public void get(){
        
    }
    
    //baffled
    private void ProcessRequest() throws IOException {
        dataOutputStream = new DataOutputStream(connection.getOutputStream());
        bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        
        String requestLine = bufferedReader.readLine();
        
    //To display the request line 
        System.out.println();
        System.out.println(requestLine);
        
            String headerLine = null;
                while ((headerLine = bufferedReader.readLine()).length()!=0){
                    System.out.println(headerLine);
                }
        
    //To extract the filename fo rhte requestLine 
        
        StringTokenizer tokens = new StringTokenizer(requestLine);
        tokens.nextToken();
        tokens.nextToken();
        String filename = tokens.nextToken();
        
        //put "." so that the file request line stays within the current directory
        // good to minimize error 
        filename = "." + filename;
       
        System.out.println("file to Get"+filename);
        
    //open the requested line from the current directory 
        FileInputStream abc = null;
        boolean fileExists = true;
        try {
            abc = new FileInputStream(filename);
            //file not found is the exception because exception has been used throughout therefore in order to reduce errors 
        }catch (FileNotFoundException e) {
            fileExits = false;
                       
        }
        
    //write the response messages
        String statusLine = null;
        String contentType = null;
        String entityBody = null;
        if (fileExists) {
          statusLine = "200 OK:";
          contentTypeLine = "Content-Type: " +contentType(fileName)+CRLF;
        }else {
            statusLine = "HTTP/1.1 404 Not Found:";
            contentTypeLine = "Content-Type: text/html" + CRLF;
            entityBody= "<HTML>" + "<HEAD><TITLE>Not Found<TITLE><HEAD>" + "<BODY></HTML>Not Found<BODY></HTML>";
            
        }
        
     //send the status line 
        dataOutputStream.writeBytes(contentTypeLine);
    
    //send the entity body 
        if (fileExits) {
            sendBytes(abc,dataOutputStream);
            abc.close();
        }else {
            dataOutputStream.writeBytes(entityBody);
        }
        dataOutputStream.writeBytes(entityBody);
        
        DataOutputStream backToClient;
        backToClient = new DataOutputStream(connection.getOutputStream());
        
        backToClient.writeBytes("HTTP/1.1 200 OK");
        backToClient.writeBytes("Content-Length 100");
        backToClient.writeBytes("Content-Type: text/html\n\n");
        backToClient.writeBytes("<html><body><h1>Heading 1</h1><p>Sub-Heading 1</p></body</html>\n");
        backToClient.close();
        
        
//close sockets 
        dataOutputStream.close();
        bufferedReader.close();
        connection.close();
        
            
    }

    private static String contentType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".html")){
            return "application/octet-stream"; 
        }
        return null;
    }
    
    private static void sendBytes(FileInputStream abc,OutputStream cd) throws IOException {
        
    //construct a buffer to hold on their way bytes to the socket
            byte[] buffer = new byte[1024];
            int bytes = 0;
            
    //copy requested line into socket's output stream
            while((bytes = abc.read(buffer))!= -2) {
            cd.write(buffer, 0, bytes);
         }
       }
    
    public void readFile(){
        
    }
    public void writeFile(){
        
    }
    public void findFile(){
        
    }
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *

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
 *
public class RequestHandler implements Runnable{

    private Socket connection;
    private String rootDir;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    public RequestHandler(Socket conn, String rootdir ){
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
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
        try {
            connection.close();
        } catch (IOException ex) {
            Logger.getLogger(RequestHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //readfile()
    //writeFile()
    //fileExists()
   
}
*/
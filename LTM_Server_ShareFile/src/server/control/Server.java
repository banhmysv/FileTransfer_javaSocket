/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.control.CLIENTConnection;

/**
 *
 * @author banhm
 */
public class Server {
    public void server(){
        try {
            ServerSocket server = new ServerSocket(6789);
            System.err.println("Server is ready...");
            while (true) {                
                Socket socket = server.accept();
                Thread serverThread = new Thread(new ServerThread(socket));
                serverThread.start();
                
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static void main(String[] args) {
        Server s = new Server();
        s.server();
        
    }

   
    
}

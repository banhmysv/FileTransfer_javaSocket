/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client.control;

import client.view.JfrClient;
import java.awt.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import object.Login;

/**
 *
 * @author banhm
 */
public class Connect {

    private static Socket socket;
    private static ObjectOutputStream oos;
    private static ObjectInputStream ois;
    private static InputStream is;
    private static FileOutputStream fos;
    private static BufferedOutputStream bos;
    private BufferedReader br;
    private PrintStream ps;
    

    public void sendFile(String fileName) {
        try {

            System.err.print("Enter file name: ");

            File myFile = new File(fileName);
            byte[] mybytearray = new byte[(int) myFile.length()];

            FileInputStream fis = new FileInputStream(myFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            //bis.read(mybytearray, 0, mybytearray.length);

            DataInputStream dis = new DataInputStream(bis);
            dis.readFully(mybytearray, 0, mybytearray.length);

            OutputStream oss = socket.getOutputStream();

            //Sending file name and file size to the server
            DataOutputStream dos = new DataOutputStream(ps);
            dos.writeUTF(myFile.getName());
            dos.writeLong(mybytearray.length);
            dos.write(mybytearray, 0, mybytearray.length);
            dos.flush();
            System.out.println("File " + fileName + " sent to Server.");
        } catch (IOException e) {
            System.err.println("File does not exist!");
        }
    }

    public void receiveFile(String fileName) {

        try {

            int bytesRead;
            is = socket.getInputStream();

            DataInputStream clientData = new DataInputStream(is);

            fileName = clientData.readUTF();
            OutputStream output = new FileOutputStream(("received_from_client_" + fileName));//received_from_client_a2
            long size = clientData.readLong();
            byte[] buffer = new byte[1024];
            while (size > 0 && (bytesRead = clientData.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesRead);
                size -= bytesRead;
            }

            output.close();
            is.close();

            System.out.println("File " + fileName + " received from Server.");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void showFileFormServer() {

        try {
            ois = new ObjectInputStream(socket.getInputStream());
            String a = (String) ois.readObject();
            System.out.println(a);

           
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }

    }


    public String login(String account, String password) {
       
        try {
            // sending
            oos = new ObjectOutputStream(socket.getOutputStream());
            Login login = new Login(account, password);
            oos.writeObject(login);
            //receive
            System.err.println("day1");
            ois = new ObjectInputStream(socket.getInputStream());
            Object o = ois.readObject();
            if (o instanceof String) {
                String result = (String) o;
                if (result.equals("true")) {
                    JOptionPane.showMessageDialog(null, "Đăng nhập thành công", "Notification", 1);
                    

                } else {
                    JOptionPane.showMessageDialog(null, "Tài khoản hoặc mật khẩu không tồn tại !!!", "Notification", 1);
                }

            }

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    public static void main(String[] args) {
        try {
            socket = new Socket("localhost", 6789);
            JfrClient jC= new JfrClient();
            jC.setVisible(true);
            
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}

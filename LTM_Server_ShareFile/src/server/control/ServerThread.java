/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import object.Login;

/**
 *
 * @author banhm
 */
public class ServerThread implements Runnable {

    private Socket socket;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;
    private Connection conn;
    private PreparedStatement pst;
    private ResultSet rs;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    public void showFileToClient() {
        try {

            String sql = "Select *From dbo.tbFile";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            String[] arr;
            String str = "" ;
            while (rs.next()) {
                String fileName = rs.getString(3);
                arr = fileName.split("\n");

                for (String arr1 : arr) {
                    str = str + arr1 + ";";
                }
            }
            System.out.println(str);
            oos = new ObjectOutputStream(socket.getOutputStream());

            oos.writeObject(str);

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean login() {
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            Login login = (Login) ois.readObject();
            if (login != null) {
                System.out.println("Acc: " + login.getAccount() + "\nPass: " + login.getPassword());
                conn = ConnectData.getConnect();
                // kiem tra dang nhap
                String sql = "SELECT *FROM tbUser where Account=? and PassW=?";
                pst = conn.prepareStatement(sql);
                pst.setString(1, login.getAccount());
                pst.setString(2, login.getPassword());
                rs = pst.executeQuery();
                System.err.println("1");
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public void checkLogin() {
        boolean check = login();
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
            if (check == true) {
                oos.writeObject("true");
                System.out.println("đên đây");
                showFileToClient();
            } else {
                oos.writeObject("false");
            }

        } catch (IOException ex) {
            Logger.getLogger(ServerThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        checkLogin();
    }

}

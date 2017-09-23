/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author banhm
 */
public class ConnectData {
    
    public static Connection getConnect() {
        Connection conn;
        try {
            conn = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QuanLyKhoHang", "sa", "admin");
            return conn;
        } catch (SQLException e) {

            JOptionPane.showMessageDialog(null, "Kết nối cơ sở dữ liệu thất bại", "Thông báo", 1);
            return conn = null;
        }

    }
}

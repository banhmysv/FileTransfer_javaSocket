/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.view;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.proteanit.sql.*;
import object.ObjFile;
import server.control.ConnectData;

/**
 *
 * @author banhmysv
 */
public class UpdateTable {

    public static PreparedStatement pst = null;// biến thực thi sql
    public static ResultSet rs = null;//kết quả trả vê dạng 1 bảng hay 1 dòng chữ liệu
    public static Connection conn = ConnectData.getConnect(); //lấy từ lớp connect cơ bản kết nối đa ta

    //viết 1 ham nạp dữ liêu cho bảng
    public static void LoadData(String sql, JTable tb) {
        try {
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();
            tb.setModel((DbUtils.resultSetToTableModel(rs)));
            //ngay tai day là nạp dữ liệu lên
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e, "Notification error", 1);
        }
    }

    // tiếp theo viết q hàm đổ dữ liệu lên textfield
    public static ResultSet ShowTextField(String sql) {
        try {
            pst = conn.prepareStatement(sql);
            return pst.executeQuery();
            //trả vê 1 dòng dữ liệu lên txtfield
        } catch (SQLException e) {
            return null;

            //JOptionPane.showMessageDialog(null, e,"Notification error",1);
            //với java tất cả mọi thứ đều nằm try catch, ae lưu ý
        }
    }
}

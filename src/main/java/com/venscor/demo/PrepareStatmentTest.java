package com.venscor.demo;

import java.sql.*;

public class PrepareStatmentTest {

    public static void main(String[] args) {
        Connection con= null;
        try {
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/demo", "root", "nipc1404");
            PreparedStatement pstmt=con.prepareStatement("select  name from school where  name like ?");

            pstmt.setString(1,"%xd");

            ResultSet set = pstmt.executeQuery();

            while (set.next()){
                String a = set.getString(1);

                System.out.printf("");

            }
            System.out.printf("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

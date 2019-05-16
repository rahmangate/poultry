package poultry.management.system;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.File;
import java.sql.DriverManager;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author rahmantk
 */
public class Connect {
   static Connection con=ConnectDB();
   static PreparedStatement pst;
   static Statement stmt;
   static ResultSet rs;
   
    
//    public static Connection ConnectDB(){
//        try{
//         Class.forName("com.mysql.jdbc.Driver");
//         Connection  con=DriverManager.getConnection("jdbc:mysql://localhost:3306/attendance","root","");
//         return con;
//        }catch(Exception ex){
//             JOptionPane.showMessageDialog(null,"can not connect to database. try again "+ex.getMessage());
//            return null;
//        }
//}
    
    
       public static Connection ConnectDB(){
        try{
             Class.forName("org.sqlite.JDBC");
             String path=File.listRoots()[0].getAbsolutePath(),
                     sp=File.separator;

             Connection  con=DriverManager.getConnection("jdbc:sqlite:"+path+sp+"PoultryManagementSystem"+sp+"pms.db");
            //System.out.print(con.getClass());
             return con;
        }catch(Exception ex){
             JOptionPane.showMessageDialog(null,"can not connect to database. try again \n"+ex.getMessage());
            return null;
        }
    }
    
}

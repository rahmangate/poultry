/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poultry.management.system;

import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.TableModel;
import net.proteanit.sql.DbUtils;
import org.jdesktop.swingx.JXDatePicker;
import static poultry.management.system.Connect.con;



/**
 *
 * @author rahman
 */
public class Global extends Connect{
    
    
   
    
    
    static String signinpaswd="signinpaswd",
           user_id="",
           user_name="",
           user_role="";
    
    
    
    
    static void login(String role,String name ,String paswd ){
         try {
                String r="",n="",p="",
                        sql="select id, username, password ,role from users where username='"+name+"'  and role ='"+name+"' and password ='"+name+"' ";
                stmt=con.createStatement();
                rs=stmt.executeQuery(sql);
                while(rs.next()){
                    r=rs.getString("role");
                    n=rs.getString("username");
                    p=rs.getString("password");
                }
          //JOptionPane.showMessageDialog(null, msg);
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
     static void saveData(String sql ,String[] v){
         try { 
               // con=ConnectDB();
                pst=con.prepareStatement(sql);
                for(int i=0;i<v.length;i++){
                    pst.setString(1+i, v[i]);
                }
                pst.execute();
               // con.close();    
          //JOptionPane.showMessageDialog(null, msg);
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }

    static void addHouse(JTextField name){
        
         String sql="insert or replace into houses(NAME) values(?)";
         saveData(sql,new String[] {name.getText()});
         name.setText("");
    }
    
    static void addPen(JTextField name ,JComboBox house){
         try { 
                String  sql="insert or replace into pens(NAME,HOUSE) values(?,?) ",
                values[]={name.getText(),house.getSelectedItem().toString()};
                saveData(sql ,values);
                name.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
     static void addCustomer(JTextField name,JTextField phone,JTextArea address){
         try { 
                String  sql="insert or replace into customers(NAME,PHONE ,ADDRESS) values(?,?,?) ",
                values[] ={name.getText(),phone.getText(),address.getText()};
                saveData(sql ,values);
                name.setText("");phone.setText("");address.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
     
     static void addEmployee(JTextField fname,JTextField lname,JXDatePicker dob,JComboBox gender,JTextField phone,JTextArea address, JTextField job){
         try { 
                String  sql="insert or replace into employees(FIRSTNAME,LASTNAME,DATE_OF_BIRTH,GENDER,PHONE,ADDRESS,JOB_DESCRIPTION) values(?,?,?,?,?,?,?) ",
                values[] ={
                    fname.getText(),lname.getText(),
                    dob.getDate().toString(),(String)gender.getSelectedItem(),
                    phone.getText(),address.getText(),job.getText()
                };
                saveData(sql ,values);
                fname.setText("");lname.setText("");phone.setText("");address.setText("");job.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
     
      static void addNewBirds(JComboBox pen,JXDatePicker date,JTextField base,JXDatePicker dateHatched, JTextField breed,JTextField opstock){
         try { 
                String  sql="insert  into birds(PEN,DATE_,BASE,DATE_HATCHED,BREED,OPENING_STOCK) values(?,?,?,?,?,?) ",
                        sql2=" update pens set STOCK=(Select stock + ?  from pens  where NAME=? )  where NAME=?  ",
                values[] ={
                    pen.getSelectedItem().toString(),
                    formatDate(date.getDate().toInstant().toString()),//(String)date.getDate().toString(),
                    base.getText(),
                    dateHatched.getDate().toString(),
                    breed.getText(),opstock.getText(),  
                };
                saveData(sql ,values);
                saveData(sql2 ,new String[]{opstock.getText(),pen.getSelectedItem().toString(),pen.getSelectedItem().toString()});
               // base.setText("");breed.setText("");
                pen.setSelectedItem("");
                opstock.setText("");
                
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
    static void addFLock(JComboBox cmbPen,JTextField txtAge,JTextField txtStock,JTextField txtDeath,JTextField txtCulls,JTextField txtPicking,JTextField txtFeed,JTextField txtMedication){
         try { 
           
                String pen=cmbPen.getSelectedItem().toString(),
                        sql="insert  into flocks(PEN, AGE , STOCK ,DEATH ,CULLS,PICKING,FEED ,MEDICATION) values(?,?,?,?,?,?,?,?) ",
                       
                values[] ={
                    pen,txtAge.getText(),txtStock.getText(),txtDeath.getText(),
                    txtCulls.getText(),txtPicking.getText(),txtFeed.getText(),txtMedication.getText()   
                };

                 Integer culls_pickings=Integer.parseInt(txtCulls.getText()) +Integer.parseInt(txtPicking.getText()),
                         currentStock=Integer.parseInt(txtStock.getText()) - (Integer.parseInt(txtDeath.getText()) + culls_pickings );
                
                 String  updateValues[]={currentStock.toString(),pen},
                       /*update stock */
                        sqlupdateStock=" update pens set STOCK=?  where NAME=?  ",
                        sqlCullsPickings=" update pens set STOCK=(Select stock + ?  from pens  where NAME='Culls & Picking' )  where NAME='Culls & Picking' ";
            
              saveData(sql ,values);
              saveData(sqlupdateStock ,updateValues);
              saveData(sqlCullsPickings ,new String[]{culls_pickings.toString()});
              
             txtStock.setText(currentStock.toString());txtDeath.setText("0"); txtCulls.setText("0");txtPicking.setText("0");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
      
      static void addProduction(String flock_id,JTextField time,JTextField qty){
         try { 
                String  sql="insert  into productions(FLOCK_ID, TIME ,QUANTITY) values(?,?,?) ",
                values[] ={
                    flock_id,time.getText(),qty.getText()  
                };
                saveData(sql ,values);
                qty.setText("0");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
      
    static void addSale(JComboBox customer,JLabel gross,JTextField discount,JTextField discount_p ,JLabel net,JTable cart){
        String s_id="";
         try { 
                String  sql="insert into sales(CUSTOMER,GROSS,DISCOUNT,PERCENT,NET) values(?,?,?,?,?) ",
                values[] ={
                    customer.getSelectedItem().toString() ,gross.getText(),  discount.getText(),discount_p.getText(),net.getText()
                };
                saveData(sql ,values);
                //select the last id from sales
                //con=ConnectDB();
                stmt=con.createStatement();
                rs=stmt.executeQuery("select id from sales order by id DESC limit 1");
                while(rs.next()){
                    s_id=rs.getString("id"); 
                }
                if(s_id.isEmpty())return ;
                //add sale items
                int rc=cart.getRowCount();
                 for(int i=0;i<rc;i++){
                     addSaleItem(s_id,
                             cart.getValueAt(i, 0).toString() ,
                             cart.getValueAt(i, 1).toString() ,
                             cart.getValueAt(i, 2).toString() ,
                             cart.getValueAt(i, 3).toString() 
                     );
                 }
                 //update stock
//                 for(   String k : stocksUpdate.keySet() ){
//                    String v=stocksUpdate.get(k);
//                    saveData("update stocks set quantity=? where description=? ",new String[]{v,k});
//                 }
                 
                 new Invoice(s_id).setVisible(true);
              //  return true;
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
        // return false;
    }
    
    static void addSaleItem(String s_id,String... s){
         try { 
                String  sql="insert  into sale_items(SALE_ID,DESCRIPTION,QUANTITY,RATE,AMOUNT) values(?,?,?,?,?) ",
                values[] ={
                    s_id, s[0],  s[1],s[2],s[3]
                };
                saveData(sql ,values);
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }

    static void addPayment(JTextField sale_id,JTextArea note,JTextField amount,String balance){
         try { 
                Double amt=Double.parseDouble(amount.getText()),
                        bal=Double.parseDouble(balance);
                String  sql="insert  into payments(SALE_ID, NOTE ,AMOUNT, BALANCE) values(?,?,?,?)",
                values[] ={
                    sale_id.getText(),note.getText(), String.format("%.2f",amt ) , String.format("%.2f",bal)
                };
                saveData(sql ,values);
                sale_id.setText("");note.setText("");amount.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    static String  getBalance(String saleid){
        String ans="0.00";
        try {
            String sql="select NET, NET - ( select sum(amount)   from payments where SALE_ID=? ) as BALANCE from sales where id=? ";
           // con=ConnectDB();
            pst=con.prepareStatement(sql);
            pst.setObject(1, saleid);
            pst.setObject(2, saleid);
            rs=pst.executeQuery();
            while(rs.next()){
                String b=rs.getString("BALANCE");
                if(b==null){
                    b=rs.getString("NET");
                }
                //con.close();
                //System.out.print(b+" balance \n");
                return b;   
            } 
           // con.close();
            return ans;
       }catch(Exception ex){} 
        return  ans;
    }
    
    static void createStockItem(JTextField description){
         try { 
                String  sql="insert  into stocks(DESCRIPTION,QUANTITY,RATE) values(?,?,?) ",
                values[] ={
                    description.getText(), "0","0.00"
                };
                saveData(sql ,values);
                description.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
    static void addNewStock(JComboBox description,JTextField quantity ,JTextField rate){
         try { 
                String descrip=description.getSelectedItem().toString(),
                        sql="insert or replace  into stocks(DESCRIPTION,QUANTITY,RATE)"
                        + " select ? ,(SELECT QUANTITY + ?  FROM STOCKS  WHERE DESCRIPTION = '"+descrip+"' ), ? ",
                        
                        sql2="insert  into stock_entries(DESCRIPTION,QUANTITY,RATE) values(?,?,?) ",
                values[] ={
                    descrip,  quantity.getText(),rate.getText()
                };
                saveData(sql ,values);
                //keep records in stock_entriees
                saveData(sql2 ,values);
                description.getSelectedItem().toString(); quantity.setText("");
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
    static void changeQtyOrRate(int i,String description,JTextField quantityOrRate ){
        //true for rate else quantity
         try { 
                String  sql="";
                String values[] ={};
                
                switch(i){
                    case 0: 
                        //change rate
                        sql="update stocks set rate=? where description=? ";
                        values =new String[]{quantityOrRate.getText(),description};
                        break;
                    case 1:
                        //change quantity
                        sql="update stocks set quantity=? where description=?";
                        values =new String[]{quantityOrRate.getText(),description};
                        break;
                    case 2:
                        //add more quantity
                        sql="UPDATE STOCKS SET QUANTITY =(SELECT SUM (QUANTITY)+ ?  FROM STOCKS  WHERE DESCRIPTION =? ) WHERE DESCRIPTION =? ";
                         values = new String[]{quantityOrRate.getText(),description,description};
                        break;
                }
                saveData(sql ,values);
      }catch(Exception  ex){
        JOptionPane.showMessageDialog(null, ex.getMessage());
      }
    }
    
    
    
    static String formatDate(String str){
        try{
            DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");       
             Date date = parseFormat.parse(str.replace("T", " "));
             return parseFormat.format(date);
        }catch(Exception err){
            System.out.print(err);
        } 
        return "";
    }
   
    static String formatDate2(String str){
        try{
            DateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd");       
             Date date = parseFormat.parse(str.replace("T", " "));
             return parseFormat.format(date)+" 23:59:00 ";
        }catch(Exception err){
            System.out.print(err);
        } 
        return "";
    }
    
    static void getTableData(String sql,JTable jtable){
        HashMap<String,String> param=new HashMap<>();
       getTableData(sql,param,jtable);
    }
    
    static void getTableData(String sql,HashMap<String,String> param,JTable jtable){
        try {
            //con=ConnectDB();
            pst=con.prepareStatement(sql);
            int i=1;
            for(Object k : param.keySet()){
                pst.setObject(i, param.get(k));
                i+=1;
            }
            rs=pst.executeQuery();
            jtable.setModel(DbUtils.resultSetToTableModel(rs));
           // con.close();
       }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex);
       } 
    }
    
    static void getData(String sql,JComboBox cmb,String ColumnName){
        try {
           // con=ConnectDB();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            while(rs.next()){
                cmb.addItem(rs.getObject(ColumnName));
            }
            //con.close();
       }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex);
       } 
    }
    

     static void getItem(JComboBox cmb,JTextField qty, JTextField  rate){
        try {
            String sql="select QUANTITY , RATE from stocks where DESCRIPTION=? ";
            //con=ConnectDB();
            pst=con.prepareStatement(sql);
            pst.setString(1, cmb.getSelectedItem().toString());
            rs=pst.executeQuery();
            while(rs.next()){
               // cmb.addItem(rs.getObject(ColumnName));
                qty.setText(rs.getObject("QUANTITY").toString());
                rate.setText(rs.getObject("RATE").toString());
            }
           // con.close();
       }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex);
       } 
    }
     
    static void getAllItems(HashMap<String,List<String>> stocks){
        try {
            String sql="select DESCRIPTION, QUANTITY , RATE from stocks  ";
            //con=ConnectDB();
            pst=con.prepareStatement(sql);
            rs=pst.executeQuery();
            String des="",qty="",rate="";
            List l=new ArrayList<>();
            while(rs.next()){
                des=rs.getObject("DESCRIPTION").toString();
                qty=rs.getObject("QUANTITY").toString();
                rate=rs.getObject("RATE").toString();
                l.add(qty);
                l.add(rate);
                stocks.put(des,l);  
            }
           // con.close();
       }catch(Exception ex){
        JOptionPane.showMessageDialog(null, ex);
       } 
    }
     
     static String getClosingStock(JComboBox cmbPen){
         return  getClosingStock(cmbPen.getSelectedItem().toString());
     }
     
      static String getClosingStock(String pen){
        try {
            String stock="0",
                    sql="select STOCK from pens  where NAME= ?   ";
            //con=ConnectDB();
            pst=con.prepareStatement(sql);
            pst.setString(1, pen);
            rs=pst.executeQuery();
            while(rs.next()){
               // cmb.addItem(rs.getObject(ColumnName));
                stock=rs.getObject("STOCK").toString();
            }
            pst.close();
           // con.close();
            return stock;
       }catch(Exception ex){
       // JOptionPane.showMessageDialog(null, ex);
       } 
        return "0";
    }
   
}


    
    
    
    
   

    


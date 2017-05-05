/**
 * Created by Illyria on 4/30/17.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.*;
import java.util.logging.*;
import java.awt.event.*;

public class Author {
    public static final String DB_URL = "jdbc:mysql://localhost:3306/luyang_test";
    public static final String USER = "root";
    public static final String PASS = "luyang";

    public static final Logger logger = Logger.getLogger( Author.class.getName() );

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("author_welcome");
        frame.setContentPane(new Author(0).author);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

    private javax.swing.JPanel author;
    private javax.swing.JButton statusButton;
    private javax.swing.JButton submitButton;
    private javax.swing.JScrollPane statusScroll;
    private javax.swing.JLabel welcome;


    public Author(int idAuthor){

//        javax.swing.JFrame frame = new javax.swing.JFrame("author_welcome");
//        frame.setContentPane(author);
//        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

        //idAuthor = 1;
        String name = getAuthorName(idAuthor);
        welcome.setText("Welcome! " + name);

        JTable myTable = createTable();
        statusScroll.setViewportView(myTable);

        statusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getManuStatus();
            }
        });

        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

    }



    public static JTable createTable(){


        Statement stmt = null;
        Connection conn = null;
        DefaultTableModel dtm = new DefaultTableModel();
        int idAuthor = 1;
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT title,idManuscript,date FROM Manuscript WHERE idAuthor=" + idAuthor ;
            dtm = buildTableModel(stmt, sql);

        }catch (Exception e){
            logger.log(Level.SEVERE, "Exception in createTable.");
            e.printStackTrace();
        }

        JTable table = new JTable(dtm);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        return table;
    }

    public static JTable createTable2(){


        Statement stmt = null;
        Connection conn = null;
        DefaultTableModel dtm = new DefaultTableModel();
        int idAuthor = 1;
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT title,idManuscript,status FROM Manuscript WHERE idAuthor=" + idAuthor ;
            dtm = buildTableModel(stmt, sql);

        }catch (Exception e){
            logger.log(Level.SEVERE, "Exception in createTable2.");
            e.printStackTrace();
        }

        JTable table = new JTable(dtm);
        table.setFillsViewportHeight(true);
        table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        return table;
    }

    public static DefaultTableModel buildTableModel (Statement stmt, String sql){
        Vector<String> columnNames = new Vector<String>();
        Vector<Vector<Object>> data = new Vector<Vector<Object>>();
        try {
            ResultSet rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }
            while (rs.next()) {
                Vector<Object> vector = new Vector<Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

        } catch (java.sql.SQLException sqle){
            sqle.printStackTrace();
        }
        return new DefaultTableModel(data, columnNames);
    }

    public String getAuthorName(int idAuthor){
        Statement stmt = null;
        Connection conn = null;
        String ret = null;
        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            stmt = conn.createStatement();
            String sql = "SELECT authorLastName, authorFirstName, mailAddress FROM Author WHERE idAuthor="+idAuthor ;

            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                String firstName = rs.getString("authorFirstName");
                String lastName  = rs.getString("authorLastName");
                String addr = rs.getString("mailAddress");
                ret = firstName + lastName + " From " + addr;
            }

        }catch (Exception e){
            logger.log(Level.SEVERE, "Exception in getAuthorName.");
            e.printStackTrace();
        }
        return ret;
    }

    public void getManuStatus() {
        JTable myTable = createTable2();
        statusScroll.setViewportView(myTable);
    }



}

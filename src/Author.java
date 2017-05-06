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
    public static javax.swing.JFrame frame = new javax.swing.JFrame("author_welcome");
    private javax.swing.JPanel Welcome_Part;
    private javax.swing.JLabel Welcome;
    private javax.swing.JButton signoutButton;
    private javax.swing.JButton submitNewButton;
    private javax.swing.JButton checkStausButton;
    private javax.swing.JPanel OpSection;
    private javax.swing.JPanel Author;
    private javax.swing.JScrollPane statusScroll;
    private javax.swing.JButton retractButton;
    private javax.swing.JComboBox retractBox;

    private static Statement stmt = null;
    private static Connection conn = null;
    public final static String QUERY = "SELECT idManuscript, title FROM Manuscript WHERE idAuthor =";

    private static List<String> titles;
    private static List<Integer> ids;
    public static int authorID;



    public static void main(String[] args) {
        JFrame frame = new JFrame("Author");
        frame.setContentPane(new Author().Author);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }



    public Author(){

    }

    public Author(int idAuthor){

        frame = new JFrame("Author");
        frame.setContentPane(Author);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation(400, 0);

        authorID = idAuthor;
        String name = getAuthorName(idAuthor);
        Welcome.setText("Welcome! " + name);

        JTable myTable = createTable(idAuthor);
        statusScroll.setViewportView(myTable);

        checkStausButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getManuStatus(idAuthor);
            }
        });

        submitNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new submit(idAuthor);
            }
        });
        signoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                try {
                    stmt.close();
                    conn.close();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                frame.dispose();
                new Login();
            }
        });

        ResultSet res  = null;
        try {
            res = stmt.executeQuery(QUERY + idAuthor);

            // iterate through results
            titles = new ArrayList<>();
            ids = new ArrayList<>();
            while(res.next()) {
                ids.add(res.getInt(1));
                titles.add(res.getString(2));
                retractBox.addItem(res.getInt(1) + " " + res.getString(2));
            }
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }




        retractButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                JOptionPane.showConfirmDialog(frame,
                        "Are you sure to retract this manuscript" + retractBox.getSelectedItem() + "?");
                int id = getManuId((String)retractBox.getSelectedItem());
                retractManu(id);
            }
        });

    }

    public void retractManu(int id){
        ResultSet res  = null;
        try {
            String sql = "DELETE from Manuscript where idManuscript = ?";
            PreparedStatement preparedStmt = conn.prepareStatement(sql);
            preparedStmt.setInt(1, id);

            // execute the preparedstatement
            preparedStmt.execute();
            JTable myTable = createTable(authorID);
            statusScroll.setViewportView(myTable);

        }catch (Exception e){
            logger.log(Level.SEVERE, "Exception in retract Manu!");
            e.printStackTrace();
        }
    }

    public static JTable createTable(int idAuthor){


        DefaultTableModel dtm = new DefaultTableModel();

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

    public static JTable createTable2(int idAuthor){


        Statement stmt = null;
        Connection conn = null;
        DefaultTableModel dtm = new DefaultTableModel();
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
                ret = firstName +" "+ lastName ;
            }

        }catch (Exception e){
            logger.log(Level.SEVERE, "Exception in getAuthorName.");
            e.printStackTrace();
        }
        return ret;
    }

    public void getManuStatus(int idAuthor) {
        JTable myTable = createTable2(idAuthor);
        statusScroll.setViewportView(myTable);
    }

    public int getManuId(String s){
        String[] array = s.split("\\s+");
        return Integer.parseInt(array[0]);
    }



}

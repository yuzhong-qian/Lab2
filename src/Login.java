import javax.jws.soap.SOAPBinding;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianyuzhong on 4/29/17.
 */
public class Login {
    private JPanel Login;
    private JPanel LoginPart1;
    private JTextField Username_text;
    private JPanel Input;
    private JPanel Buttons;
    private JButton Register_btn;
    private JButton Login_btn;
    private JLabel UserID;
    private JLabel User_type;
    private JComboBox User_type_select;
    private static List<String> interests;
    public static JFrame frame = new JFrame("Login");


    private static Connection con = null;
    private static Statement stmt = null;
    private static ResultSet res  = null;
    public static final String QUERY    = "SELECT interest FROM RICodes;";

    public static void main(String[] args) {
        frame.setContentPane(new Login().Login);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Login(){
//        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
//        frame.setLocation(dim.width/2+frame.getSize().width/2, dim.height/2+frame.getSize().height/2);

        frame.setLocation(600, 300);
        frame.setContentPane(Login);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        //Add three types of user
        User_type_select.addItem("Editor");
        User_type_select.addItem("Author");
        User_type_select.addItem("Reviewer");

        con = DatebaseConnection.connection();
        // initialize a query statement
        try {
            stmt = con.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        Login_btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                String user = (String) User_type_select.getSelectedItem();
                String id = Username_text.getText();
                if(id.equals("")) {
                    JOptionPane.showMessageDialog(frame,
                            "You must input your UserID!");
                    return;
                }
                if(!isNumeric(id)) {
                    JOptionPane.showMessageDialog(frame,
                            "UserID must be valid!");
                    return;
                }
                int userid = Integer.parseInt(id);
                String getUser = "SELECT * FROM " + user + " WHERE id" + user + " = " + userid;

                boolean isEmpty = true;
                try {
                    res = stmt.executeQuery(getUser);
                    while (res.next()) {
                        isEmpty = false;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                if(isEmpty) {
                    JOptionPane.showMessageDialog(frame,
                            "No such userid in " + user + "!");
                } else {
                    frame.dispose();
                    if(user.equals("Editor")) new Editor(userid);
                    else if(user.equals("Author")) new Author(userid);
                    else if(user.equals("Reviewer")) new Reviewer(userid);
                    else JOptionPane.showMessageDialog(frame,
                                "No such user type!");
                    try {
                        stmt.close();
                        con.close();
                        System.out.print("\nConnection terminated.");
                    } catch (Exception exp) { /* ignore cleanup errors */ }
                }
            }
        });

        Register_btn.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // display/center the jdialog when the button is pressed
                frame.setVisible(false);
                new Register();
            }
        });
    }

    public static boolean isNumeric(String str)
    {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }
}

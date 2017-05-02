import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by qianyuzhong on 4/29/17.
 */
public class Register {
    private JPanel Register_Panel;
    private JPanel Input;
    private JTextField last_name_text;
    private JTextField first_name_text;
    private JButton Cancel_btn;
    private JButton Register_btn;
    private JPanel Button;
    private JLabel Register_Label;
    private JPanel Other_info;
    private JTextField mailaddress_text;
    private JTextField emailAddress_text;
    private JLabel EmailAddress;
    private JLabel MailAddress;
    private JComboBox user_type;
    private JPanel Interest;
    private JComboBox Interest1_select;
    private JComboBox Interest2_select;
    private JComboBox Interest3_select;
    private JLabel Interest1;
    private JLabel Interest2;
    private JLabel Interest3;
    private JTextField affiliation_text;
    private JLabel Affiliation;

    private Connection con = null;
    private Statement stmt = null;
    private ResultSet rst = null;

    public static JFrame frame = new JFrame("Register_Label");
    private static List<String> interests;
    private static List<Integer> codes;
    public static final String QUERY    = "SELECT * FROM RICodes;";

    public static void main(String[] args) {
        JFrame frame = new JFrame("Register_Label");
        frame.setContentPane(new Register().Register_Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public Register() {
        frame.setContentPane(Register_Panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        ResultSet res  = null;

        try {
            con = DatebaseConnection.connection();
            // initialize a query statement
            stmt = con.createStatement();

            // query db and save results
            res = stmt.executeQuery(QUERY);

            // iterate through results
            interests = new ArrayList<>();
            codes = new ArrayList<>();
            while(res.next()) {
                codes.add(res.getInt(1));
                interests.add(res.getString(2));
            }

        } catch (SQLException e ) {          // catch SQL errors
            System.err.format("SQL Error: %s", e.getMessage());
        } catch (Exception e) {              // anything else
            e.printStackTrace();
        }


        user_type.addItem("Editor");
        user_type.addItem("Author");
        user_type.addItem("Reviewer");

        Interest2_select.addItem("-");
        Interest3_select.addItem("-");

        for(int i = 0; i < interests.size(); i++) {
            Interest1_select.addItem(codes.get(i) + " " + interests.get(i));
            Interest2_select.addItem(codes.get(i) + " " + interests.get(i));
            Interest3_select.addItem(codes.get(i) + " " + interests.get(i));
        }

        Other_info.setVisible(false);
        Interest.setVisible(false);

        Cancel_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
//                    rst.close();
                    stmt.close();
                    con.close();
                    System.out.print("\nConnection terminated.");
                } catch (Exception exp) {
                    /* ignore cleanup errors */
                    System.out.print("\nSomething went wrong!");
                }
                frame.dispose();
                Login.frame.setVisible(true);
            }
        });

        Register_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) user_type.getSelectedItem();//get the selected item
                String insert = null;
                String id = null;
                int last_inserted_id = 0;
                PreparedStatement insert_table = null;
                Statement find_id = null;
//                Statement
                try {
                    switch (s) {//check for a match
                        case "Editor":
                            insert = "INSERT INTO Editor (`editorLastName`,`editorFirstName`) VALUES (?, ?)";
                            insert_table = con.prepareStatement(insert);
                            find_id = con.createStatement();
                            insert_table.setString(1, last_name_text.getText());
                            insert_table.setString(2, first_name_text.getText());

                            insert_table.executeUpdate();
                            id = "SELECT MAX(`idEditor`) FROM Editor";
                            rst = find_id.executeQuery(id);
                            rst.next();
                            last_inserted_id = rst.getInt(1);
//                            System.out.print("last id: " + last_inserted_id);
                            JOptionPane.showConfirmDialog(frame,
                                    "Your userid is " + last_inserted_id);
                            // cleanup
                            break;
                        case "Author":
                            insert = "INSERT INTO Author (`authorLastName`,`authorFirstName`, `mailAddress`,`emailAddress`,`affliation`" +
                                    ") VALUES (?, ?, ?, ?, ?)";
                            insert_table = con.prepareStatement(insert);
                            find_id = con.createStatement();
                            insert_table.setString(1, last_name_text.getText());
                            insert_table.setString(2, first_name_text.getText());
                            insert_table.setString(3, mailaddress_text.getText());
                            insert_table.setString(4, emailAddress_text.getText());
                            insert_table.setString(5, affiliation_text.getText());

                            insert_table.executeUpdate();
                            id = "SELECT MAX(`idAuthor`) FROM Author";
                            rst = find_id.executeQuery(id);
                            rst.next();
                            last_inserted_id = rst.getInt(1);
//                            System.out.print("last id: " + last_inserted_id);
                            JOptionPane.showConfirmDialog(frame,
                                    "Your userid is " + last_inserted_id);
                            break;
                        case "Reviewer":
                            insert = "INSERT INTO Reviewer (`reviewerLastName`,`reviewerFirstName`,`emailAddress`,`affliation`" +
                                    ") VALUES (?, ?, ?, ?)";
                            insert_table = con.prepareStatement(insert);
                            find_id = con.createStatement();
                            insert_table.setString(1, last_name_text.getText());
                            insert_table.setString(2, first_name_text.getText());
                            insert_table.setString(3, emailAddress_text.getText());
                            insert_table.setString(4, affiliation_text.getText());

                            insert_table.executeUpdate();

                            String[] reviewer_interest = new String[3];
                            reviewer_interest[0] = (String) Interest1_select.getSelectedItem();
                            reviewer_interest[1] = (String) Interest2_select.getSelectedItem();
                            reviewer_interest[2] = (String) Interest3_select.getSelectedItem();

                            id = "SELECT MAX(`idReviewer`) FROM Reviewer";
                            rst = find_id.executeQuery(id);
                            rst.next();
                            last_inserted_id = rst.getInt(1);
//                            System.out.print("last id: " + last_inserted_id);

                            insert = "INSERT INTO InterestList (`code`,`idReviewer`) VALUES ";

                            for(String temp: reviewer_interest)
                                if(!temp.equals("-"))
                                    insert = insert + "(?, ?),";
                            insert = insert.substring(0, insert.length() - 1);
//                            System.out.print(insert);
                            insert_table = con.prepareStatement(insert);
                            int count = 1;
                            for(String temp: reviewer_interest)
                                if(!temp.equals("-")) {
                                    insert_table.setInt(count++, getCode(temp));
                                    insert_table.setInt(count++, last_inserted_id);
                                }

                            insert_table.executeUpdate();
                            JOptionPane.showConfirmDialog(frame,
                                    "Your userid is " + last_inserted_id);
                            break;
                        default:
                            frame.setSize(new Dimension(250, 200));
                            Input.setVisible(true);
                            Other_info.setVisible(false);
                            Interest.setVisible(false);
                            break;
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                try {
                    stmt.close();
                    con.close();
                    System.out.print("\nConnection terminated.");
                } catch (Exception exp) { /* ignore cleanup errors */System.out.print("\nSomething went wrong!"); }
                frame.dispose();
                Login.frame.setVisible(true);
            }
        });

        user_type.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) user_type.getSelectedItem();//get the selected item

                switch (s) {//check for a match
                    case "Editor":
                        frame.setSize(new Dimension(250, 200));
                        Other_info.setVisible(false);
                        Interest.setVisible(false);
                        break;
                    case "Author":
                        frame.setSize(new Dimension(250, 300));
                        Other_info.setVisible(true);
                        break;
                    case "Reviewer":
                        frame.setSize(new Dimension(450, 400));
                        Other_info.setVisible(true);
                        Interest.setVisible(true);
                        break;
                    default:
                        frame.setSize(new Dimension(250, 200));
                        Input.setVisible(true);
                        Other_info.setVisible(false);
                        Interest.setVisible(false);
                        break;
                }
            }
        });
        Interest2_select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) Interest2_select.getSelectedItem();//get the selected item
                if(s.equals((String) Interest1_select.getSelectedItem()) || s.equals((String) Interest3_select.getSelectedItem())) {
                    Interest2_select.setSelectedIndex(0);
                    JOptionPane.showConfirmDialog(frame,
                            "You cannot choose duplicated interests!");
                }
            }
        });

        Interest3_select.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s = (String) Interest3_select.getSelectedItem();//get the selected item
                if(s.equals((String) Interest1_select.getSelectedItem()) || s.equals((String) Interest2_select.getSelectedItem())) {
                    Interest3_select.setSelectedIndex(0);
                    JOptionPane.showConfirmDialog(frame,
                            "You cannot choose duplicated interests!");
                }
            }
        });
    }

    private int getCode(String s) {
        String[] array = s.split("\\s+");
        return Integer.parseInt(array[0]);
    }
}

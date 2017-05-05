/**
 * Created by Illyria on 5/2/17.
 */

import java.awt.event.*;
import javax.swing.*;
public class submit {
    private javax.swing.JPanel panel1;
    private javax.swing.JTextField textField3;
    private javax.swing.JTextField textField4;

    public static void main(String[] args) {
        javax.swing.JFrame frame = new javax.swing.JFrame("submit");
        frame.setContentPane(new submit().panel1);
        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private javax.swing.JTextField textField5;
    private javax.swing.JTextField textField6;
    private javax.swing.JComboBox comboBox1;
    private javax.swing.JTextField textField1;
    private javax.swing.JButton chooseYourFileButton;


    public submit() {
//        javax.swing.JFrame frame = new javax.swing.JFrame("submit");
//        frame.setContentPane(new submit().panel1);
//        frame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
//        frame.pack();
//        frame.setVisible(true);

        chooseYourFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION)
                {
                    java.io.File selectedFile = fileChooser.getSelectedFile();
                    System.out.println(selectedFile.getName());
                }
            }
        });


    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.ParseException;


public class LoginFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel userLabel = new JLabel("USERID");
    JLabel passwordLabel = new JLabel("PIN");
    JTextField userTextField = new JTextField();
    JPasswordField passwordField = new JPasswordField();
    JButton loginButton = new JButton("LOGIN");
    JButton resetButton = new JButton("RESET");
    JCheckBox showPassword = new JCheckBox("Show Password");
    ImageIcon img = new ImageIcon("/../assets/steth.png");
    JLabel l=new JLabel(new ImageIcon((img.getImage()).getScaledInstance(100, 100,  java.awt.Image.SCALE_SMOOTH)));

    LoginFrame() {
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        addActionEvent();

    }

    public void setLayoutManager() {
        container.setLayout(null);
    }

    public void setLocationAndSize() {
        l.setBounds(100,30,150,150);
        userLabel.setBounds(50, 200, 100, 30);
        passwordLabel.setBounds(50, 270, 100, 30);
        userTextField.setBounds(150, 200, 150, 30);
        passwordField.setBounds(150, 270, 150, 30);
        showPassword.setBounds(150, 300, 150, 30);
        loginButton.setBounds(50, 350, 100, 30);
        resetButton.setBounds(200, 350, 100, 30);


    }

    public void addComponentsToContainer() {
            container.add(l);
        container.add(userLabel);
        container.add(passwordLabel);
        container.add(userTextField);
        container.add(passwordField);
        container.add(showPassword);
        container.add(loginButton);
        container.add(resetButton);
    }

    public void addActionEvent() {
        loginButton.addActionListener(this);
        resetButton.addActionListener(this);
        showPassword.addActionListener(this);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        //Coding Part of LOGIN button
        if (e.getSource() == loginButton) {
            String userText;
            String pwdText;
            userText = userTextField.getText();
            pwdText = passwordField.getText();
            try
            {
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");
                Statement stmt = con.createStatement();
                ResultSet rs=stmt.executeQuery("SELECT * FROM `registry` WHERE `user_id` LIKE '"+userText+"' AND `pass` LIKE '"+pwdText+"'");
                rs.last();
                if((rs.getRow()==1) && (userText.equalsIgnoreCase(rs.getString(1)) && pwdText.equalsIgnoreCase(rs.getString(3))))
                {
                    JOptionPane.showMessageDialog(this, "Login Successful");

                    new HospitalFrame(rs.getString(1),rs.getString(2),rs.getString("occupation"));
                    dispose();
                }
                else
                {
                    JOptionPane.showMessageDialog(this, "Invalid Username or Password");
                }
                stmt.close();
                con.close();

            }
            catch (SQLException | ParseException ex)
            {
                System.out.println(ex);
            }


        }
        //Coding Part of RESET button
        if (e.getSource() == resetButton) {
            userTextField.setText("");
            passwordField.setText("");
        }
        //Coding Part of showPassword JCheckBox
        if (e.getSource() == showPassword) {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('*');
            }


        }
    }

}

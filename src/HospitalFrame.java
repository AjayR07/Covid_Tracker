import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;
import java.text.ParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.util.*;
public class HospitalFrame extends JFrame {

    private final JLabel l15;
    private final DebugAction aAct,pAct,dAct;
    private final JTabbedPane tab;
    JPanel p1, p2,p3;
    JMenu menu, submenu;
    JMenuItem i1, i2;
    JLabel l1,l2,l3,l4,l5,l6,l7,l8,l9,l10,l11,l12,l13,l14;
    JTable jtb;
    JComboBox cb;
    String data[][]={{"","",""}};
    DefaultTableModel model;
    JTextArea ta;
    int ind;
    String[][] med =new String[5][2];


    HospitalFrame(String user_id,String name, String occ) throws ParseException {

        getContentPane().setLayout(new BorderLayout());
        Container c=getContentPane();
        p1=new JPanel();
        p2=new JPanel();
        p3=new JPanel();
        p1.setLayout(null);
        l1=new JLabel("A-Z MultiSpeciality Hospital");
        l1.setFont(new Font("Verdana", Font.PLAIN, 18));
        l1.setBounds(100,20,300,25);
        p1.add(l1);
        l2=new JLabel("Patient Admission");
        l2.setFont(new Font("Verdana", Font.PLAIN, 16));
        l2.setBounds(150,50,200,30);
        p1.add(l2);

        l3=new JLabel("Patient Name");
        l3.setBounds(30,120,120,20);
        p1.add(l3);

        JTextField t=new JTextField();
        t.setBounds(180,120,200,20);
        p1.add(t);
        l4=new JLabel("Age");
        l4.setBounds(30,160,120,20);
        p1.add(l4);

        SpinnerModel value =
                new SpinnerNumberModel(1,
                        0,
                        150,
                        1);
        JSpinner spinner = new JSpinner(value);
        spinner.setBounds(180,160,50,25);
        p1.add(spinner);

        l5=new JLabel("Gender");
        l5.setBounds(30,200,120,20);
        p1.add(l5);

        JRadioButton r1=new JRadioButton(" Male");
        JRadioButton r2=new JRadioButton(" Female");
        r1.setBounds(180,200,100,30);
        r2.setBounds(180,230,100,30);
        ButtonGroup bg=new ButtonGroup();
        bg.add(r1);bg.add(r2);
        p1.add(r1);p1.add(r2);


        l6=new JLabel("Symptoms");
        l6.setBounds(30,290,120,20);
        p1.add(l6);

        final DefaultListModel<String> lis = new DefaultListModel<>();
        lis.addElement("Fever");
        lis.addElement("Cold");
        lis.addElement("Cough");
        lis.addElement("Breathing Trouble");
        final JList<String> list1 = new JList<>(lis);
        list1.setBounds(180,290, 200,75);
        list1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        p1.add(list1);
        l8=new JLabel("Remarks");
        l8.setBounds(30,380,120,20);
        p1.add(l8);

        JTextArea area=new JTextArea();
        area.setBounds(180,380, 200,150);
        p1.add(area);
        JButton b1=new JButton("Admit");
        b1.setBounds(160,560,100,30);
        b1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e)
            {

                String Gender="Male";
                if(r1.isSelected())
                    Gender="Male";
                else if(r2.isSelected())
                    Gender="Female";
                Integer age= (Integer) spinner.getValue();
                List<String> sym=list1.getSelectedValuesList();
                String result = String.join(",", sym);
//
                try
                {
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");
                    Statement stmt = con.createStatement();
                  int n=stmt.executeUpdate("Insert into `patient` (`name`,`age`,`gender`,`symptoms`,`remarks`) values('"+t.getText()+"',"+age+",'"+Gender+"','"+result+"','"+area.getText()+"')");
                  if(n==1)
                  {
                      ResultSet rs=stmt.executeQuery("select last_insert_id()");
                      rs.first();
                      JOptionPane.showMessageDialog(p1, "Patient Admission Successful\n        Patient ID:"+rs.getString(1));
                      t.setText("");
                      area.setText("");
                      list1.clearSelection();
                      bg.clearSelection();
                      spinner.setValue(1);

                  }
                  else
                  {
                      JOptionPane.showMessageDialog(p1, "Invalid Info");
                  }

                    stmt.close();
                    con.close();

                }
                catch (SQLException ex)
                {
                    System.out.println(ex);
                }

            }
        });
        p1.add(b1);
        p2.setLayout(null);
        p3.setLayout(null);

        l9=new JLabel("A-Z MultiSpeciality Hospital");
        l9.setFont(new Font("Verdana", Font.PLAIN, 18));
        l9.setBounds(100,20,300,25);
        p3.add(l9);
        l10=new JLabel("Patient Discharge");
        l10.setFont(new Font("Verdana", Font.PLAIN, 16));
        l10.setBounds(150,50,200,30);
        p3.add(l10);

        l11=new JLabel("A-Z MultiSpeciality Hospital");
        l11.setFont(new Font("Verdana", Font.PLAIN, 18));
        l11.setBounds(100,20,300,25);
        p2.add(l11);

        l12=new JLabel("Prescribe Medication");
        l12.setFont(new Font("Verdana", Font.PLAIN, 16));
        l12.setBounds(150,50,200,30);
        p2.add(l12);
        l13=new JLabel("Patient Name");
        l13.setBounds(30,120,120,20);
        p2.add(l13);

        cb=new JComboBox();
        cb.setBounds(180,120,200,20);
        p2.add(cb);
        cb.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if((e.getStateChange()== ItemEvent.SELECTED)&& (cb.getSelectedIndex() > 0)) {
                    String id=cb.getSelectedItem().toString();
                    try
                    {
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");
                        Statement stmt = con.createStatement();

                        ResultSet rs = stmt.executeQuery("SELECT * FROM `patient` where `patient_id` LIKE "+id);
                        rs.next();
                        ta.setText("                       A-Z MultiSpeciality Hospital\n                       ****************************");
                        ta.append("");
                        ta.append("\n   Patient ID        :     "+rs.getString("patient_id"));
                        ta.append("\n   Patient Name  :     "+rs.getString("name"));
                        ta.append("\n   Age                 :     "+rs.getString("Age"));
                        ta.append("\n   Symptoms       :     "+rs.getString("symptoms"));
                        ta.append("\n   Doctor            :     "+name);
                        ta.append("\n***********************************************************");
                        ta.append("\n\n                                    Prescription");
                        ta.append("\n                                    ************");

                        ind=1;
                        stmt.close();
                        con.close();

                    }
                    catch (SQLException ex)
                    {
                        System.out.println(ex);
                    }

                }
            }
        });
        l14=new JLabel("Medicine Name");
        l14.setBounds(30,160,120,20);
        p2.add(l14);

        JTextField tf = new JTextField();
        tf.setBounds(180,160,200,25);
        p2.add(tf);

        l15=new JLabel("Timing");
        l15.setBounds(30,200,120,20);
        p2.add(l15);
        MaskFormatter mf = new MaskFormatter("#-#-#-#");
        mf.setPlaceholderCharacter('#');
        JFormattedTextField Jftf=new JFormattedTextField(mf);
        Jftf.setToolTipText("Mrng-Afn-Evng-Nyt");
        Jftf.setBounds(180,200,200,30);
        p2.add(Jftf);

        JButton b4=new JButton("Add");
        b4.setBounds(150,260,130,30);
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ta.append("\n     "+(ind)+"."+tf.getText()+"                                           "+Jftf.getText());
                med[ind-1][0]=tf.getText();
                med[ind-1][1]=Jftf.getText();
                ind++;
                tf.setText("");
                Jftf.setText("");


            }
        });
        p2.add(b4);
        ta =new JTextArea("                       A-Z MultiSpeciality Hospital");
              ta.append("\n                       ****************************");

        ta.setBounds(30,300, 360,250);
       p2.add(ta);
       JButton b3=new JButton("Prescribe");
       b3.setBounds(150,565,130,30);
       b3.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               ind--;
               int n=0;
               for(int i=0;i<ind;i++)
               {
                  try {
                       Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");

                       Statement stmt = con.createStatement();
                       String sql="Insert into `prescription` (`id`,`patient_id`,`medicine`,`timing`,`dr`) values(NULL,"+cb.getSelectedItem()+",'"+med[i][0].toString()+"','"+med[i][1].toString()+"',"+user_id+")";
//                       System.out.println(sql);
                       n+=stmt.executeUpdate(sql);
                       stmt.close();
                       con.close();
                   }
                catch (SQLException ex)
                   {
                       System.out.println(ex);
                   }

               }
               if(n==ind)
               {
                   JOptionPane.showMessageDialog(p1, "Prescription Saved Successful");
               }

                   tf.setText("");
                   Jftf.setText("");
                   ta.setText("");
               cb.setSelectedIndex(0);

           }
       });
       p2.add(b3);


        Object column[]={"Patient ID","Patient Name","Age"};

        model=new DefaultTableModel(data,column);

        jtb=new JTable(model);

        jtb.setFillsViewportHeight(true);
        JScrollPane sp=new JScrollPane(jtb);
        sp.setBounds(30,100,380,300);
        p3.add(sp);

        JButton b2=new JButton("Discharge");
        b2.setBounds(150,500,150,30);
        b2.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int r[]=jtb.getSelectedRows();
                        try {
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID", "root", "");

                            for (int ele : r) {
                                Statement stmt = con.createStatement();
                                String del = jtb.getValueAt(ele, 0).toString();
                                int n = stmt.executeUpdate("DELETE  FROM `patient` where `patient_id` LIKE " + del);

                                stmt.close();
                            }
                            int i=0;
                            for(int ele :r) {
                                model.removeRow(ele - i);
                                i++;
                            }
                            con.close();
                            JOptionPane.showMessageDialog(null, "Selected Patients Discharged successfully");
                        }
                            catch (SQLException ex)
                            {
                                System.out.println(ex);
                            }
                        }
                    }

        );
        p3.add(b2);

      tab=new JTabbedPane();
        tab.setBounds(0,40,450,700);

        tab.add("Admission",p1);
        tab.add("Prescribe",p2);
        if(occ.equals("Stf"))
        {
            System.out.println(occ);
//            tab.setEnabledAt(1,false);
        }

        tab.add("Discharge",p3);
        c.add(tab);


        tab.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JTabbedPane) {
                    JTabbedPane pane = (JTabbedPane) e.getSource();
                    if(pane.getSelectedIndex()==1)
                    {
                        updatePatientscb();
                    }

                    else if(pane.getSelectedIndex()==2)
                    {
                        updatePatients();
                    }
                }
            }
        });


       aAct=new DebugAction("Admission",new ImageIcon((new ImageIcon("../assets/reg.jpeg").getImage()).getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)),"Admit Patient");
        pAct=new DebugAction("Prescribe",new ImageIcon((new ImageIcon("../assets/pres.jpeg").getImage()).getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)),"Give Prescription");
       dAct=new DebugAction("Discharge",new ImageIcon((new ImageIcon("../assets/dis.jpeg").getImage()).getScaledInstance(20, 20,  java.awt.Image.SCALE_SMOOTH)),"Discharge Patient");

        JMenuBar mb = new JMenuBar();
        menu = new JMenu("Hospital Operations");

        menu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);

        i1 = new JMenuItem(aAct);
        i2 = new JMenuItem(pAct);
        JMenuItem i3 = new JMenuItem(dAct);
        menu.add(i1);
        menu.add(i2);
        menu.add(i3);
        mb.add(menu);
        mb.add(Box.createHorizontalGlue());
        menu = new JMenu(name);
        JMenuItem lt=new JMenuItem("Logout");
        menu.add(lt);
        lt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                dispose();
                try {
                    dispose();
                    new COVID();

                } catch (IOException ioException) {
                    ioException.printStackTrace();
                } catch (org.json.simple.parser.ParseException parseException) {
                    parseException.printStackTrace();
                }
            }
        });
        mb.add(menu);
        setJMenuBar(mb);

        JToolBar Jtb=new JToolBar("Ops");
        JButton a=new JButton(aAct);Jtb.add(a);

        JButton p=new JButton(pAct);Jtb.add(p);
        JButton d=new JButton(dAct);Jtb.add(d);
        add(Jtb,BorderLayout.NORTH);
        JPopupMenu jp=new JPopupMenu();
        jp.add(new JMenuItem(aAct));
        jp.add(new JMenuItem(pAct));
        jp.add(new JMenuItem(dAct));
        p1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jp.show(tab, e.getX(), e.getY());
            }
        });

        p2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jp.show(tab, e.getX(), e.getY());
            }
        });
        p3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                jp.show(tab, e.getX(), e.getY());
            }
        });

        add(jp);
        setLocationRelativeTo(null);
        setSize(450, 750);
        setTitle("A-Z MultiSpeciality Hospital");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class DebugAction extends AbstractAction{
        public DebugAction(String name,Icon image,String tTip)
        {
            putValue(NAME,name);
            putValue(SMALL_ICON,image);

            putValue(SHORT_DESCRIPTION,tTip);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String cons =e.getActionCommand();
            if(cons.equals("Admission"))
            {
                tab.setSelectedIndex(0);
                aAct.setEnabled(false);
                pAct.setEnabled(true);
                dAct.setEnabled(true);

            }
            else if(cons.equals("Prescribe"))
            {
                tab.setSelectedIndex(1);
                aAct.setEnabled(true);
                pAct.setEnabled(false);
                dAct.setEnabled(true);

            }
            else if(cons.equals("Discharge"))
            {
                tab.setSelectedIndex(2);
                aAct.setEnabled(true);
                pAct.setEnabled(true);
                dAct.setEnabled(false);

            }
        }
    }
    private void updatePatientscb() {

        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM `patient`");
            cb.removeAllItems();
            cb.addItem("Select Patients");
            while(rs.next()){
                cb.addItem(String.valueOf(rs.getInt("patient_id")));
            }

            stmt.close();
            con.close();

        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }

    private void updatePatients() {
        try
        {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/COVID","root","");
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM `patient`");
            int rowCount=model.getRowCount();
           if(rowCount!=0){
               for (int i = rowCount - 1; i >= 0; i--) {
                   model.removeRow(i);
               }
           }
            while(rs.next()){
                model.addRow(new Object[]{String.valueOf(rs.getInt("patient_id")),rs.getString("name"),String.valueOf(rs.getInt("age"))});
            }
            stmt.close();
            con.close();

        }
        catch (SQLException ex)
        {
            System.out.println(ex);
        }
    }




}


  
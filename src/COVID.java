import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.border.TitledBorder;
import javax.swing.tree.TreePath;
import java.sql.*;
public class COVID extends JFrame
{
    JFrame f;
    JTree tntree;
    JTable jt;
    JPanel p1,p2,c;
    Image splashScreen;
    ImageIcon imageIcon;
    JMenu menu, submenu;
    JMenuItem i1, i2, i3, i4, i5;
    DefaultMutableTreeNode[] district= new DefaultMutableTreeNode[1000];
    DefaultMutableTreeNode[] state= new DefaultMutableTreeNode[100];
    COVID() throws IOException, ParseException,NullPointerException
    {
        getFromApi();
//        System.out.println(result);
        getContentPane().setLayout(new BorderLayout());
        Object obj = new JSONParser().parse(new FileReader("source.json"));

        JSONObject jo = (JSONObject) obj;
        Object jobb = jo.get("Tamil Nadu");
        JSONObject tn= (JSONObject) jobb;
        Object job = tn.get("districtData");
        JSONObject tndis = (JSONObject) job;


        splashScreen = Toolkit.getDefaultToolkit().getImage("../assets/covid1.jpg");
        imageIcon = new ImageIcon(splashScreen);
        DefaultMutableTreeNode Country=new DefaultMutableTreeNode("India");

        int i=1,j=1;
        for (Object st:jo.keySet()) {
            state[j]=new DefaultMutableTreeNode(st);
            JSONObject stdata= (JSONObject) jo.get(st);
            JSONObject stdat = (JSONObject) stdata.get("districtData");
            for (Object dis : stdat.keySet()) {
                district[i] = new DefaultMutableTreeNode(dis);
                state[j].add(district[i]);
            }
            Country.add(state[j]);
            j++;i++;
        }
        tntree = new JTree(Country);
        String  data[][]={ {"0","0","0"}};
        String column[]={"Active Cases","Confirmed Cases","Total Recovered Cases"};
        DefaultTableModel model=new DefaultTableModel(data,column);

        jt=new JTable(model);
        jt.setRowHeight(0,138);
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 50);
        jt.setFont(font);
        jt.setForeground(Color.DARK_GRAY);
        DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
        dtcr.setHorizontalAlignment(SwingConstants.CENTER);
        jt.getColumnModel().getColumn(0).setCellRenderer(dtcr);
        jt.getColumnModel().getColumn(1).setCellRenderer(dtcr);
        jt.getColumnModel().getColumn(2).setCellRenderer(dtcr);


        tntree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e)
            {
                TreePath arr = e.getPath();
                String city = String.valueOf(arr.getLastPathComponent());
                if(arr.getPathCount()==3){ JSONObject stateselected= (JSONObject) jo.get(String.valueOf(arr.getParentPath().getLastPathComponent()));
                JSONObject stdat = (JSONObject) stateselected.get("districtData");
                JSONObject cityJson = (JSONObject) stdat.get(city);
                System.out.println(cityJson.get("confirmed"));
                model.setValueAt(cityJson.get("active"),0,0);
                model.setValueAt(cityJson.get("confirmed"),0,1);
                model.setValueAt(cityJson.get("recovered"),0,2);
}
                else
                {
                    System.out.println(arr.getPathCount());
                }
//
            }

        });

        JScrollPane sp = new JScrollPane(tntree);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

       p1=new JPanel();
        p1.setBorder(BorderFactory.createEmptyBorder(200, 20, 20, 20));
        p2= new JPanel();
        p2.setBackground(Color.LIGHT_GRAY);
         c= new JPanel();

        p2.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "Covid Statistics",
                TitledBorder.CENTER, TitledBorder.TOP));
        c.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        c.setLayout(new BorderLayout());
        c.add(new JScrollPane(jt),BorderLayout.CENTER);
        c.setPreferredSize(new Dimension(700, 200));
        c.setMinimumSize(new Dimension(100, 30));
        c.setMaximumSize(new Dimension(100, 30));
        p2.add(c);
        p2.setOpaque(true);
        p1.add(p2);
        jt.setOpaque(true);
        p1.setOpaque(true);
        p2.setOpaque(true);
        c.setOpaque(true);
        getContentPane().add(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,sp,p1));



        JMenuBar mb=new JMenuBar();
        menu=new JMenu("Covid Tracker");
        mb.add(menu);
        mb.add(Box.createHorizontalGlue());
        menu=new JMenu("Login / Register");
        submenu=new JMenu("Register");
        menu.add(submenu);
        menu.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
        submenu=new JMenu("Login");
        i1=new JMenuItem("Hospital Login");
        i1.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        LoginFrame frame = new LoginFrame();
                        frame.setTitle("Hospital Login");
                        frame.setVisible(true);
                        frame.setBounds(10, 10, 370, 500);
                        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        frame.setResizable(false);
                        dispose();
                    }
                }
        );
        i2=new JMenuItem("Pharmacy Login");

        submenu.add(i1); submenu.add(i2);
        menu.add(submenu);
        mb.add(menu);
        setJMenuBar(mb);




        setSize(1200,750);
        setTitle("COVID 2k19 Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    public void paint(Graphics g) {
        super.paint(g);
//        g.drawImage(splashScreen, 190, 0, 1010,760,this);
    }


    public void getFromApi()
    {
        String inline="";
        try
        {
            String api="https://api.covid19india.org/state_district_wise.json";
            URL url = new URL(api);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            System.out.println(responsecode);

            if(responsecode != 200)
                throw new RuntimeException("HttpResponseCode: " +responsecode);
            else
            {
                Scanner sc = new Scanner(url.openStream());
                inline = null;
                while(sc.hasNext())
                {
                    inline+=sc.nextLine();
                }
                inline=inline.substring(4);
//                System.out.println(inline);
                FileWriter myWriter = new FileWriter("source.json");
                myWriter.write(inline);
                myWriter.close();
                sc.close();

            }

        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}



//    System.out.println(tndis.keySet());
//    System.out.println(tndis.values());
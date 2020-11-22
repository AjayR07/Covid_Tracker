import javax.swing.*;
import java.awt.*;
public class CreateSplashScreen extends JWindow {
    Image splashScreen;
    ImageIcon imageIcon;
    public CreateSplashScreen() {
        splashScreen = Toolkit.getDefaultToolkit().getImage("../assets/covid.jpg");
        imageIcon = new ImageIcon(splashScreen);

        setSize(imageIcon.getIconWidth()+40,imageIcon.getIconHeight()+40);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width-getSize().width)/2;
        int y = (screenSize.height-getSize().height)/2;
        setBackground(Color.BLACK);
        setLocation(x,y);
        setVisible(true);
    }
    // Paint image onto JWindow
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(splashScreen, 20, 20, this);
    }
    public static void main(String[]args) {
        CreateSplashScreen splash = new CreateSplashScreen();
        try {
            Thread.sleep(5000);
            splash.dispose();
            new COVID();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
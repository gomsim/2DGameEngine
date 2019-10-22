package Graphics;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(){
        setTitle("Death from Above");
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        setVisible(true);
    }

    public void setScene(Renderer renderer){
        add(renderer);
    }
    public void clearScene(Renderer renderer){
        remove(renderer);
    }
}

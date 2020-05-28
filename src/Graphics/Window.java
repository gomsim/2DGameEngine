package Graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArraySet;

public class Window extends JFrame {

    public Window(Renderer renderer, CopyOnWriteArraySet inputBuffer){
        setTitle("Death from Above");
        setSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        //setOpacity(0.3f); //For testing

        Toolkit tk= getToolkit();
        Cursor transparent = tk.createCustomCursor(tk.getImage(""), new Point(), "trans");
        setCursor(transparent);

        addKeyListener(new KeyInputListener(inputBuffer));

        add(renderer);

        setVisible(true);
    }

    private static class KeyInputListener extends KeyAdapter{

        CopyOnWriteArraySet<Integer> inputBuffer;

        KeyInputListener(CopyOnWriteArraySet<Integer> inputBuffer){
            this.inputBuffer = inputBuffer;
        }

        public void keyPressed(KeyEvent event){
            inputBuffer.add(event.getKeyCode());
        }
        public void keyReleased(KeyEvent event){
            inputBuffer.remove(event.getKeyCode());
        }
    }
}

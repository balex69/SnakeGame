import javax.swing.*;
import java.io.IOException;

public class GameFrame extends JFrame {

    GameFrame() throws IOException {

        this.add(new GamePanel());
        this.setTitle("Snake fun!");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);

    }

}

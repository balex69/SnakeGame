import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600, SCREEN_HEIGHT = 600, UNIT_SIZE = 25, DELAY = 70;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX, appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;
    private BufferedImage gameApple = ImageIO.read(getClass().getResource("/res/img/gameApple.png")); // (c) Freepik for all images
    private BufferedImage snakeHead = ImageIO.read(getClass().getResource("/res/img/snakeHead.png"));
    GamePanel() throws IOException {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new GameKeyAdapter());
        startGame();
    }

    public void startGame(){
        spawnApple();
        running = true;
        timer = new Timer(DELAY,this);
        timer.start();
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {

        if(running) {
            for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                //g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT); // utilisé pour prévoir le quadrillage
                //g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            g.drawImage(gameApple, appleX, appleY, this);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.drawImage(snakeHead, x[i], y[i], this);
                } else {
                    g.setColor(new Color(0x75AF55));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void spawnApple() {
        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }
    public void move() {
        for(int i = bodyParts; i>0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;
            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;
            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;
        }
    }
    public void checkApple() {
        if ((x[0] == appleX) && (y[0] == appleY)) {
            bodyParts++;
            applesEaten++;
            spawnApple();
        }
    }
    public void checkCollision() {

        // vérification que la tête ne croque pas le corps
        for(int i = bodyParts; i>0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // vérification que la tête ne touche pas un bord
        if((x[0] < 0) || (x[0] > SCREEN_WIDTH) || (y[0] < 0) || (y[0] > SCREEN_HEIGHT)) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }
    public void gameOver(Graphics g) {

        g.setColor(Color.red);
        g.setFont(new Font("Century Gothic",Font.BOLD,60));
        FontMetrics metricsGO = getFontMetrics(g.getFont());
        g.drawString("Game Over! :(", (SCREEN_WIDTH - metricsGO.stringWidth("Game Over! :("))/2, SCREEN_HEIGHT/2);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metricsSC = getFontMetrics(g.getFont());
        g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metricsSC.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metricsRT = getFontMetrics(g.getFont());
        g.drawString("Try again?", (SCREEN_WIDTH - metricsRT.stringWidth("Try again?"))/2, 400);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(running) {
            move();
            checkApple();
            checkCollision();
        }
        repaint();
    }
    public class GameKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch(e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    if(direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    if(direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != 'L') {
                        direction = 'R';
                    }
                    break;
            }
        }
    }
}

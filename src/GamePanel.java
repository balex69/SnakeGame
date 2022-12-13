import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int ECRAN_LARGEUR = 600, ECRAN_HAUTEUR = 600, TAILLE_UNITES = 25, DELAY = 70;
    static final int UNITES_JEU = (ECRAN_LARGEUR*ECRAN_HAUTEUR)/TAILLE_UNITES;
    final int x[] = new int[UNITES_JEU];
    final int y[] = new int[UNITES_JEU];
    int corpsSerpent = 6;
    int pommesMangees;
    int pommeX, pommeY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;
    private BufferedImage imagePomme = ImageIO.read(getClass().getResource("/res/img/gameApple.png")); // (c) Freepik for all images
    private BufferedImage imageSerpent = ImageIO.read(getClass().getResource("/res/img/snakeHead.png"));
    GamePanel() throws IOException {

        random = new Random();
        this.setPreferredSize(new Dimension(ECRAN_LARGEUR, ECRAN_HAUTEUR));
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
            for (int i = 0; i < ECRAN_HAUTEUR / TAILLE_UNITES; i++) {
                //g.drawLine(i * TAILLE_UNITES, 0, i * TAILLE_UNITES, ECRAN_HAUTEUR); // utilisé pour prévoir le quadrillage
                //g.drawLine(0, i * TAILLE_UNITES, ECRAN_LARGEUR, i * TAILLE_UNITES);
            }
            g.drawImage(imagePomme, pommeX, pommeY, this);

            for (int i = 0; i < corpsSerpent; i++) {
                if (i == 0) {
                    g.drawImage(imageSerpent, x[i], y[i], this);
                } else {
                    g.setColor(new Color(0x75AF55));
                    g.fillRect(x[i], y[i], TAILLE_UNITES, TAILLE_UNITES);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free",Font.BOLD,25));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+pommesMangees, (ECRAN_LARGEUR - metrics.stringWidth("Score: "+pommesMangees))/2, g.getFont().getSize());
        }
        else {
            gameOver(g);
        }

    }
    public void spawnApple() {
        pommeX = random.nextInt((int)(ECRAN_LARGEUR/TAILLE_UNITES))*TAILLE_UNITES;
        pommeY = random.nextInt((int)(ECRAN_HAUTEUR/TAILLE_UNITES))*TAILLE_UNITES;
    }
    public void move() {
        for(int i = corpsSerpent; i>0 ; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }
        switch(direction) {
            case 'U':
                y[0] = y[0] - TAILLE_UNITES;
                break;
            case 'D':
                y[0] = y[0] + TAILLE_UNITES;
                break;
            case 'R':
                x[0] = x[0] + TAILLE_UNITES;
                break;
            case 'L':
                x[0] = x[0] - TAILLE_UNITES;
                break;
        }
    }
    public void checkApple() {
        if ((x[0] == pommeX) && (y[0] == pommeY)) {
            corpsSerpent++;
            pommesMangees++;
            spawnApple();
        }
    }
    public void checkCollision() {

        // vérification que la tête ne croque pas le corps
        for(int i = corpsSerpent; i>0; i--) {
            if((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
        }

        // vérification que la tête ne touche pas un bord
        if((x[0] < 0) || (x[0] > ECRAN_LARGEUR) || (y[0] < 0) || (y[0] > ECRAN_HAUTEUR)) {
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
        g.drawString("Game Over! :(", (ECRAN_LARGEUR - metricsGO.stringWidth("Game Over! :("))/2, ECRAN_HAUTEUR/2);

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metricsSC = getFontMetrics(g.getFont());
        g.drawString("Score: "+pommesMangees, (ECRAN_LARGEUR - metricsSC.stringWidth("Score: "+pommesMangees))/2, g.getFont().getSize());

        g.setColor(Color.red);
        g.setFont(new Font("Ink Free",Font.BOLD,25));
        FontMetrics metricsRT = getFontMetrics(g.getFont());
        g.drawString("Try again?", (ECRAN_LARGEUR - metricsRT.stringWidth("Try again?"))/2, 400);

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

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Game extends JPanel implements ActionListener {
    private int width = 300;
    private int height = 300;

    private Image apple;
    private Image head;
    private Image tail;

    private int tailAmount = 3;
    private int snakeWidth = 10;
    private int snakeX[] = new int[width*height/(snakeWidth*snakeWidth)];
    private int snakeY[] = new int[width*height/(snakeWidth*snakeWidth)];

    private int appleX;
    private int appleY;
    private boolean running;

    private Timer t;

    //0 = left; 1 = right; 2 = up; 3 = down
    public static int direction;


    public Game(){
        addKeyListener(new SnakeListener());
        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        setBackground(Color.DARK_GRAY);

        ImageIcon icon_apple = new ImageIcon("apple.png");
        ImageIcon icon_head = new ImageIcon("head.png");
        ImageIcon icon_tail = new ImageIcon("tail.png");
        apple = icon_apple.getImage();
        head = icon_head.getImage();
        tail = icon_tail.getImage();

        for (int i = 0; i < tailAmount; i++){
            snakeX[i] = 100 - i*10;
            snakeY[i] = 50;

        }
        running = true;
        t = new Timer(200, this);
        t.start();

        spawnApple();
    }

    private void spawnApple() {
        int random = (int) (Math.random()*29);
        appleX = random * snakeWidth;
        random = (int) (Math.random()*29);
        appleY = random * snakeWidth;


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running){
            checkApple();
            checkDeath();
            moveSnake();
        }
        repaint();
    }

    private void moveSnake() {
        for(int i = tailAmount; i > 0; i--){
            snakeX[i] = snakeX[i-1];
            snakeY[i] = snakeY[i-1];
        }
        //0 = left; 1 = right; 2 = up; 3 = down
        switch (direction){
            case 0:
                snakeX[0] -= snakeWidth;
                break;
            case 1:
                snakeX[0] += snakeWidth;
                break;
            case 2:
                snakeY[0] -= snakeWidth;
                break;
            case 3:
                snakeY[0] += snakeWidth;
                break;
            default:
                break;
        }

    }

    private void checkDeath() {
        for(int i = tailAmount; i > 3; i--){
            if(snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]){
                running = false;
            }
        }
        if(snakeX[0] >= width || snakeX[0] >= height || snakeX[0] < 0 || snakeY[0] < 0){
           running = false;
        }
        if(!running){
            t.stop();
        }
    }

    private void checkApple() {
        if(snakeX[0] == appleX && snakeY[0] == appleY){
            tailAmount++;
            spawnApple();
        }
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(running){
            g.drawImage(apple, appleX, appleY, this);
            for(int i = 1; i < tailAmount; i++){
                g.drawImage(tail,snakeX[i], snakeY[i], this);
            }
            g.drawImage(head, snakeX[0], snakeY[0], this);
            Toolkit.getDefaultToolkit().sync();
        } else {
            Font f = new Font("Calibri", Font.BOLD, 16);
            FontMetrics metrics = getFontMetrics(f);
            String gameOverMessage = "Game Over - You died, noob!";

            g.setColor(Color.lightGray);
            g.setFont(f);
            g.drawString(gameOverMessage, (width - metrics.stringWidth(gameOverMessage)), (height / 2) );
        }
    }
}

package controller;

import game.ScreenSwitcher;
import model.ball.Ball;
import model.brick.Brick;
import model.paddle.Paddle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Quản lý toàn bộ logic, hiển thị và vòng lặp chính của game Arkanoid.
 * Kế thừa JPanel, triển khai ActionListener và KeyListener.
 */
public class GameManager extends JPanel implements ActionListener {

    protected static Paddle paddle;                 // Thanh đỡ
    protected static Ball ball;                     // Quả bóng
    private ArrayList<Brick> bricks;       // Gạch
    private Timer timer;                   // Bộ đếm
    private MapManager mapManger;          // Quản lý bản đồ
    protected static int score, lives, highScore;   // Điểm, mạng, điểm cao
    private boolean running; // Trạng thái game và phím
    private ScreenSwitcher screenSwitcher;              // Chuyển đổi màn hình
    private KeyManager keyManager;                // Quản lý phím
    private JLabel gameOver = new JLabel("Game Over!");
    private JLabel restart = new JLabel("Press R to restart.");
    protected static final int PANEL_WIDTH = 1200;
    protected static final int PANEL_HEIGHT = 800;

    /** Khởi tạo paddle, bóng, gạch,... */
    public void initGameObjects() {
        paddle = new Paddle((PANEL_WIDTH - 150) / 2, PANEL_HEIGHT - 100, 150, 20, Color.MAGENTA);
        ball = new Ball(PANEL_WIDTH / 2, PANEL_HEIGHT / 2, 15, 15, 1, Color.BLACK);

        mapManger = new MapManager();
        bricks = mapManger.loadMap(1); // Load map đầu tiên

        ball.setPaddle(paddle);
        ball.setBricks(bricks);


        score = 0;
        lives = 3;
        highScore = 0;

        running = true;

    }

    /**
     * Khởi tạo controller.GameManager.
     * @param width  chiều rộng khung
     * @param height chiều cao khung
     * @param switcher đối tượng điều khiển chuyển màn
     */

    public GameManager(int width, int height, ScreenSwitcher switcher) {
        this.screenSwitcher = switcher;

        setPreferredSize(new Dimension(width, height));

        setFocusable(true);

        keyManager = new KeyManager();

        addKeyListener(keyManager);

        initGameObjects();

        timer = new Timer(1000/60, this);
        timer.start();

        setLayout(null);


        gameOver.setFont(new Font("Arial", Font.BOLD, 72));
        gameOver.setForeground(Color.RED);
        gameOver.setHorizontalAlignment(SwingConstants.CENTER);
        gameOver.setBounds(0, PANEL_HEIGHT / 2 - 120, PANEL_WIDTH, 100); // căn giữa hoàn hảo
        add(gameOver);
        gameOver.setVisible(false);


        restart.setFont(new Font("Arial", Font.PLAIN, 30));
        restart.setForeground(Color.DARK_GRAY);
        restart.setHorizontalAlignment(SwingConstants.CENTER);
        restart.setBounds(0, PANEL_HEIGHT / 2 - 30, PANEL_WIDTH, 60); // nằm ngay dưới "Game Over"
        add(restart);
        restart.setVisible(false);

    }

    /** Cập nhật logic trò chơi mỗi khung hình. */
    public void updateGame() {
        if( running ) {
            if(keyManager.isLeftPressed()) paddle.moveLeft();
            if(keyManager.isRightPressed()) paddle.moveRight();
            if(!keyManager.isLeftPressed() && !keyManager.isRightPressed()) paddle.stop();
            ball.update();
            paddle.update();
            if (ball.outOfBottom()) {
                lives--;
                paddle.resetPosition((PANEL_WIDTH - 150) / 2, PANEL_HEIGHT - 100);
                ball.resetPosition();
            }
            if(lives == 0) {
                running = false;
                gameOver.setVisible(true);
                restart.setVisible(true);
            }
        }
        if (keyManager.isRestartPressed() && lives == 0) {
            initGameObjects();
            gameOver.setVisible(false);
            restart.setVisible(false);
        }


    }

    /** Vẽ trò chơi lên màn hình. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if (running) {
            ball.render(g2d);
            paddle.render(g2d);
        }
        for (Brick brick : bricks) {
            brick.render(g2d);
        }
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Score: " + score, 20, 30);
        g2d.drawString("Lives: " + lives, 100, 30);
        g2d.drawString("High Score: " + highScore, PANEL_WIDTH - 150, 30);


    }

    /** Xử lý vòng lặp game. */
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    // Các getter và setter

    public static int getLives() {
        return lives;
    }

    public static void setLives(int lives) {
        GameManager.lives = lives;
    }

    public static int getScore() {
        return score;
    }

    public static void setScore(int score) {
        GameManager.score = score;
    }

    public static Paddle getPaddle() {
        return paddle;
    }

    public static void setPaddle(Paddle paddle) {
        GameManager.paddle = paddle;
    }
}

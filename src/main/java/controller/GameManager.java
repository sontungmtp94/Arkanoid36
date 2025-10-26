package controller;

import game.ScreenSwitcher;
import model.ball.Ball;
import model.brick.Brick;
import model.paddle.Paddle;
import model.powerup.PowerUp;
import view.GameBackground;
import view.GameOver;
import view.LevelCompleted;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Quản lý toàn bộ logic, hiển thị và vòng lặp chính của game Arkanoid.
 * Kế thừa JPanel, triển khai ActionListener và KeyListener.
 */
public class GameManager extends JPanel implements ActionListener {

    private GameState gameState;
    protected int panelWidth;
    public static int panelHeight;
    protected int currentLevel = 5;          // Mức độ hiện tại
    public static Paddle paddle;                 // Thanh đỡ
    public static Ball ball;
    public static ArrayList<PowerUp> powerUps;
    private ArrayList<Brick> bricks;       // Gạch
    private Timer timer;                   // Bộ đếm
    private MapManager mapManger;          // Quản lý bản đồ
    private GameBackground gameBackground;      // Nền game
    protected static int score, lives, highScore;   // Điểm, mạng, điểm cao
    private KeyManager keyManager;                // Quản lý phím
    private GameOver gameOver;
    private LevelCompleted levelCompleted;

    /** Khởi tạo paddle, bóng, gạch,... */
    public void initGameObjects() {
        paddle = new Paddle(Paddle.getDefaultX(), Paddle.getDefaultY(), Paddle.getDefaultWidth(), Paddle.getDefaultHeight());
        ball = new Ball(panelWidth / 2, panelHeight / 2, 15, 15, 1, Color.BLACK);
        ball.setAndReloadSpritePath("images/balls/ball_default.png");
        ball.setDamage(1);
        powerUps = new ArrayList<>();

        gameBackground = new GameBackground();
        mapManger = new MapManager();
        bricks = mapManger.loadMap(currentLevel);

        ball.setPaddle(paddle);
        ball.setBricks(bricks);

        score = 0;
        lives = 3;
        highScore = 0;

    }

    public void restartGame() {
        initGameObjects();
        gameOver.hidePanel();
        levelCompleted.hidePanel();
        gameState = GameState.READY;
    }

    public void nextLevel() {
        currentLevel++;
        if(currentLevel > 5){
            currentLevel = 1;
        }
        initGameObjects();
        levelCompleted.hidePanel();
        gameState = GameState.READY;
    }

    /**
     * Khởi tạo GameManager.
     * @param width  chiều rộng khung
     * @param height chiều cao khung
     * @param switcher đối tượng điều khiển chuyển màn
     */
    public GameManager(int width, int height, ScreenSwitcher switcher) {
        panelWidth = width;
        panelHeight = height;

        setPreferredSize(new Dimension(panelWidth, panelHeight));

        setFocusable(true);

        keyManager = new KeyManager();

        addKeyListener(keyManager);

        initGameObjects();

        gameState = GameState.READY;

        timer = new Timer(1000/60, this);
        timer.start();

        setLayout(null);

        gameOver = new GameOver(panelWidth, panelHeight, this);
        gameOver.setBounds(0, 0, panelWidth, panelHeight);
        add(gameOver);

        levelCompleted = new LevelCompleted(panelWidth, panelHeight, this);
        levelCompleted.setBounds(0, 0, panelWidth, panelHeight);
        add(levelCompleted);
    }

    /** Cập nhật logic trò chơi mỗi khung hình. */
    public void updateGame() {
        if(gameState == GameState.READY || gameState == GameState.PLAYING) {
            if (keyManager.isLeftPressed()) {
                paddle.moveLeft();
            }
            if (keyManager.isRightPressed()) {
                paddle.moveRight();
            }
            if (!keyManager.isLeftPressed() && !keyManager.isRightPressed()) {
                paddle.stop();
            }
            ball.update();
            paddle.update();
        }

        if (gameState == GameState.READY) {
            if(keyManager.isBallReleased()) {
                ball.launch();
                gameState = GameState.PLAYING;
            }

        }

        if (gameState == GameState.PLAYING) {
            for (int i = 0; i < powerUps.size(); i++) {
                powerUps.get(i).update();
            }

            for(Brick brick : bricks) {
                if(brick.isDestroyed() && !brick.isScored()) {
                    score += 10;
                    brick.setScored(true);
                }
            }

            if (score > highScore) {
                highScore = score;
            }

            boolean allCleared = true;
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()) {
                    allCleared = false;
                    break;
                }
            }

            if (allCleared) {
                gameState = GameState.LEVEL_COMPLETED;
                levelCompleted.showPanel();
            }

            if (ball.outOfBottom()) {
                lives--;
                paddle.resetPaddle();
                ball.resetBall();
                gameState = GameState.READY;
            }

            if (lives == 0) {
                gameState = GameState.GAME_OVER;
                gameOver.showPanel();
            }
        }

        if (keyManager.isPausePressed()) {
            if (gameState == GameState.PLAYING) {
                gameState = GameState.PAUSE;
            } else if (gameState == GameState.PAUSE) {
                gameState = GameState.PLAYING;
            }
            keyManager.clearPause();
        }



        if (keyManager.isNextLevelPressed() && gameState == GameState.LEVEL_COMPLETED) {
            nextLevel();
        }


        if (keyManager.isRestartPressed() && gameState == GameState.GAME_OVER
            || keyManager.isRestartPressed() && gameState == GameState.LEVEL_COMPLETED) {
                restartGame();
        }

    }

    /** Vẽ trò chơi lên màn hình. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameBackground.getBackground(), 0, 0, panelWidth, panelHeight, null);
        Graphics2D g2d = (Graphics2D) g;
        // Bật anti-aliasing cho đồ họa
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // Bật anti-aliasing cho text
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        ball.render(g2d);
        paddle.render(g2d);
        for (Brick brick : bricks) {
            brick.render(g2d);
        }
        for (PowerUp pu : powerUps) {
            pu.render(g2d);
        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.drawString("Score: " + score, 20, 30);
        g2d.drawString("Lives: " + lives, 200, 30);
        g2d.drawString("High Score: " + highScore, panelWidth - 150, 30);
        if (gameState == GameState.PAUSE) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, panelWidth, panelHeight);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 60));
            g2d.drawString("PAUSED", panelWidth / 2 - 150, panelHeight / 2);
        }
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

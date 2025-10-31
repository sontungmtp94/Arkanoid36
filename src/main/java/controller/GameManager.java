package controller;

import game.ArkanoidGame;
import model.ball.Ball;
import model.brick.Brick;
import model.paddle.GalaxyPaddle;
import model.paddle.NormalPaddle;
import model.paddle.Paddle;
import model.powerup.PowerUp;
import model.projectile.Projectile;
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

    private static GameState gameState;
    private static int panelWidth;
    private static int panelHeight;
    protected int currentLevel = 1; // Mức độ hiện tại
    public static Paddle paddle; // Thanh đỡ
    public static ArrayList<PowerUp> powerUps;
    private static ArrayList<Brick> bricks;
    public static ArrayList<Ball> balls; // Bóng
    private static ArrayList<Projectile> projectiles; // Các Projectile.
    private Timer timer; // Bộ đếm
    private MapManager mapManger; // Quản lý bản đồ
    private GameBackground gameBackground; // Nền game
    protected static int score, lives, highScore; // Điểm, mạng, điểm cao
    public static KeyManager keyManager; // Quản lý phím
    private GameOver gameOver;
    private LevelCompleted levelCompleted;
    private final ArkanoidGame game;

    /** Khởi tạo paddle, bóng, gạch,... */
    public void initGameObjects() {
        gameBackground = new GameBackground();
        mapManger = new MapManager();
        bricks = mapManger.loadMap(currentLevel);

        paddle = new GalaxyPaddle(Paddle.getDefaultX(), Paddle.getDefaultY(),
                Paddle.getDefaultWidth(), Paddle.getDefaultHeight());
        projectiles = new ArrayList<>();
        balls = new ArrayList<>();
        Ball ball = new Ball(panelWidth / 2, panelHeight / 2,
                            Ball.getDefaultSize(), Ball.getDefaultSize());
        ball.setAndReloadSpritePath("images/balls/ball_default.png");
        ball.setDamage(1);
        balls.add(ball);
        powerUps = new ArrayList<>();

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
        if (currentLevel > MapManager.getNumOfMaps()) {
            currentLevel = 1;
        }
        initGameObjects();
        levelCompleted.hidePanel();
        gameState = GameState.READY;
    }

    public void stopGame() {
        if (timer != null) {
            timer.stop();      // Dừng vòng lặp game
        }
        PowerUp.cancelAllEffects(); // Hủy mọi hiệu ứng tồn đọng
    }

    /**
     * Khởi tạo GameManager.
     * @param panelWidth  chiều rộng khung
     * @param panelHeight chiều cao khung
     * @param game        cửa sổ ArkanoidGame
     */
    public GameManager(int panelWidth, int panelHeight, ArkanoidGame game) {
        GameManager.panelWidth = panelWidth;
        GameManager.panelHeight = panelHeight;
        this.game = game;

        setPreferredSize(new Dimension(panelWidth, panelHeight));
        setFocusable(true);

        keyManager = new KeyManager();
        addKeyListener(keyManager);

        initGameObjects();

        gameState = GameState.READY;

        timer = new Timer(1000 / 60, this);
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
        if (gameState == GameState.READY || gameState == GameState.PLAYING) {
            if (keyManager.isLeftPressed() && paddle.isMovingAllowed()) {
                paddle.moveLeft();
            }
            if (keyManager.isRightPressed() && paddle.isMovingAllowed()) {
                paddle.moveRight();
            }
            if (!keyManager.isLeftPressed() && !keyManager.isRightPressed()) paddle.stop();

            for (Ball b : balls) b.update();

            if (gameState == GameState.PLAYING) {
                if (keyManager.isSkillXPressed()) {
                    paddle.castSkillX();
                }

                if (keyManager.isSkillCPressed()) {
                    paddle.castSkillC();
                }
            }

            paddle.update();
            updateProjectile();
        }

        if (gameState == GameState.READY) {
            if (keyManager.isBallReleased()) {
                balls.get(0).launch();
                gameState = GameState.PLAYING;
            }
        }

        if (gameState == GameState.PLAYING) {
            for (int i = 0; i < powerUps.size(); i++) powerUps.get(i).update();

            for (Brick brick : bricks) {
                if (brick.isDestroyed() && !brick.isScored()) {
                    score += 10;
                    PowerUp.createPowerUp(brick, 0.2);
                    brick.setScored(true);
                }
            }

            if (score > highScore) highScore = score;

            boolean allCleared = true;
            for (Brick brick : bricks)
                if (!brick.isDestroyed()) { allCleared = false; break; }

            if (allCleared) {
                PowerUp.cancelAllEffects();
                gameState = GameState.LEVEL_COMPLETED;
                levelCompleted.showPanel();
            }

            for (int i = 0; i < balls.size(); i++)
                if (balls.get(i).outOfBottom()) balls.remove(i);

            if (balls.isEmpty()) {
                lives--;
                PowerUp.cancelAllEffects();
                paddle.resetPaddle();
                Ball ball = new Ball(0, 0, Ball.getDefaultSize(),
                                     Ball.getDefaultSize());
                ball.setPaddle(paddle);
                ball.setBricks(bricks);
                ball.setAndReloadSpritePath("images/balls/ball_default.png");
                ball.resetBall();
                ball.launch();
                balls.add(ball);
            }

            if (lives == 0) {
                gameState = GameState.GAME_OVER;
                gameOver.showPanel();
            }
        }

        if (keyManager.isPausePressed()) {
            if (gameState == GameState.PLAYING) gameState = GameState.PAUSE;
            else if (gameState == GameState.PAUSE) gameState = GameState.PLAYING;
            keyManager.clearPause();
        }

        if (keyManager.isNextLevelPressed() && gameState == GameState.LEVEL_COMPLETED) nextLevel();

        if ((keyManager.isRestartPressed() && gameState == GameState.GAME_OVER)
            || (keyManager.isRestartPressed() && gameState == GameState.LEVEL_COMPLETED)) {
            restartGame();
        }
    }

    /** Thêm Projectile do Paddle. */
    public static void addProjectile(Projectile p) {
        projectiles.add(p);
    }

    /** Cập nhật các projectiles. */
    public void updateProjectile() {
        for (Projectile p : projectiles) {
            p.update();
        }
        projectiles.removeIf(p -> !p.isActive());
    }

    /** Vẽ trò chơi lên màn hình. */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(gameBackground.getBackground(), 0, 0, panelWidth, panelHeight, null);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        for (Brick brick : bricks) brick.render(g2d);
        for (PowerUp pu : powerUps) pu.render(g2d);
        for (Ball b : balls) b.render(g2d);
        paddle.render(g2d);
        for (Projectile p : projectiles) p.render(g2d);

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

    // Getter & Setter

    public static int getPanelWidth() {
        return panelWidth;
    }

    public static int getPanelHeight() {
        return panelHeight;
    }

    public static int getLives() { return lives; }
    public static void setLives(int lives) { GameManager.lives = lives; }
    public static int getScore() { return score; }
    public static void setScore(int score) { GameManager.score = score; }
    public static Paddle getPaddle() { return paddle; }
    public static void setPaddle(Paddle paddle) { GameManager.paddle = paddle; }
    public static GameState getGameState() { return gameState; }
    public static ArrayList<Brick> getBricks() { return bricks; }
}

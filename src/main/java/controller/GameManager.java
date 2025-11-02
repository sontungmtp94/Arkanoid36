package controller;

import game.ArkanoidGame;
import model.ball.Ball;
import model.brick.Brick;
import model.paddle.GalaxyPaddle;
import model.paddle.NormalPaddle;
import model.paddle.Paddle;
import model.powerup.PowerUp;
import model.powerup.PowerUpView;
import model.projectile.Projectile;
import view.GameBackground;
import view.GameOver;
import view.LevelCompleted;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Quản lý toàn bộ logic, hiển thị và vòng lặp chính của game Arkanoid.
 * Kế thừa JPanel, triển khai ActionListener và KeyListener.
 */
public class GameManager extends JPanel implements ActionListener {

    private static GameState gameState;
    private static int panelWidth;
    private static int panelHeight;
    public static String playerName;
    protected static int currentLevel = 1; // Mức độ hiện tại
    public static Paddle paddle; // Thanh đỡ
    public static ArrayList<PowerUp> powerUps;
    private PowerUpView powerUpView = new PowerUpView();
    private static ArrayList<Brick> bricks;
    public static ArrayList<Ball> balls; // Bóng
    private static ArrayList<Projectile> projectiles; // Các Projectile.
    private Timer timer; // Bộ đếm
    private MapManager mapManager; // Quản lý bản đồ
    private GameBackground gameBackground; // Nền game
    protected static int score, lives; // Điểm, mạng, điểm cao
    public static KeyManager keyManager; // Quản lý phím
    private GameOver gameOver;
    private LevelCompleted levelCompleted;
    private final ArkanoidGame game;

    /** Khởi tạo paddle, bóng, gạch,... */
    public void initGameObjects() {
        loadPlayerName();
        gameBackground = new GameBackground();
        if (playerName == null || playerName.isEmpty()) {
            playerName = "Unknown";
        }
        mapManager = new MapManager();
        bricks = mapManager.loadMap(currentLevel);

        paddle = new GalaxyPaddle(Paddle.getDefaultX(), Paddle.getDefaultY(),
                Paddle.getDefaultWidth(), Paddle.getDefaultHeight());
        projectiles = new ArrayList<>();
        balls = new ArrayList<>();
        Ball ball = new Ball(panelWidth / 2, panelHeight / 2,
                            Ball.getDefaultSize());
        ball.setAndReloadSpritePath("images/balls/ball_default.png");
        ball.setDamage(1);
        balls.add(ball);
        powerUps = new ArrayList<>();

        ball.setPaddle(paddle);
        ball.setBricks(bricks);

        score = 0;
        lives = 3;
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

            boolean allCleared = true;
            for (Brick brick : bricks)
                if (!brick.isDestroyed()) { allCleared = false; break; }

            if (allCleared) {
                PowerUp.cancelAllEffects();
                LevelCompleted.unlockNewLevel();
                gameState = GameState.LEVEL_COMPLETED;
                levelCompleted.showPanel();
            }

            for (int i = 0; i < balls.size(); i++)
                if (balls.get(i).outOfBottom()) balls.remove(i);

            if (balls.isEmpty()) {
                lives--;
                PowerUp.cancelAllEffects();
                paddle.resetPaddle();
                Ball ball = new Ball(0, 0, Ball.getDefaultSize());
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

        if (gameState == GameState.PAUSE) {
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.fillRect(0, 0, panelWidth, panelHeight);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("IntelOne Display Bold", Font.BOLD, 60));
            g2d.drawString("PAUSED", panelWidth / 2 - 150, panelHeight / 2);
        }

        // Phần thể hiện thông tin của 1 ván trò chơi.
        g.drawImage(gameBackground.getInformationBar(), 0, 610, panelWidth, 40, null);
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 36));
        g2d.drawString(String.valueOf(score), 105, 637);
        for (int i = 0; i < 5; i++) {
            if (i <= (lives - 1)) {
                g.drawImage(gameBackground.getHeart(true), 319 + 30 * i, 616, 28, 25, null);
            } else {
                g.drawImage(gameBackground.getHeart(false), 319 + 30 * i, 616, 28, 25, null);
            }
        }
        String inforLevel = String.valueOf(currentLevel) + " / 20";
        if (currentLevel < 10) {
            inforLevel = "0" + inforLevel;
        }
        g2d.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 32));
        g2d.drawString(inforLevel, 580, 637);

        // Hiện thị thông tin PowerUp vừa kích hoạt.
        powerUpView.draw((Graphics2D) g, panelWidth, panelHeight);
        String msg = PowerUp.getActiveMessage();
        if (msg != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("IntelOne Display Bold", Font.BOLD, 28));
            FontMetrics fm = g2.getFontMetrics();

            int textWidth = fm.stringWidth(msg);
            int centerX = (panelWidth - textWidth) / 2;
            int centerY = 500;

            // Tính độ mờ dần theo thời gian
            float alpha = 1.0f - (float)(System.currentTimeMillis() - PowerUp.messageStartTime) / PowerUp.MESSAGE_DURATION_MS;
            alpha = Math.max(0f, alpha);

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Bóng đổ
            g2.setColor(new Color(0, 0, 0, 150));
            g2.drawString(msg, centerX + 2, centerY + 2);

            // Text chính
            g2.setColor(Color.WHITE);
            g2.drawString(msg, centerX, centerY);

            g2.setComposite(AlphaComposite.SrcOver);
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
    public static int getCurrentLevel() {
        return currentLevel;
    }
    public static void setCurrentLevel(int lv) {
        currentLevel = lv;
    }
    public static ArrayList<Projectile> getProjectiles() {
        return projectiles;
    }
    public static void loadPlayerName() {
        try {
            File file = new File("src/main/resources/DataPlayer.txt");
            if (!file.exists()) return;

            Scanner sc = new Scanner(file, "UTF-8");
            if (sc.hasNextLine()) {
                String name = sc.nextLine().trim();
                if (!name.isEmpty()) playerName = name;
            }
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

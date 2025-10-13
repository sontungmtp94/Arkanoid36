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

    protected static final int PANEL_WIDTH = 1200;
    protected static final int PANEL_HEIGHT = 675;

    /** Khởi tạo paddle, bóng, gạch,... */
    public void initGameObjects() {
        paddle = new Paddle((PANEL_WIDTH - 150) / 2, PANEL_HEIGHT - 50, 150, 20, Color.MAGENTA);
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
     * Khởi tạo GameManager.
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
    }

//    /** Tạo các nút Game Over. */
//    public JPanel createGameOverButtons() {
//    }
//
//    /** Hiển thị nút Game Over. */
//    public void showGameOverButtons(JPanel gameOverPanel) {
//    }
//
//    /** Xóa nút Game Over khỏi màn hình. */
//    public void removeGameOverButtons() {
//    }
//
//    /** Định dạng JButton. */
//    public void styleButton(JButton b) {
//    }

    /** Cập nhật logic trò chơi mỗi khung hình. */
    public void updateGame() {
        if( running ) {
            if(keyManager.isLeftPressed()) paddle.moveLeft();
            if(keyManager.isRightPressed()) paddle.moveRight();
            if(!keyManager.isLeftPressed() && !keyManager.isRightPressed()) paddle.stop();
            ball.update();
            paddle.update();
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
        g2d.drawString("Lives: " + lives, 20, 60);
        g2d.drawString("High Score: " + highScore, PANEL_WIDTH - 150, 30);


    }

    /** Xử lý vòng lặp game. */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) updateGame();
        repaint();
    }
}

import java.awt.*;

/**
 * Lớp Paddle đại diện cho thanh trượt, được người chơi điều khiển để đỡ bóng.
 * Thanh trượt có tốc độ ban đầu cố định.
 * Thanh trượt có thể đi sang hai bên trái/phải và phải dừng lại khi chạm biên.
 */
public class Paddle extends MovableObject {
    /** Màu sắc Paddle. */
    private Color color;

    /** Tốc độ mặc định ban đầu. */
    private final int DEFAULT_SPEED = 8;

    /** Tốc độ hiện tại. */
    private int speed = DEFAULT_SPEED;

    /**
     * Constructor cho Paddle.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     * @param color  Màu sắc
     */
    public Paddle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    /** Di chuyển sang trái. */
    public void moveLeft() {
        setDx(-speed);
    }

    /** Di chuyển sang phải. */
    public void moveRight() {
        setDx(speed);
    }

    /** Dừng di chuyển. */
    public void stop() {
        setDx(0);
    }

    /** Cập nhật vị trí của Paddle khi di chuyển. */
    @Override
    public void update() {
        setX((int) (getX() + getDx()));

        // Giữ Paddle luôn trong biên
        if (getX() < 0) {
            setX(0);
        } else if (getX() + getWidth() > ArkanoidGame.WIDTH) {
            setX(ArkanoidGame.WIDTH - getWidth());
        }
    }

    /**
     * Render Paddle lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    // Các getter và setter

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getDefaultSpeed() {
        return DEFAULT_SPEED;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}

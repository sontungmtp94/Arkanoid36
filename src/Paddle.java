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
    private int speed = 8;

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
    }

    /**
     * Render Paddle lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(color);
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    // Các getter và setter

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getSpeed() {
        return speed;
    }
}

import java.awt.*;

public class Paddle extends MovableObject {
    /** Màu sắc Paddle. */
    private Color color;

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
    public void moveLeft() {}

    /** Di chuyển sang phải. */
    public void moveRight() {}

    /** Dừng di chuyển. */
    public void stop() {}

    /** Cập nhật vị trí. */
    @Override
    public void update() {}

    /**
     * Render Paddle lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {}

    // Các getter và setter

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

import java.awt.*;

public class Ball extends MovableObject {
    /** Lượng sát thương của Ball. */
    private int damage;

    /** Màu sắc Ball. */
    private Color color;

    /**
     * Constructor cho Ball.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     * @param damage Sát thương
     * @param color  Màu sắc
     */
    public Ball(int x, int y, int width, int height, int damage, Color color) {
        super(x, y, width, height);
        this.damage = damage;
        this.color = color;
    }

    /** Cập nhật vị trí. */
    @Override
    public void update() {}

    /**
     * Kiểm tra va chạm của Ball.
     */
    public boolean checkCollision(GameObject other) {
        // TO-DO
        return false;
    }

    /**
     * Nảy ra sau khi va chạm.
     */
    public void bounce(GameObject other) {}

    /** Kiểm tra Ball rơi xuống đáy. */
    public boolean outOfBottom() {
        // TO-DO
        return false;
    }

    /**
     * Render Ball lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {}

    // Các getter và setter

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

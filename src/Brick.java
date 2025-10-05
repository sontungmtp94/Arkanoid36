import java.awt.*;

public class Brick  extends GameObject {
    /** Loại Brick. */
    private String type;

    /** Độ bền Brick. */
    private int hitPoints;

    /** Màu sắc Brick. */
    private Color color;

    /**
     * Constructor cho Brick.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public Brick(int x, int y, int width, int height, String type, int hitPoints, Color color) {
        super(x, y, width, height);
        this.type = type;
        this.hitPoints = hitPoints;
        this.color = color;
    }

    /** Cập nhật trạng thái của Brick. */
    @Override
    public void update() {}

    /**
     * Giảm độ bền Brick khi Ball đánh trúng.
     *
     * @param damage: Lượng sát thương của Ball gây ra.
     */
    public void takeHits(int damage) {
         hitPoints -= damage;
    }

    /**
     * Kiểm tra xem Brick đã bị phá chưa.
     *
     * @return true nếu hitPoints <= 0, false nếu ngược lại.
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }


    /** Render Brick lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {}
    // Các getter và setter

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

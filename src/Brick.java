import java.awt.*;

/**
 * Lớp Brick đại diện cho gạch.
 * Gạch có nhiều loại BrickType,ứng với màu săc và độ bền mặc định khác nhau.
 * Gạch bị phá hủy khi hết độ bền.
 */
public class Brick  extends GameObject {
    /** Loại Brick. */
    private final BrickType type;

    /** Độ bền Brick. */
    private int hitPoints;

    /**
     * Constructor cho Brick.
     *
     * @param x         Tọa độ x (ngang)
     * @param y         Tọa độ y (dọc)
     * @param width     Chiều rộng
     * @param height    Chiều cao
     * @param type      Loại gạch
     */
    public Brick(int x, int y, int width, int height,
                 BrickType type) {
        super(x, y, width, height);
        this.type = type;
        this.hitPoints = type.getHitPoints();
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
         setHitPoints(hitPoints -= damage);
         if (hitPoints < 0) {
             setHitPoints(0);
         }
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
    public void render(Graphics2D g) {
        if (isDestroyed()) return;

        g.setColor(type.getColor());
        g.fillRect(getX(), getY(), getWidth(), getHeight());
    }

    // Các getter và setter

    public BrickType getType() {
        return type;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}

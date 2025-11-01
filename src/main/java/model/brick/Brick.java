package model.brick;

import model.base.GameObject;
import java.awt.*;
import java.awt.image.BufferedImage;


/**
 * Lớp model.brick.Brick đại diện cho gạch.
 * Gạch có nhiều loại model.brick.BrickType,ứng với màu săc và độ bền mặc định khác nhau.
 * Gạch bị phá hủy khi hết độ bền.
 */
public class Brick extends GameObject {
    /** Loại model.brick.Brick. */
    private final BrickType type;

    /** Độ bền model.brick.Brick trong game. */
    private int hitPoints;

    private boolean scored = false; // Biến kiểm tra đã được tính điểm chưa

    /**
     * Constructor cho model.brick.Brick.
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
        this.hitPoints = type.getHitPoints(); // Độ cứng lấy từ BrickType.
    }

    /** Cập nhật trạng thái của model.brick.Brick. */
    @Override
    public void update() {}

    /**
     * Giảm độ bền model.brick.Brick khi model.ball.Ball đánh trúng.
     *
     * @param damage: Lượng sát thương của model.ball.Ball gây ra.
     */
    public void takeHits(int damage) {
        if (type == BrickType.METAL) {
            System.out.println("[METAL] hit ignored!");
            return;
        }

        // Giảm HP của gạch sau khi va chạm.
        hitPoints -= damage;

        System.out.printf("[HIT] %s took %d damage → HP now %d%n", type, damage, hitPoints);

        // Gạch sẽ vỡ khi HP <= 0
        if (hitPoints < 0) {
            hitPoints = 0;
        }
    }

    /**
     * Kiểm tra xem model.brick.Brick đã bị phá chưa.
     *
     * @return true nếu hitPoints <= 0, false nếu ngược lại.
     */
    public boolean isDestroyed() {
        return hitPoints <= 0;
    }

    @Override
    public Shape getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /** Render model.brick.Brick lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        if (isDestroyed()) {
            return; // Nếu gạch bị phá, xóa gạch.
        }

        // Lấy ảnh sprite tương ứng với hp hiện tại.
        BufferedImage img = type.getFrame(hitPoints);  // Lấy frame theo HP còn lại
        if (img != null) {
            g.drawImage(img, x, y, width, height, null);
        }
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


    public boolean isScored() {
        return scored;
    }

    public void setScored(boolean scored) {
        this.scored = scored;
    }
}

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
        this.hitPoints = type.getHitPoints();
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
         setHitPoints(hitPoints -= damage);
         if (hitPoints < 0) {
             setHitPoints(0);
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

    /** Render model.brick.Brick lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        if (isDestroyed()) {
            return;
        }

        BufferedImage img = type.getSprite();
        if (img != null) {
            g.drawImage(img, getX(), getY(), getWidth(), getHeight(), null);
        } else {

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

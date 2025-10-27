package model.paddle;

import game.ArkanoidGame;
import model.base.*;
import static view.SpritesView.*;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Lớp Paddle đại diện cho thanh trượt, được người chơi điều khiển để đỡ bóng.
 * Thanh trượt có tốc độ ban đầu cố định.
 * Thanh trượt có thể đi sang hai bên trái/phải và phải dừng lại khi chạm biên.
 */
public abstract class Paddle extends MovableObject {
    // Kích thước mặc định trong game.
    protected static final int DEFAULT_WIDTH = 150;
    protected static final int DEFAULT_HEIGHT = 30;

    // Tọa độ x, y mặc định trong game.
    protected static final int DEFAULT_X = (ArkanoidGame.getGameWidth()
                                          - DEFAULT_WIDTH) / 2;
    protected static final int DEFAULT_Y = ArkanoidGame.getGameHeight()
                                         - DEFAULT_HEIGHT - 80;

    // Tốc độ mặc định ban đầu.
    protected static final double DEFAULT_SPEED = 12.0;

    // Tốc độ hiện tại.
    protected double speed;

    // Kỹ năng bị động và chủ động.
    protected Skill passiveSkill;
    protected Skill activeSkill;

    // Sprite Paddle.
    protected BufferedImage sprite;

    // Path đến sprite.
    protected String spritePath;

    /**
     * Constructor cho Paddle.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        speed = DEFAULT_SPEED;
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

    /** Cập nhật Paddle trong game. */
    @Override
    public void update() {
        setX(getX() + (int) getDx());

        // Giữ Paddle luôn trong biên
        if (getX() < 0) {
            setX(0);
        } else if (getX() + getWidth() > ArkanoidGame.getGameWidth()) {
            setX(ArkanoidGame.getGameWidth() - getWidth());
        }
    }

    /** Reset trạng thái Paddle. */
    public void resetPaddle() {
        setX(DEFAULT_X);
        setY(DEFAULT_Y);
        setWidth(DEFAULT_WIDTH);
        setHeight(DEFAULT_HEIGHT);
        speed = DEFAULT_SPEED;
    }

    /** Thực hiện kỹ năng bị động. */
    public abstract void applyPassiveSkill();

    /** Kích hoạt kỹ năng chủ động. */
    public abstract void castActiveSkill();

    /** Cập nhật sprite tương ứng khi Width thay đổi do Powerup. */
    public void updateSpriteByWidth() {
        if (getWidth() == getDefaultWidth() - 60)
            setAndLoadSprite(getPathShort());
        else if (getWidth() == getDefaultWidth() + 60)
            setAndLoadSprite(getPathLong());
        else
            setAndLoadSprite(getPathDefault());
    }

    public abstract String getPathShort();
    public abstract String getPathLong();
    public abstract String getPathDefault();

    /**
     * Render Paddle lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        if (sprite != null) {
            g.drawImage(sprite, getX(), getY(), getWidth(), getHeight(), null);
        }
    }

    public void setAndLoadSprite(String spritePath) {
        this.spritePath = spritePath;
        sprite = loadSprite(this.spritePath);
    }

    // Các getter và setter

    public static int getDefaultX() {
        return DEFAULT_X;
    }

    public static int getDefaultY() {
        return DEFAULT_Y;
    }

    public static int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public static int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }
}

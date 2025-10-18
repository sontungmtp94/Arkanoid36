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
public class Paddle extends MovableObject {
    // Kích thước mặc định trong game.
    private static final int DEFAULT_WIDTH = 100;
    private static final int DEFAULT_HEIGHT = 20;

    // Tọa độ x, y mặc định trong game.
    private static final int DEFAULT_X = (ArkanoidGame.getGameWidth() - DEFAULT_WIDTH) / 2;
    private static final int DEFAULT_Y = ArkanoidGame.getGameHeight() - DEFAULT_HEIGHT - 80;

    // Tốc độ mặc định ban đầu.
    private static final double DEFAULT_SPEED = 15.0;

    // Tốc độ hiện tại.
    private double speed;

    // Path đến ảnh Paddle mặc định.
    private static final String DEFAULT_SPRITE_PATH = "images/paddles/GalaxyPaddle_default.png";

    // Path đến ảnh Paddle.
    private String spritePath;

    // Sprite Paddle.
    private BufferedImage sprite;

    /**
     * Constructor cho Paddle.
     *
     * @param x           Tọa độ x (ngang)
     * @param y           Tọa độ y (dọc)
     * @param width       Chiều rộng
     * @param height      Chiều cao
     */
    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        speed = DEFAULT_SPEED;
        spritePath = DEFAULT_SPRITE_PATH;
        sprite = loadSprite(spritePath);
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
        spritePath = DEFAULT_SPRITE_PATH;
        sprite = loadSprite(spritePath);
    }

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

    public String getSpritePath() {
        return spritePath;
    }

    public void setSpritePath(String spritePath) {
        this.spritePath = spritePath;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }
}

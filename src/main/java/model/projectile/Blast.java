package model.projectile;

import controller.GameManager;
import model.brick.Brick;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

/**
 * Blast là tia bắn do GalaxyPaddle tạo ra khi kích hoạt skillC.
 */
public class Blast extends Projectile {
    private static final int DEFAULT_WIDTH = 50;
    private static final int DEFAULT_HEIGHT = 650;
    private static final int DAMAGE = 1;
    private static final int LIFETIME = 250; // 0.25s
    private static final String SPRITE_PATH = "images/paddles/galaxy/blast.png";

    private BufferedImage sprite;
    private long startTime;
    /**
     * Constructor cho Blast.
     *
     * @param x      Tọa độ x, Chiều ngang từ Trái sang Phải
     * @param y      Tọa độ y, Chiều dọc từ Trên xuống Dưới
     */
    public Blast(int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT);
        sprite = loadSprite(SPRITE_PATH);
        active = true;
        startTime = System.currentTimeMillis();
    }

    @Override
    public void update() {
        if (!active) {
            return;
        }

        // Nếu Blast xuyên qua Brick thì gây sát thương.
        Rectangle2D blastRect = (Rectangle2D) getBounds();
        for (Brick b : GameManager.getBricks()) {
            if (blastRect.intersects((Rectangle2D) b.getBounds())) {
                b.takeHits(DAMAGE);
            }
        }

        if (System.currentTimeMillis() - startTime > LIFETIME) {
            active = false;
        }
    }

    @Override
    public Shape getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        g.drawImage(sprite, getX(), getY(), DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
    }

    // Getter và setter.

    public static int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public static int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }

    public static int getLifeTime() {
        return LIFETIME;
    }
}

package model.projectile;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

/**
 * Lớp Nuke đại diện cho nuke từ kỹ năng C của BomberPaddle.
 * Nuke kích thước 60 x 60, khi nổ gây 5 sát thương cho gạch trong vùng nổ.
 * Vùng nổ tính từ tâm nuke với bán kính 180.
 */
public class Nuke extends ExplosiveProjectile {
    private static final int DEFAULT_SIZE = 60;
    private static final int EXPLOSION_RADIUS = 180;
    private static final int DAMAGE = 3;

    private static final String PATH_NORMAL = "images/paddles/bomber/nuke.png";
    private static final String PATH_GRAY = "images/paddles/bomber/nuke_deploying.png";

    private BufferedImage spriteNormal;
    private BufferedImage spriteGray;

    /**
     * Constructor cho Nuke.
     *
     * @param x Tọa độ x
     * @param y Tọa độ y
     */
    public Nuke(int x, int y) {
        super(x, y, DEFAULT_SIZE, DEFAULT_SIZE, EXPLOSION_RADIUS, DAMAGE);
        spriteNormal = loadSprite(PATH_NORMAL);
        spriteGray = loadSprite(PATH_GRAY);
    }

    /** Trả về vùng bao quanh Nuke. */
    @Override
    public Shape getBounds() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    /** Trả về vùng bao quanh vùng nổ. */
    public Ellipse2D getExplosionBounds() {
        return new Ellipse2D.Double(x + width / 2.0 - explosionRadius,
                                    y + height / 2.0 - explosionRadius,
                                    explosionRadius * 2.0,
                                    explosionRadius * 2.0);
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        if (exploded) {
            g.drawImage(explosion,
                        x + width / 2 - explosionRadius,
                        y + height / 2 - explosionRadius,
                        explosionRadius * 2,
                        explosionRadius * 2,
                        null);
            return;
        }

        BufferedImage sprite = deployed ? spriteNormal : spriteGray;
        g.drawImage(sprite, x, y, DEFAULT_SIZE, DEFAULT_SIZE, null);
    }

    // Getter.

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }
}

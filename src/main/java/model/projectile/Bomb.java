package model.projectile;

import view.PngAnimator;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

/**
 * Lớp Bomb đại diện cho bomb từ kỹ năng X của BomberPaddle.
 * Bomb có kích thước 60 x 84, khi nổ gây 1 sát thương cho gạch trong vùng nổ.
 * Vùng nổ tính từ tâm của thân bomb với bán kính 90.
 */
public class Bomb extends ExplosiveProjectile {
    private static final int DEFAULT_WIDTH = 30;
    private static final int DEFAULT_HEIGHT = 42;
    private static final int EXPLOSION_RADIUS = 50;
    private static final int DAMAGE = 1;

    private static final String PATH_NORMAL = "images/paddles/bomber/bomb.png";
    private static final String PATH_GRAY = "images/paddles/bomber/bomb_deploying.png";

    private BufferedImage spriteNormal;
    private BufferedImage spriteGray;
    private PngAnimator explosionAnimator;
    private boolean playingExplosion = false;

    /**
     * Constructor cho Bomb.
     *
     * @param x Tọa độ x
     * @param y Tọa độ y
     */
    public Bomb(int x, int y) {
        super(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, EXPLOSION_RADIUS, DAMAGE);
        spriteNormal = loadSprite(PATH_NORMAL);
        spriteGray = loadSprite(PATH_GRAY);
    }

    @Override
    public void update() {
        super.update();

        if (exploded && !playingExplosion) {
            explosionAnimator = new PngAnimator(PATH_EXPLOSION, 10, 60, 2.0);
            playingExplosion = true;
        }
    }

    @Override
    public Shape getBounds() {
        return new Ellipse2D.Double(x, y + (height - width), width, width);
    }

    public Ellipse2D getExplosionBounds() {
        return new Ellipse2D.Double(x + width / 2.0 - explosionRadius,
                                    y + (height - width) + width / 2.0 - explosionRadius,
                                    explosionRadius * 2.0, explosionRadius * 2.0);
    }

    @Override
    public void render(Graphics2D g) {
        if (!active && !playingExplosion) {
            return;
        }

        if (exploded) {
            // checker
            if (explosionAnimator == null) {
                System.err.println("no explo anim");
                return;
            }

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.translate(x + width / 2.0 - explosionRadius,
                          y + (height - width) + width / 2.0 - explosionRadius);
            explosionAnimator.paint(g2d);
            g2d.dispose();
            return;
        }

        BufferedImage sprite = deployed ? spriteNormal : spriteGray;
        g.drawImage(sprite, x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, null);
    }

    // Các getter

    public static int getDefaultWidth() {
        return DEFAULT_WIDTH;
    }

    public static int getDefaultHeight() {
        return DEFAULT_HEIGHT;
    }
}

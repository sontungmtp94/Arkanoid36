package model.projectile;

import view.PngAnimator;

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
    private static final int EXPLOSION_RADIUS = 90;
    private static final int DAMAGE = 3;

    private static final String PATH_NORMAL = "images/paddles/bomber/nuke.png";
    private static final String PATH_GRAY = "images/paddles/bomber/nuke_deploying.png";

    private BufferedImage spriteNormal;
    private BufferedImage spriteGray;
    private PngAnimator explosionAnimator;
    private boolean playingExplosion = false;

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
        return new Ellipse2D.Double(x, y, width, height);
    }

    public Ellipse2D getExplosionBounds() {
        return new Ellipse2D.Double(x + width / 2.0 - explosionRadius,
                                    y + height / 2.0 - explosionRadius,
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
                          y + height / 2.0 - explosionRadius);
            explosionAnimator.paint(g2d);
            g2d.dispose();
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

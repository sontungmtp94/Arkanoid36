package model.projectile;

import controller.GameManager;
import game.ArkanoidGame;
import model.brick.Brick;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

/**
 * Lớp ExplosiveProjectile đại diện cho các loại Projectile gây nổ (Bomb, Nuke).
 * Xử lý đặt, nổ, gây sát thương và nổ dây chuyền.
 */
public abstract class ExplosiveProjectile extends Projectile {
    protected final int explosionRadius;
    protected final int damage;

    protected boolean deployed = false;
    private static final double DEPLOYING_SPEED = 25.0;

    protected boolean exploded = false;
    protected long deployedTime;
    protected long explosionStartTime;
    protected final int EXPLODE_DURATION = 250;
    protected final int EXPLODE_DELAY = 2000;

    protected static final String PATH_EXPLOSION = "images/paddles/bomber/explosion.png";
    protected BufferedImage explosion;

    public ExplosiveProjectile(int x, int y, int width, int height,
                               int explosionRadius, int damage) {
        super(x, y, width, height);
        this.explosionRadius = explosionRadius;
        this.damage = damage;
        explosion = loadSprite(PATH_EXPLOSION);
        active = true;
    }

    /** Cập nhật trạng thái. */
    @Override
    public void update() {
        if (!active) {
            return;
        }

        if (exploded) {
            if (System.currentTimeMillis() - explosionStartTime > EXPLODE_DURATION) {
                active = false;
            }
            return; // vẫn render animation
        }

        // Chưa đặt xuống, di chuyển để đặt.
        if (!deployed) {
            if (GameManager.keyManager.isArrowUpPressed()) {
                y -= (int) DEPLOYING_SPEED;
            }

            if (GameManager.keyManager.isArrowDownPressed()) {
                y += (int) DEPLOYING_SPEED;
            }

            if (GameManager.keyManager.isArrowLeftPressed()) {
                x -= (int) DEPLOYING_SPEED;
            }

            if (GameManager.keyManager.isArrowRightPressed()) {
                x += (int) DEPLOYING_SPEED;
            }

            // Giữ luôn trong trần, đáy và 2 biên.
            if (x < 0) {
                x = 0;
            } else if (x + width > ArkanoidGame.getGameWidth() - 1) {
                x = ArkanoidGame.getGameWidth() - width - 1;
            }

            if (y < 0) {
                y = 0;
            } else if (y + height > ArkanoidGame.getGameHeight() - 1) {
                y = ArkanoidGame.getGameHeight() - height - 1;
            }

            // Đặt xuống.
            if (GameManager.keyManager.isDeployPressed()) {
                deployed = true;
                deployedTime = System.currentTimeMillis();
            }
        } else {
            if (System.currentTimeMillis() - deployedTime > EXPLODE_DELAY) {
                explode();
            }
        }
    }

    /** Nổ, gây sát thương cho Brick và nổ dây chuyền. */
    public void explode() {
        if (exploded) {
            return;
        }
        exploded = true;
        explosionStartTime = System.currentTimeMillis();

        Area explosionArea = new Area(getExplosionBounds());

        // Gây sát thương cho Brick trong vùng nổ.
        for (Brick b : GameManager.getBricks()) {
            Area intersectArea = new Area(b.getBounds());
            intersectArea.intersect(explosionArea);
            if (!intersectArea.isEmpty()) {
                b.takeHits(damage);
            }
        }

        // Gây nổ dây chuyền.
        for (Projectile p : GameManager.getProjectiles()) {
            if (p instanceof Bomb b && b != this && b.isDeployed()) {
                Area intersectArea = new Area(b.getBounds());
                intersectArea.intersect(explosionArea);
                if (!intersectArea.isEmpty()) {
                    b.explode();
                }
            }
        }
    }

    /** Lấy vùng nổ. */
    public abstract Ellipse2D getExplosionBounds();

    // Getter

    public boolean isDeployed() {
        return deployed;
    }
}

package model.paddle;

import controller.GameManager;
import model.projectile.Bomb;
import model.projectile.Nuke;

import static view.SpritesView.loadSprite;

/**
 * BomberPaddle có các kỹ năng:
 * Kỹ năng X: Đặt 1 bomb, có thể tích lũy bomb, phạm vi nổ nhỏ, sát thương bé.
 * Kỹ năng C: Đặt 1 nuke, nổ phạm vi lớn, sát thương lớn.
 */
public class BomberPaddle extends Paddle {
    private final int X_COOLDOWN = 10000;
    private final int X_MAX_STACK = 5;
    private final int C_COOLDOWN = 50000;
    // 3 path đến 3 sprites của BomberPaddle.
    private final String PATH_DEFAULT = "images/paddles/bomber/BomberPaddle_default.png";
    private final String PATH_SHORT = "images/paddles/bomber/BomberPaddle_short.png";
    private final String PATH_LONG = "images/paddles/bomber/BomberPaddle_long.png";

    private boolean skillXOn = false;
    private int stackX = X_MAX_STACK;
    private long cooldownTimerX = 0;
    private boolean skillCOn = false;
    private int cooldownTimerC = 0;

    /**
     * Constructor cho BomberPaddle.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public BomberPaddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        spritePath = PATH_DEFAULT;
        sprite = loadSprite(spritePath);
    }

    /** Kích hoạt kỹ năng X, tạo một bomb và giảm 1 stack hiện tại. */
    @Override
    public void castSkillX() {
        if (stackX < 1 || skillXOn || skillCOn) {
            return;
        }

        skillXOn = true;
        stackX--;
        int bombX = x + width / 2 - Bomb.getDefaultWidth() / 2;
        int bombY = y - Bomb.getDefaultHeight();
        Bomb bomb = new Bomb(bombX, bombY);
        GameManager.addProjectile(bomb);
    }

    /** Kích hoạt kỹ năng C, tạo một nuke. */
    @Override
    public void castSkillC() {
        if (cooldownTimerC > 0 || skillCOn || skillXOn) {
            return;
        }

        skillCOn = true;
        int nukeX = x + width / 2 - Nuke.getDefaultSize() / 2;
        int nukeY = y - Nuke.getDefaultSize();
        Nuke nuke = new Nuke(nukeX, nukeY);
        GameManager.addProjectile(nuke);
    }

    /** Xử lý cooldown kỹ năng X và C. */
    @Override
    public void update() {
        super.update();

        // Nếu không còn bomb đang deploy thì mới reset skillXOn
        if (skillXOn && GameManager.getProjectiles()
                                   .stream()
                                   .noneMatch(p -> p instanceof Bomb
                                              && !((Bomb)p).isDeployed())) {
            skillXOn = false;
            cooldownTimerX = X_COOLDOWN;
        }

        // Cooldown skill X
        if (stackX < X_MAX_STACK && !skillXOn) {
            cooldownTimerX -= 1000 / GameManager.getFps();
            System.out.println("cooldownX is " + cooldownTimerX);
            if (cooldownTimerX <= 0) {
                stackX++;
                cooldownTimerX = X_COOLDOWN;
            }
        }

        // Nếu không còn nuke đang deploy thì mới reset skillCOn
        if (skillCOn && GameManager.getProjectiles()
                                   .stream()
                                   .noneMatch(p -> p instanceof Nuke
                                              && !((Nuke)p).isDeployed())) {
            skillCOn = false;
            cooldownTimerC = C_COOLDOWN;
        }

        // Cooldown skillC
        if (cooldownTimerC > 0 && !skillCOn) {
            cooldownTimerC -= 1000 / GameManager.getFps();
            System.out.println("cooldownC is " + cooldownTimerC);
            if (cooldownTimerC < 0) {
                cooldownTimerC = 0;
            }
        }
    }

    /** Reset skill X và C. */
    @Override
    public void resetPaddle() {
        super.resetPaddle();
        skillXOn = false;
        skillCOn = false;
    }

    @Override
    public String getPathShort() {
        return PATH_SHORT;
    }

    @Override
    public String getPathLong() {
        return PATH_LONG;
    }

    @Override
    public String getPathDefault() {
        return PATH_DEFAULT;
    }
}

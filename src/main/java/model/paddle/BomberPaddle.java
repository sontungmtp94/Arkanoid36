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
    private final int X_COOLDOWN = 200; //10000
    private final int X_MAX_STACK = 5;
    private final int C_COOLDOWN = 200; //30000

    private boolean skillXOn = false;
    private int stackX = X_MAX_STACK;
    private long cooldownTimerX = 0;

    private boolean skillCOn = false;
    private int cooldownTimerC = 0;

    // 3 path đến 3 sprites của BomberPaddle.
    private final String PATH_DEFAULT = "images/paddles/normal/NormalPaddle_default.png";
    private final String PATH_SHORT = "images/paddles/galaxy/BomberPaddle_short.png";
    private final String PATH_LONG = "images/paddles/galaxy/BomberPaddle_long.png";

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
        skillX = new Skill("Chain Explosion",
                           "Plant up to "
                           + X_MAX_STACK
                           + " bombs that automatically explode."
                           + " Bombs can be triggered by other explosions.",
                           "images/paddles/galaxy/ChainExplosion.png");
        skillC = new Skill("Nuke 'Em",
                           "Plant a Nuke that has impactful explosion.",
                           "images/paddles/galaxy/NukeEm.png");
    }

    @Override
    public void updateSpriteByWidth() {
        if (getWidth() == DEFAULT_WIDTH + 60)
            setAndLoadSprite(PATH_LONG);
        else if (getWidth() == DEFAULT_WIDTH - 60)
            setAndLoadSprite(PATH_SHORT);
        else {
            setAndLoadSprite(PATH_DEFAULT);
        }
    }

    @Override
    public void castSkillX() {
        if (stackX <= 0 || skillXOn || skillCOn) {
            return;
        }

        skillXOn = true;
        stackX--;
        int bombX = x + width / 2 - Bomb.getDefaultWidth() / 2;
        int bombY = y - Bomb.getDefaultHeight();
        Bomb bomb = new Bomb(bombX, bombY);
        GameManager.addProjectile(bomb);
        System.out.println(stackX + " bombs left");
    }

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

    @Override
    public void update() {
        super.update();

        // Nếu không còn bomb đang deploy thì mới reset skillXOn
        if (skillXOn && GameManager.getProjectiles()
                                   .stream().noneMatch(p -> p instanceof Bomb && !((Bomb)p).isDeployed())) {
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
                                   .stream().noneMatch(p -> p instanceof Nuke && !((Nuke)p).isDeployed())) {
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

    @Override
    public void resetPaddle() {
        super.resetPaddle();
        skillXOn = false;
        skillCOn = false;
        sprite = loadSprite(PATH_DEFAULT);
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

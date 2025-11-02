package model.paddle;

import controller.GameManager;
import game.ArkanoidGame;
import model.projectile.Blast;

import view.GameInformation;
import static view.SpritesView.loadSprite;

/**
 * GalaxyPaddle có các kỹ năng:
 * Kỹ năng X: Cho phép di chuyển dọc bằng W/S và được tăng tốc độ trong 3s.
 * Kỹ năng C: Bắn blast lên trên, gây sát thương cho gạch trong phạm vi.
 */
public class GalaxyPaddle extends Paddle {
    private final int X_DURATION = 3000; //3000
    private final int X_COOLDOWN = 200; //20000
    private final double X_SPEED_BOOST = 1.3;
    private boolean skillXOn = false;
    private int remainingDurationX = X_DURATION;
    private int cooldownTimerX = 0;

    private final int C_COOLDOWN = 200; //20000
    private final int C_FREEZE_TIME = Blast.getLifeTime();
    private boolean skillCOn = false;
    private int cooldownTimerC = 0;
    private long startTimeC;

    // 3 path đến 3 sprites của GalaxyPaddle.
    private final String PATH_DEFAULT = "images/paddles/galaxy/GalaxyPaddle_default.png";
    private final String PATH_SHORT = "images/paddles/galaxy/GalaxyPaddle_short.png";
    private final String PATH_LONG = "images/paddles/galaxy/GalaxyPaddle_long.png";

    /**
     * Constructor cho GalaxyPaddle.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public GalaxyPaddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        spritePath = PATH_DEFAULT;
        sprite = loadSprite(spritePath);
    }

    @Override
    public void castSkillX() {
        if (skillXOn || cooldownTimerX > 0) {
            return;
        }

        skillXOn = true;
        remainingDurationX = X_DURATION;
        setSpeed(speed * X_SPEED_BOOST);
    }

    @Override
    public void castSkillC() {
        if (skillCOn || cooldownTimerC > 0) {
            return;
        }

        skillCOn = true;
        startTimeC = System.currentTimeMillis();

        int blastX = getX() + getWidth() / 2 - Blast.getDefaultWidth() / 2;
        int blastY = getY() - Blast.getDefaultHeight();
        GameManager.addProjectile(new Blast(blastX, blastY));

        movingAllowed = false;
    }

    @Override
    public void update() {
        super.update();

        // Khi kích hoạt skill X.
        if (skillXOn) {
            // Giảm dần duration
            remainingDurationX -= 1000 / GameManager.getFps();
            System.out.println("durationX is " + remainingDurationX);

            // Cho phép Paddle di chuyển dọc bằng W/S.
            if (movingAllowed) {
                if (GameManager.keyManager.isUpPressed()) {
                    dy = -speed;
                    y += (int) dy;
                }
                if (GameManager.keyManager.isDownPressed()) {
                    dy = speed;
                    y += (int) dy;
                }
            }

            // Giữ Paddle luôn trong trần và đáy.
            if (y < 0) {
                y = 0;
            } else if (getY() + getHeight()
                       > ArkanoidGame.getGameHeight() - 1
                         - GameInformation.getBarHeight()) {
                y = ArkanoidGame.getGameHeight() - 1
                    - GameInformation.getBarHeight() - getHeight();
            }
        }

        // Hết duration skill X
        if (remainingDurationX <= 0) {
            skillXOn = false;
            cooldownTimerX = X_COOLDOWN;
            speed = DEFAULT_SPEED;
            y = DEFAULT_Y;
            remainingDurationX = X_DURATION;
        }

        // Cooldown skill X.
        if (cooldownTimerX > 0) {
            cooldownTimerX -= 1000 / GameManager.getFps();
            if (cooldownTimerX < 0) {
                cooldownTimerX = 0;
            }
        }

        // Khi kích hoạt skill C.
        if (skillCOn) {
            // Paddle phải đứng yên.
            movingAllowed = false;

            if (System.currentTimeMillis() - startTimeC > C_FREEZE_TIME) {
                skillCOn = false;
                movingAllowed = true;
                cooldownTimerC = C_COOLDOWN;
            }
        }

        // Cooldown skill C.
        if (cooldownTimerC > 0) {
            cooldownTimerC -= 1000 / GameManager.getFps();
            if (cooldownTimerC < 0) {
                cooldownTimerC = 0;
            }
        }
    }

    @Override
    public void resetPaddle() {
        super.resetPaddle();
        skillXOn = false;
        remainingDurationX = X_DURATION;
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

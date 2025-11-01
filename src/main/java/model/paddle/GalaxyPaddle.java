package model.paddle;

import controller.GameManager;
import game.ArkanoidGame;
import model.projectile.Blast;

import static view.SpritesView.loadSprite;

public class GalaxyPaddle extends Paddle {
    private final int X_DURATION = 3000; //3000
    private final int X_COOLDOWN = 200; //20000
    private final double X_SPEED_BOOST = 1.4;
    private boolean skillXOn = false;
    private int remainingDurationX = X_DURATION;
    private int remainingCooldownX = 0;

    private final int C_COOLDOWN = 200; //20000
    private final int C_FREEZE_TIME = Blast.getLifeTime();
    private boolean skillCOn = false;
    private int remainingCooldownC = 0;
    private long skillCStartTime;

    // 3 path đến 3 sprites của GalaxyPaddle.
    private final String PATH_DEFAULT = "images/paddles/galaxy/GalaxyPaddle_default.png";
    private final String PATH_SHORT = "images/paddles/galaxy/GalaxyPaddle_short.png";
    private final String PATH_LONG = "images/paddles/galaxy/GalaxyPaddle_long.png";

    /**
     * Constructor cho GalaxyPaddle.
     *
     * @param x          Tọa độ x (ngang)
     * @param y          Tọa độ y (dọc)
     * @param width      Chiều rộng
     * @param height     Chiều cao
     */
    public GalaxyPaddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        spritePath = PATH_DEFAULT;
        sprite = loadSprite(spritePath);
        skillX = new Skill("Space Travel",
                           "Can move freely and have boosted speed",
                           "images/paddles/galaxy/SpaceTravel.png");
        skillC = new Skill("Supernova Blast",
                           "Shoot an energy beam upwards.",
                           "images/paddles/galaxy/SupernovaBlast.png");
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
        if (skillXOn || remainingCooldownX > 0) {
            return;
        }

        skillXOn = true;
        remainingDurationX = X_DURATION;
        setSpeed(speed * X_SPEED_BOOST);
    }

    @Override
    public void castSkillC() {
        if (skillCOn || remainingCooldownC > 0) {
            return;
        }

        skillCOn = true;
        skillCStartTime = System.currentTimeMillis();

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
            remainingDurationX -= 1000 / 60;
            System.out.println("durationX is " + remainingDurationX);

            // Cho phép Paddle di chuyển dọc bằng W/S.
            if (movingAllowed) {
                if (GameManager.keyManager.isUpPressed()) {
                    setDy(-speed);
                    setY(getY() - (int) getSpeed());
                }
                if (GameManager.keyManager.isDownPressed()) {
                    setDy(speed);
                    setY(getY() + (int) getSpeed());
                }
            }

            // Giữ Paddle luôn trong trần và đáy.
            if (getY() < 0) {
                setY(0);
            } else if (getY() + getHeight() > ArkanoidGame.getGameHeight() - 1) {
                setY(ArkanoidGame.getGameHeight() - getHeight() - 1);
            }
        }

        // Hết duration skill X
        if (remainingDurationX <= 0) {
            skillXOn = false;
            remainingCooldownX = X_COOLDOWN;
            setSpeed(DEFAULT_SPEED);
            setY(DEFAULT_Y);
            remainingDurationX = X_DURATION;
        }

        // Cooldown skill X.
        if (remainingCooldownX > 0) {
            remainingCooldownX -= 1000 / 60;
            System.out.println("cooldownX is " + remainingCooldownX);
        }

        // Khi kích hoạt skill C.
        if (skillCOn) {
            // Paddle phải đứng yên.
            movingAllowed = false;

            if (System.currentTimeMillis() - skillCStartTime > C_FREEZE_TIME) {
                skillCOn = false;
                movingAllowed = true;
                remainingCooldownC = C_COOLDOWN;
            }
        }

        // Cooldown skill C.
        if (remainingCooldownC > 0) {
            remainingCooldownC -= 1000 / 60;
            System.out.println("cooldownC is " + remainingCooldownC);
        }
    }

    @Override
    public void resetPaddle() {
        super.resetPaddle();
        skillXOn = false;
        remainingDurationX = X_DURATION;
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

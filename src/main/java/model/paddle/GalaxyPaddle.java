package model.paddle;

import controller.GameManager;
import game.ArkanoidGame;

import static view.SpritesView.loadSprite;

public class GalaxyPaddle extends Paddle {
    // 3 path đến 3 sprites của GalaxyPaddle.
    private final String PATH_SHORT = "images/paddles/galaxy/GalaxyPaddle_short.png";
    private final String PATH_DEFAULT = "images/paddles/galaxy/GalaxyPaddle_default.png";
    private final String PATH_LONG = "images/paddles/galaxy/GalaxyPaddle_long.png";

    private final int DURATION = 5000; //5000
    private final int COOLDOWN = 1000; //20000

    private boolean activeOn = false;
    private int remainingDuration = DURATION;
    private int remainingCooldown = 0;

    /**
     * Constructor cho Paddle.
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
        passiveSkill = new Skill("Gravitational Field",
                                 "Attract nearby PowerUps",
                                 "images/paddles/galaxy/GravitationalField.png");
        activeSkill = new Skill("Space Travel",
                                "Can move freely and have boosted speed",
                                "images/paddles/galaxy/SpaceTravel.png");
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
    public void applyPassiveSkill() {

    }

    @Override
    public void castActiveSkill() {
        if (activeOn || remainingCooldown > 0) {
            return;
        }

        activeOn = true;
        remainingDuration = DURATION;
        setSpeed(speed * 2);
    }

    @Override
    public void update() {
        super.update();

        // Khi kích hoạt Active
        if (activeOn) {
            // Giảm dần duration
            remainingDuration -= 1000 / 60;
            System.out.println("duration is " + remainingDuration);
            // Cho phép Paddle di chuyển dọc bằng W/S.

            if (GameManager.keyManager.isUpPressed()) {
                setDy(-speed);
                setY(getY() - (int) getSpeed());
            }
            if (GameManager.keyManager.isDownPressed()) {
                setDy(speed);
                setY(getY() + (int) getSpeed());
            }

            // Giữ Paddle luôn trong biên
            if (getY() < 0) {
                setY(0);
            } else if (getY() + getHeight() > ArkanoidGame.getGameHeight()) {
                setY(ArkanoidGame.getGameHeight() - getHeight());
            }
        }

        // Hết duration
        if (remainingDuration <= 0) {
            activeOn = false;
            remainingCooldown = COOLDOWN;
            setSpeed(DEFAULT_SPEED);
            setY(DEFAULT_Y);
            remainingDuration = DURATION;
        }

        // Bắt đầu cooldown
        if (remainingCooldown > 0) {
            remainingCooldown -= 1000/60;
            System.out.println("cooldown is " + remainingCooldown);
        }
    }

    @Override
    public void resetPaddle() {
        super.resetPaddle();
        activeOn = false;
        remainingDuration = DURATION;
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

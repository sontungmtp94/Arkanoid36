package model.paddle;

import controller.GameManager;
import game.ArkanoidGame;
import model.ball.Ball;

import java.awt.*;
import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

public class GalaxyPaddle extends Paddle {
    // 3 path đến 3 sprites của GalaxyPaddle.
    private final String PATH_SHORT = "images/paddles/galaxy/GalaxyPaddle_short.png";
    private final String PATH_DEFAULT = "images/paddles/galaxy/GalaxyPaddle_default.png";
    private final String PATH_LONG = "images/paddles/galaxy/GalaxyPaddle_long.png";

    private final int DURATION = 3000; //3000
    private final int COOLDOWN = 500; //20000

    private boolean skillXOn = false;
    private int remainingDuration = DURATION;
    private int remainingCooldown = 0;

    private int ballHits = 0;
    private BufferedImage blast;

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
        skillX = new Skill("Space Travel",
                                 "Can move freely and have boosted speed",
                                 "images/paddles/galaxy/SpaceTravel.png");
        skillC = new Skill("Solar Blast",
                                 "Shoot an energy beam upwards.",
                                 "images/paddles/galaxy/SolarBlast.png");
        blast = loadSprite("images/paddles/galaxy/lase.png");
    }

    /* @Override
    public void render(Graphics2D g) {
        super.render(g);
        int laserX = getX() + getWidth() / 2 - 25;
        int laserY = getY() - 1170;
        g.drawImage(blast, laserX, laserY, 50, 1170, null);
    }*/

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
        if (skillXOn || remainingCooldown > 0) {
            return;
        }

        skillXOn = true;
        remainingDuration = DURATION;
        setSpeed(speed * 1.5);
    }

    @Override
    public void castSkillC() {

    }

    @Override
    public void update() {
        super.update();

        // Khi kích hoạt Active
        if (skillXOn) {
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

            // Giữ Paddle luôn trong trần và đáy.
            if (getY() < 0) {
                setY(0);
            } else if (getY() + getHeight() > ArkanoidGame.getGameHeight() - 1) {
                setY(ArkanoidGame.getGameHeight() - getHeight() - 1);
            }
        }

        // Hết duration
        if (remainingDuration <= 0) {
            skillXOn = false;
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
        skillXOn = false;
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

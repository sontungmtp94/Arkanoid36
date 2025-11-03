package model.paddle;

import static view.SpritesView.loadSprite;

public class NormalPaddle extends Paddle {
    // 3 path đến 3 sprites của NormalPaddle.
    private final String PATH_SHORT =
            "images/paddles/normal/NormalPaddle_short.png";
    private final String PATH_DEFAULT =
            "images/paddles/normal/NormalPaddle_default.png";
    private final String PATH_LONG =
            "images/paddles/normal/NormalPaddle_long.png";

    /**
     * Constructor cho NormalPaddle.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public NormalPaddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        spritePath = PATH_DEFAULT;
        sprite = loadSprite(spritePath);
    }

    // Không có kỹ năng.
    @Override
    public void castSkillX() {}

    @Override
    public void castSkillC() {}

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

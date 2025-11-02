package view;
import controller.GameManager;

import static view.SpritesView.*;

import java.awt.image.BufferedImage;

public class GameBackground {
    private static final String[] path = new String[7];

    private final BufferedImage background;
    private final BufferedImage informationBar;
    private final BufferedImage heart;
    private final BufferedImage unheart;

    public GameBackground() {
        for (int i = 0; i < 7; i++) {
            path[i] = "images/utils/background_game/background" + i + ".png";
        }
        background = loadSprite(path[(GameManager.getCurrentLevel()) % 7]);
        informationBar = loadSprite("images/utils/informationBar.png");
        heart = loadSprite("images/utils/heart.png");
        unheart = loadSprite("images/utils/unheart.png");
    }

    public BufferedImage getBackground() {
        return background;
    }

    public BufferedImage getInformationBar() {
        return informationBar;
    }

    public BufferedImage getHeart(boolean t) {
        if (t) return heart;
        else return unheart;
    }
}

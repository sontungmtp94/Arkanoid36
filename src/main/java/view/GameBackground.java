package view;
import static view.SpritesView.*;

import java.awt.image.BufferedImage;

public class GameBackground {
    private static final String path = "images/utils/background_game.png";

    private final BufferedImage background;


    public GameBackground() {
        background = loadSprite(path);
    }

    public BufferedImage getBackground() {
        return background;
    }
}

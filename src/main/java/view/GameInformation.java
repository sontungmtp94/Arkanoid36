package view;

import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

public class GameInformation {
    private static final BufferedImage informationBar = loadSprite("images/utils/informationBar.png");;
    private static  final BufferedImage heart = loadSprite("images/utils/heart.png");
    private static final BufferedImage unheart = loadSprite("images/utils/unheart.png");

    public GameInformation() {
    }

    public static BufferedImage getInformationBar() {
        return informationBar;
    }

    public static BufferedImage getHeart(boolean t) {
        if (t) return heart;
        else return unheart;
    }
}

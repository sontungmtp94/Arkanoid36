package view;

import java.awt.image.BufferedImage;

import static view.SpritesView.loadSprite;

/**
 * Lớp GameInformation để hiện thông tin của ván game.
 */
public class GameInformation {
    private static final int BAR_HEIGHT = 41;
    private static final BufferedImage informationBar = loadSprite("images/utils/informationBar.png");
    private static  final BufferedImage heart = loadSprite("images/utils/heart.png");
    private static final BufferedImage unheart = loadSprite("images/utils/unheart.png");

    public GameInformation() {
    }

    /**
     * Lấy đồ họa chứa thông tin trò chơi.
     */
    public static BufferedImage getInformationBar() {
        return informationBar;
    }

    /**
     * Lấy đồ họa vẽ trái tim trò chơi (2 loại: tim đỏ và tim xám).
     */
    public static BufferedImage getHeart(boolean t) {
        if (t) return heart;
        else return unheart;
    }

    /**
     * Lấy chiều cao của thanh đồ họa.
     */
    public static int getBarHeight() {
        return BAR_HEIGHT;
    }
}

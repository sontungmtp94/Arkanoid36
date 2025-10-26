package model.paddle;

import static view.SpritesView.loadSprite;
import java.awt.image.BufferedImage;

/**
 * Lớp Skill đại diện cho kỹ năng của Paddle.
 * Skill gồm tên gọi, mô tả và icon.
 */
public class Skill {
    private final String name;
    private final String description;
    private final BufferedImage icon;
    private final String iconPath;

    /**
     * Constructor cho Skill của Paddle.
     *
     * @param name        Tên kỹ năng
     * @param description Mô tả kỹ năng
     * @param iconPath    Path đến icon
     */
    public Skill(String name, String description, String iconPath) {
        this.name = name;
        this.description = description;
        this.iconPath = iconPath;
        icon = loadSprite(iconPath);
    }

    // Các getter.

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BufferedImage getIcon() {
        return icon;
    }

    public String getIconPath() {
        return iconPath;
    }
}

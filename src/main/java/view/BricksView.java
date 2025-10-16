package view;

import model.brick.BrickType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BricksView {
    public void loadBrickSprites() {
        try {
            BufferedImage spriteSheet = ImageIO.read(new File("src/main/resources/images/brick/bricks.png"));
            for (BrickType type : BrickType.values()) {
                BufferedImage sub = spriteSheet.getSubimage(
                        type.getX(), type.getY(), type.getWidth(), type.getHeight()
                );
                type.setSprite(sub);
            }
            System.out.println("Brick sprites loaded successfully!");
        } catch (IOException e) {
            System.err.println("Failed to load sprites: " + e.getMessage());
        }
    }
}

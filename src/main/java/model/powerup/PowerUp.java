package model.powerup;

import static model.ball.Ball.*;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import model.ball.Ball;
import model.base.MovableObject;
import controller.GameManager;
import javax.swing.*;

public class PowerUp extends MovableObject {
    private int id;
    private int NumsOfPU = 6;
    private String[] FILE_IMAGES = new String[NumsOfPU];
    private BufferedImage image;

    private void loadImage() {
        try {
            image = ImageIO.read(getClass().getResourceAsStream(FILE_IMAGES[id]));
            System.out.println("Loaded image: " + FILE_IMAGES[id]);
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("⚠️ Không thể load ảnh PowerUp id=" + id);
            e.printStackTrace();
            image = null;
        }
    }

    public PowerUp(int x, int y, int w, int h, int id) {
        super(x, y, w, h);
        setDx(0);
        setDy(5);
        this.id = id;

        for(int i = 0; i < NumsOfPU; i++) {
            FILE_IMAGES[i] = "/images/powerups/PU" + i + ".png";
        }
        loadImage();
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean alive() {
        return getY() < 1200 ? true : false;
    }

    @Override
    public void update() {
        setX(getX() + (int)getDx());
        setY(getY() + (int)getDy());
        for(int i = 0; i < GameManager.powerUps.size(); i++) {
            if (GameManager.powerUps.get(i).getBounds().intersects(GameManager.paddle.getBounds())) {
                GameManager.powerUps.get(i).applyEffect(GameManager.powerUps.get(i).getId());
                GameManager.powerUps.remove(i);
                i--;
            }
            // nếu power rơi ra khỏi màn hình
            else if (GameManager.powerUps.get(i).getY() > GameManager.panelHeight) {
                GameManager.powerUps.remove(i);
                i--;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (alive()) {
            if (image != null) {
                g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                // Fallback nếu ảnh không tải được
                g.setColor(Color.GREEN);
                g.fillOval(getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public void applyEffect(int id) {
        switch (id) {
            case 0:
                GameManager.setScore(GameManager.getScore() + 5);
                System.out.println("PowerUp 0: Cong 5 diem");
                break;
            case 1:
                if (GameManager.getPaddle().getWidth() < GameManager.getPaddle().getDefaultWidth() + 50) {
                    GameManager.getPaddle().setWidth(GameManager.getPaddle().getWidth() + 50);
                }
                System.out.println("PowerUp 1: Tang chieu dai Paddle");
                break;
            case 2:
                if (GameManager.getLives() < 5) GameManager.setLives(GameManager.getLives() + 1);
                System.out.println("PowerUp 2: Cong 1 mang");
                break;
            case 3:
                if (GameManager.ball.getDamage() == 1) {
                    GameManager.ball.setAndReloadSpritePath("/images/balls/ball_fire.png");
                    GameManager.ball.setDamage(2);
                }
                System.out.println("PowerUp 3: Gap doi sat thuong cua bong");
                break;
            case 4:
                if (GameManager.ball.getSpeed() == GameManager.ball.getDefaultSpeed()) {
                    //GameManager.ball.setSpeed(GameManager.ball.getSpeed() * 2);
                    GameManager.ball.setDx(GameManager.ball.getDx() * 2);
                    GameManager.ball.setDy(GameManager.ball.getDy() * 2);
                }
                System.out.println("PowerUp 4: Gap doi toc do cua bong");
                break;
            case 5:
                if (GameManager.getPaddle().getWidth() >= GameManager.getPaddle().getDefaultWidth()) {
                    GameManager.getPaddle().setWidth(GameManager.getPaddle().getWidth() - 50);
                }
                System.out.println("PowerUp 5: Giam chieu dai Paddle");
                break;
            default:
                break;
        }
    }
}

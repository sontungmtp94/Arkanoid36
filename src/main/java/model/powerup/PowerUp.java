package model.powerup;

import java.awt.*;
import model.base.MovableObject;
import controller.GameManager;

public class PowerUp extends MovableObject {
    private int id;

    public PowerUp(int x, int y, int w, int h, int id) {
        super(x, y, w, h);
        setDx(0);
        setDy(5);
        this.id = id;
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
    }

    @Override
    public void render(Graphics2D g) {
        if (alive()) {
            g.setColor(Color.GREEN);
            g.fillOval(getX(), getY(), getWidth(), getHeight());
        }
    }

    public void applyEffect(int id) {
        switch (id) {
            case 0:
                GameManager.setScore(GameManager.getScore() + 50);
                break;
            case 1:
                if (GameManager.getPaddle().getWidth() < 230) {
                    GameManager.getPaddle().setWidth(GameManager.getPaddle().getWidth() + 50);
                }
                break;
            case 2:
                if (GameManager.getLives() < 5) GameManager.setLives(GameManager.getLives() + 1);
                break;
            default:
                break;
        }
    }
}

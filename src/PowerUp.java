import java.awt.*;

public class PowerUp extends MovableObject {
    private int id;

    public PowerUp(int x, int y, int w, int h, int id) {
        super(x, y, w, h);
        this.dx = 0;
        this.dy = 5;
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean alive() {
        return this.y < 1200 ? true : false;
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics2D g) {
        if (alive()) {
            g.setColor(Color.GREEN);
            g.fillOval(x, y, width, height);
        }
    }

    public void applyEffect(int id) {
        switch (id) {
            case 0:
                GameManager.point += 50;
                break;
            case 1:
                if (GameManager.paddle.width < 230) {
                    GameManager.paddle.width += 50;
                }
                break;
            case 2:
                if (GameManager.lives < 5) GameManager.lives ++;
                break;
            default:
                break;
        }
    }
}

import java.awt.*;
import java.util.ArrayList;

/**
 * Lớp Ball đại diện cho bóng.
 * Bóng nảy khi va chạm với thanh trượt, gạch, trần và 2 bên biên cửa sổ.
 * Bóng gây sát thương cho gạch.
 */

public class Ball extends MovableObject {
    /** Thời gian delay trước khi bóng tự bay lên khi bắt đầu chơi. */
    private static final int LAUNCH_DELAY_TIME = 60; // 1s trong 60 fps.

    /** Tốc độ mặc định ban đầu của Ball theo 2 chiều. */
    private final double DEFAULT_SPEED = 6.0;

    /** Góc phản xạ khi va chạm từ 60 độ (tại biên) đến 0 độ (tại tâm). */
    private final double MAX_REFLECT_ANGLE = Math.toRadians(60);

    private int delayTimer = LAUNCH_DELAY_TIME;

    /** Lượng sát thương của Ball. */
    private int damage;

    /** Trạng thái bóng đang chờ (bắt dầu chơi) / đang di chuyển */
    private boolean moving = false;

    /** Màu sắc Ball. */
    private Color color;

    /** Paddle và các Brick mà Ball có thể va chạm. */
    private Paddle paddle;
    private ArrayList<Brick> bricks;

    /**
     * Constructor cho Ball.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     * @param damage Sát thương
     * @param color  Màu sắc
     */
    public Ball(int x, int y, int width, int height,
                int damage, Color color) {
        super(x, y, width, height);
        this.damage = damage;
        this.color = color;
        setVelocity(0, 0);
    }

    /** Cập nhật vị trí Ball. */
    @Override
    public void update() {
        // Giai đoạn ban đầu, hết thời gian delay thì Ball bắt đầu di chuyển.
        if (!moving) {
            if (delayTimer > 0) {
                delayTimer--;

                // Đặt Ball căn giữa bên trên Paddle.
                setX(paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2);
                setY(paddle.getY() - getHeight());
            } else {
                // Ball bắt đầu bay lên theo một trong 2 hướng ngẫu nhiên.
                moving = true;
                int direction = Math.random() > 0.5 ? 1 : -1;
                setVelocity(DEFAULT_SPEED * direction, -DEFAULT_SPEED);
            }
            return;
        }

        // Cập nhật vị trí khi di chuyển
        setX(getX() + (int)getDx());
        setY(getY() + (int)getDy());

        // Va chạm với trần và 2 bên biên cửa sổ.
        if (getY() <= 0) {
            setDy(-getDy());
        }

        if (getX() <= 0 || getX() + getWidth() >= ArkanoidGame.WIDTH) {
            setDx(-getDx());
        }

        // Va chạm với Paddle.
        if (paddle != null && getBounds().intersects(paddle.getBounds())) {
            bounce(paddle);
        }

        // Va chạm với Brick.
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()
                    && getBounds().intersects(brick.getBounds())) {
                    bounce(brick);
                    brick.takeHits(damage);
                    break;
                }
            }
        }
    }

    /**
     * Xử lí Ball nảy ra sau khi va chạm với GameObject khác (Paddle và Brick).
     *
     * @param other Đối tượng va chạm
     */
    public void bounce(GameObject other) {
        // Va chạm với Paddle.
        if (other instanceof Paddle p) {
            // Tính tỉ lệ điểm va chạm so với chiều rộng Paddle.
            double ballCenterX = getX() + getWidth() / 2.0;
            double paddleCenterX = p.getX() + p.getWidth() / 2.0;
            double collideRatioX = (ballCenterX - paddleCenterX)
                                   / (p.getWidth() / 2.0);

            // Tạo độ cong ảo của Paddle khi Ball va chạm với Paddle.
            double reflectAngle = collideRatioX * MAX_REFLECT_ANGLE;

            // Giữ nguyên độ lớn vận tốc và đổi hướng theo góc phản xạ.
            double speed = Math.sqrt(getDx() * getDx() + getDy() * getDy());
            setDx(speed * Math.sin(reflectAngle));
            setDy(-speed * Math.cos(reflectAngle));
        }

        // Va chạm với Brick.
        else if (other instanceof Brick br) {
            Rectangle ballRect = getBounds();
            Rectangle brickRect = br.getBounds();

            // Tính phần chồng lấn nhau để xác định hướng va chạm.
            double overlapX = Math.min(ballRect.getMaxX(), brickRect.getMaxX())
                            - Math.max(ballRect.getMinX(), brickRect.getMinX());
            double overlapY = Math.min(ballRect.getMaxY(), brickRect.getMaxY())
                            - Math.max(ballRect.getMinY(), brickRect.getMinY());

            if (overlapX < overlapY) {
                setDx(-getDx()); // Nảy ngang vì va cạnh bên của gạch.
            } else if (overlapX > overlapY) {
                setDy(-getDy()); // Nảy dọc vì va cạnh trên/dưới của gạch.
            } else {
                setVelocity(-getDx(), -getDy()); // Nảy ngược lại vì va vào góc.
            }
        }
    }

    /**
     * Kiểm tra Ball rơi xuống đáy.
     *
     * @return true khi tọa độ y của Ball lớn hơn chiều cao cửa sổ game,
     *         false nếu ngược lại.
     */
    public boolean outOfBottom() {
        return getY() > ArkanoidGame.HEIGHT;
    }

    /**
     * Render Ball lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        g.setColor(getColor());
        g.fillOval(getX(), getY(), getWidth(), getHeight());
    }

    // Các getter và setter

    public int getDelayTimer() {
        return delayTimer;
    }

    public void setDelayTimer(int delayTimer) {
        this.delayTimer = delayTimer;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Paddle getPaddle() {
        return paddle;
    }

    public void setPaddle(Paddle paddle) {
        this.paddle = paddle;
    }

    public ArrayList<Brick> getBricks() {
        return bricks;
    }

    public void setBricks(ArrayList<Brick> bricks) {
        this.bricks = bricks;
    }
}

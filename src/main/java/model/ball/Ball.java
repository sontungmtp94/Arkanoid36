package model.ball;

import game.ArkanoidGame;
import model.brick.*;
import model.paddle.*;
import model.base.*;
import model.powerup.*;
import static view.SpritesView.*;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Lớp Ball đại diện cho bóng.
 * Bóng nảy khi va chạm với thanh trượt, gạch, trần và 2 bên biên cửa sổ.
 * Bóng gây sát thương cho gạch.
 */

public class Ball extends MovableObject {
    /** Kích thước mặc định của Ball. */
    private static final int DEFAULT_SIZE = 15;

    /** Lượng sát thương mặc định của Ball. */
    private static final int DEFAULT_DAMAGE = 1;

    /** Tốc độ mặc định của Ball theo 2 chiều. */
    private static final double DEFAULT_SPEED = 6.0;

    /** Tốc độ hiện tại của bóng. */
    private double speed = DEFAULT_SPEED;

    /** Góc phản xạ khi va chạm từ 60 độ (tại biên) đến 0 độ (tại tâm). */
    private final double MAX_REFLECT_ANGLE = Math.toRadians(60);

    /** Góc ngắm bắn khi ball ở trên paddle */
    private double aimAngle = 0;
    private boolean increasing = true;
    private static final double AIM_SPEED = 2;
    private static final double MAX_LAUNCH_ANGLE = 60;

    /** Lượng sát thương của Ball. */
    private int damage;

    /** Trạng thái bóng đang chờ (bắt dầu chơi) / đang di chuyển */
    private boolean moving = false;

    /** Sprite của Ball. */
    private BufferedImage ballSprite;
    private static String SPRITE_PATH = "images/balls/ball_default.png";

    /** Paddle và các Brick mà Ball có thể va chạm. */
    private Paddle paddle;
    private ArrayList<Brick> bricks;
    private boolean bounceBrick = true;


    /** Chia nhỏ di chuyển thành các steps nhỏ hơn. **/
    private static final int MAX_MOVE_STEPS = 4;

    /**
     * Constructor cho Ball.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param size   Kích thước Ball
     */
    public Ball(int x, int y, int size) {
        super(x, y, size, size);
        damage = DEFAULT_DAMAGE;
        setVelocity(0, 0);
        ballSprite = loadSprite(SPRITE_PATH);
    }

    public void launch() {
        moving = true;
        double rad = Math.toRadians(aimAngle);
        setDx(speed * Math.cos(rad - Math.PI / 2));
        setDy(speed * Math.sin(rad - Math.PI / 2));
    }

    /** Cập nhật vị trí Ball. */
    @Override
    public void update() {
        // Giai đoạn ban đầu, người chơi căn hướng phóng bóng.
        if (!moving) {
            setX(paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2);
            setY(paddle.getY() - getHeight());

            if (increasing) {
                aimAngle += AIM_SPEED;
                if (aimAngle >= MAX_LAUNCH_ANGLE) increasing = false;
            } else {
                aimAngle -= AIM_SPEED;
                if (aimAngle <= -MAX_LAUNCH_ANGLE) increasing = true;
            }
        }

        // Cập nhật vị trí khi di chuyển sử dụng CCD bằng cách chia steps.
        double totalDx = getDx();
        double totalDy = getDy();
        double distance = Math.sqrt(totalDx * totalDx + totalDy * totalDy);

        int steps = (int) Math.ceil(distance / 5.0);
        steps = Math.min(steps, MAX_MOVE_STEPS);

        double stepDx = totalDx / steps;
        double stepDy = totalDy / steps;

        double posX = getX();
        double posY = getY();

        for (int i = 0; i < steps; i++) {
            posX += stepDx;
            posY += stepDy;
            setX((int) posX);
            setY((int) posY);
        }


        // Va chạm với trần.
        if (getY() <= 0) {
            // Đẩy xuống dưới trần để tránh kẹt.
            setY(0);
            setDy(-getDy());
        }

        // Va chạm 2 biên cửa sổ.
        if (getX() <= 0) {
            // Ball vào biên trái, đẩy ra ngoài để tránh kẹt.
            setX(0);
            setDx(-getDx());
        } else if (getX() + getWidth() >= ArkanoidGame.getGameWidth() - 1) {
            // Ball vào biên phải, đẩy ra ngoài để tránh kẹt.
            setX(ArkanoidGame.getGameWidth() - getWidth() - 1);
            setDx(-getDx());
        }

        // Va chạm với Paddle.
        if (paddle != null && getBounds().intersects((Rectangle2D) paddle.getBounds())) {
            bounce(paddle);
        }

        // Va chạm với Brick.
        if (bricks != null) {
            for (Brick brick : bricks) {
                if (!brick.isDestroyed()
                        && getBounds().intersects((Rectangle2D) brick.getBounds())) {
                    if (bounceBrick) {
                        bounce(brick);
                        brick.takeHits(damage);
                        audio.SoundManager.get().playSfx(audio.SoundId.SFX_HIT);
                    } else {
                        brick.takeHits(36);
                        audio.SoundManager.get().playSfx(audio.SoundId.SFX_HIT);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Xử lí Ball nảy ra sau khi va chạm với Paddle và Brick.
     *
     * @param other Đối tượng va chạm
     */
    public void bounce(GameObject other) {
        Ellipse2D ballRect = (Ellipse2D) getBounds();
        // Va chạm với Paddle.
        if (other instanceof Paddle p) {
            Rectangle2D paddleRect = (Rectangle2D) p.getBounds();

            // Tính phần chồng lấn nhau để xác định hướng va chạm.
            double overlapX = Math.min(ballRect.getMaxX(), paddleRect.getMaxX())
                    - Math.max(ballRect.getMinX(), paddleRect.getMinX());
            double overlapY = Math.min(ballRect.getMaxY(), paddleRect.getMaxY())
                    - Math.max(ballRect.getMinY(), paddleRect.getMinY());

            // Va vào cạnh bên của Paddle.
            if (overlapX < overlapY) {
                // Đẩy ra khỏi 2 mép Paddle để tránh kẹt.
                if (ballRect.getCenterX() < paddleRect.getCenterX()) {
                    // Đẩy sang trái.
                    setX((int) (paddleRect.getX() - getWidth()));
                } else {
                    // Đẩy sang phải.
                    setX((int) (paddleRect.getX() + paddleRect.getWidth()));
                }

                setDx(-getDx()); // Ball bật ngược
            }

            // Va vào mặt trên của Paddle.
            else if (overlapX > overlapY) {
                // Đẩy lên trên mặt Paddle để tránh kẹt.
                setY((int) paddleRect.getY() - getHeight());

                // Tính tỉ lệ điểm va chạm của Ball so với chiều rộng Paddle.
                double ballContactX;
                if (getX() + getWidth() / 2.0 < p.getX()) {
                    // Tâm Ball nằm ngoài mép trái Paddle, lấy mép phải của Ball.
                    ballContactX = getX() + getWidth();
                } else if (getX() + getWidth() / 2.0 > p.getX() + p.getWidth()) {
                    // Tâm Ball nằm ngoài mép phải Paddle, lấy mép trái của Ball.
                    ballContactX = getX();
                } else {
                    // Tâm Ball nằm trên mặt Paddle, lấy luôn tâm.
                    ballContactX = getX() + getWidth() / 2.0;
                }
                double paddleCenterX = p.getX() + p.getWidth() / 2.0;
                double collideRatioX = (ballContactX - paddleCenterX)
                        / (p.getWidth() / 2.0);
                collideRatioX = Math.max(-1, Math.min(collideRatioX, 1));

                // Tạo độ cong ảo của Paddle khi Ball va chạm với Paddle.
                double reflectAngle = collideRatioX * MAX_REFLECT_ANGLE;

                // Giữ nguyên độ lớn vận tốc và đổi hướng theo góc phản xạ.
                double speed = Math.sqrt(getDx() * getDx() + getDy() * getDy());
                setDx(speed * Math.sin(reflectAngle));
                setDy(-speed * Math.cos(reflectAngle));
            }

            // Va vào đúng góc.
            else {
                setVelocity(-getDx(), -getDy());
            }
        }

        // Va chạm với Brick.
        else if (other instanceof Brick br) {
            Rectangle2D brickRect = (Rectangle2D) br.getBounds();

            // Tính phần chồng lấn nhau để xác định hướng va chạm.
            double overlapX = Math.min(ballRect.getMaxX(), brickRect.getMaxX())
                    - Math.max(ballRect.getMinX(), brickRect.getMinX());
            double overlapY = Math.min(ballRect.getMaxY(), brickRect.getMaxY())
                    - Math.max(ballRect.getMinY(), brickRect.getMinY());

            // Va vào 2 cạnh bên của Brick.
            if (overlapX < overlapY) {
                // Đẩy ra khỏi 2 mép Brick để tránh kẹt.
                if (ballRect.getCenterX() < brickRect.getCenterX()) {
                    // Đẩy sang trái.
                    setX((int) (brickRect.getX() - getWidth()));
                } else {
                    // Đẩy sang phải.
                    setX((int) (brickRect.getX() + brickRect.getWidth()));
                }

                setDx(-getDx()); // Ball bật ngược
            }

            // Va vào cạnh trên/dưới của Brick.
            else if (overlapX > overlapY) {
                setDy(-getDy());
            }

            // Va vào góc.
            else {
                setVelocity(-getDx(), -getDy());
            }
        }
    }

    @Override
    public Shape getBounds() {
        return new Ellipse2D.Double(x, y, width, height);
    }

    /**
     * Kiểm tra Ball rơi xuống đáy.
     *
     * @return true khi tọa độ y của Ball lớn hơn chiều cao cửa sổ game,
     *         false nếu ngược lại.
     */
    public boolean outOfBottom() {
        return getY() >= ArkanoidGame.getGameHeight();
    }

    /**
     * Reset Ball về trạng thái ban đầu.
     */
    public void resetBall() {
        setX(paddle.getX() + paddle.getWidth() / 2 - getWidth() / 2);
        setY(paddle.getY() - getHeight());
        setVelocity(0, 0);
        setDamage(DEFAULT_DAMAGE);
        setSpeed(DEFAULT_SPEED);
        moving = false;
    }

    /**
     * Render Ball lên màn hình.
     *
     * @param g Dùng để render
     */
    @Override
    public void render(Graphics2D g) {
        g.drawImage(ballSprite, getX(), getY(), getWidth(), getHeight(), null);

        // Nếu bóng chưa bay, vẽ mũi tên ngắm
        if (!moving) {
            int centerX = getX() + getWidth() / 2;
            int centerY = getY();
            double rad = Math.toRadians(aimAngle);
            int arrowLength = 60;

            int endX = centerX + (int) (arrowLength * Math.cos(rad - Math.PI / 2));
            int endY = centerY + (int) (arrowLength * Math.sin(rad - Math.PI / 2));

            g.setColor(Color.YELLOW);
            g.setStroke(new BasicStroke(2));
            g.drawLine(centerX, centerY, endX, endY);

            // đầu mũi tên nhỏ
            g.fillOval(endX - 3, endY - 3, 6, 6);
        }

    }

    // Các getter và setter

    public static int getDefaultSize() {
        return DEFAULT_SIZE;
    }

    public static double getDefaultSpeed() {
        return DEFAULT_SPEED;
    }

    public static int getDefaultDamage() {
        return DEFAULT_DAMAGE;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double s) {
        this.speed = s;
    }

    public double getMaxReflectAngle() {
        return MAX_REFLECT_ANGLE;
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

    public String getSpritePath() {
        return SPRITE_PATH;
    }

    public void setAndReloadSpritePath(String spritePath) {
        SPRITE_PATH = spritePath;
        this.ballSprite = loadSprite(SPRITE_PATH);
    }

    public void setBounceBrick(boolean bounceBrick) {
        this.bounceBrick = bounceBrick;
    }
}

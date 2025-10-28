package model.ball;

import game.ArkanoidGame;
import model.brick.*;
import model.paddle.*;
import model.base.*;
import model.powerup.*;
import controller.GameManager;
import static view.SpritesView.*;

import java.awt.*;
import java.awt.image.BufferedImage;
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
    private final double DEFAULT_SPEED = 7.0;

    /** Tốc độ hiện tại của bóng. */
    private double speed = DEFAULT_SPEED;

    /** Góc phản xạ khi va chạm từ 60 độ (tại biên) đến 0 độ (tại tâm). */
    private final double MAX_REFLECT_ANGLE = Math.toRadians(60);

    /** Góc ngắm bắn khi ball ở trên paddle */
    private double aimAngle = 0;
    private boolean increasing = true;
    private static final double AIM_SPEED = 2;
    private static final double MAX_LAUNCH_ANGLE = 60;


    /** Lượng sát thương mặc định của Ball. */
    private final int DEFAULT_DAMAGE = 1;

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
        setVelocity(0, 0);
        ballSprite = loadSprite(SPRITE_PATH);
    }

    public void launch(){
        moving = true;
        double rad = Math.toRadians(aimAngle);
        setDx(speed * Math.cos(rad - Math.PI / 2));
        setDy(speed * Math.sin(rad - Math.PI / 2));

    }

    /** Cập nhật vị trí Ball. */
    @Override
    public void update() {
        // Giai đoạn ban đầu, hết thời gian delay thì Ball bắt đầu di chuyển.
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
        // Cập nhật vị trí khi di chuyển
        setX(getX() + (int) getDx());
        setY(getY() + (int) getDy());

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
        } else if (getX() + getWidth() >= ArkanoidGame.getGameWidth()) {
            // Ball vào biên phải, đẩy ra ngoài để tránh kẹt.
            setX(ArkanoidGame.getGameWidth() - getWidth());
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
                    if(bounceBrick) {
                        bounce(brick);
                        brick.takeHits(damage);
                        audio.SoundManager.get().playSfx(audio.SoundId.SFX_HIT);
                    } else {
                        brick.takeHits(1);
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
        Rectangle ballRect = getBounds();
        // Va chạm với Paddle.
        if (other instanceof Paddle p) {
            Rectangle paddleRect = p.getBounds();

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
            Rectangle brickRect = br.getBounds();

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

    public static int getLaunchDelayTime() {
        return LAUNCH_DELAY_TIME;
    }

    public double getDefaultSpeed() {
        return DEFAULT_SPEED;
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

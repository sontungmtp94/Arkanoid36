package model.powerup;

import java.awt.*;
import javax.imageio.ImageIO;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Timer;

import controller.GameState;
import model.ball.Ball;
import model.base.MovableObject;
import controller.GameManager;
import model.brick.Brick;
import model.paddle.Paddle;

public class PowerUp extends MovableObject {
    private static final int PADDLE_WIDTH_CHANGE = 60;
    private int id;
    public static int numsOfPU = 12;
    private String[] FILE_IMAGES = new String[numsOfPU];
    private BufferedImage image;

    // === Biến static để quản lý hiệu ứng Paddle mở rộng ===
    private static Map<Integer, Timer> timers = new HashMap<>();
    private static Map<Integer, Integer> remainingTimes = new HashMap<>(); // giây còn lại

    private static String activeMessage = null;
    public static long messageStartTime = 0;
    public static final int MESSAGE_DURATION_MS = 2000; // hiển thị 2 giây

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

    public static String getActiveMessage() {
        if (activeMessage == null) return null;
        long now = System.currentTimeMillis();
        if (now - messageStartTime > MESSAGE_DURATION_MS) {
            activeMessage = null; // hết thời gian
        }
        return activeMessage;
    }

    private static void showMessage(String text) {
        activeMessage = text;
        messageStartTime = System.currentTimeMillis();
    }

    public PowerUp(int x, int y, int w, int h, int id) {
        super(x, y, w, h);
        setDx(0);
        setDy(5);
        this.id = id;

        for (int i = 0; i < numsOfPU; i++) {
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

    public static void createPowerUp(Brick brick, double probability) {
        if (Math.random() < probability) {
            int idPower = (int) (Math.random() * numsOfPU);
            PowerUp newP = new PowerUp(0, 0, 30, 30, idPower);
            newP.setX(brick.getX() + brick.getWidth() / 2 - brick.getWidth() / 2);
            newP.setY(brick.getY() + brick.getHeight() / 2 - newP.getHeight() / 2);
            GameManager.powerUps.add(newP);
        }
    }

    @Override
    public void update() {
        setX(getX() + (int)getDx());
        setY(getY() + (int)getDy());
        for (int i = 0; i < GameManager.powerUps.size(); i++) {
            if (GameManager.powerUps.get(i).getBounds().intersects((Rectangle2D) GameManager.getPaddle().getBounds())) {
                GameManager.powerUps.get(i).applyEffect(GameManager.powerUps.get(i).getId());
                GameManager.powerUps.remove(i);
                i--;
            }
            // Nếu power rơi ra khỏi màn hình
            else if (GameManager.powerUps.get(i).getY() > GameManager.getPanelHeight()) {
                GameManager.powerUps.remove(i);
                i--;
            }
        }
    }

    @Override
    public Shape getBounds() {
        return new Rectangle2D.Double(x, y, width, height);
    }

    @Override
    public void render(Graphics2D g) {
        if (alive()) {
            if (image != null) {
                g.drawImage(image, getX(), getY(), getWidth(), getHeight(), null);
            } else {
                g.setColor(Color.GREEN);
                g.fillOval(getX(), getY(), getWidth(), getHeight());
            }
        }
    }

    public void countdown(int id, int duration, Runnable onFinish) {
        // Nếu đã có timer cho ID này, reset lại thời gian
        if (timers.containsKey(id) && timers.get(id) != null) {
            remainingTimes.put(id, duration);
            System.out.println("🔁 Reset thời gian PowerUp ID " + id + " về " + duration + "s");
            return;
        }

        // Nếu chưa có timer -> tạo mới
        remainingTimes.put(id, duration);
        Timer t = new Timer(1000, e -> {
            if(GameManager.getGameState() == GameState.PLAYING) {
                int remain = remainingTimes.get(id) - 1;
                remainingTimes.put(id, remain);

                if (remain <= 0) {
                    ((Timer) e.getSource()).stop();
                    timers.put(id, null);
                    remainingTimes.put(id, 0);

                    System.out.println("⏰ Hết thời gian PowerUp ID " + id);
                    if (onFinish != null) onFinish.run();
                } else {
                    System.out.println("PowerUp " + id + " còn " + remain + "s");
                }
            }
        });

        timers.put(id, t);
        t.start();
        System.out.println("▶ Bắt đầu đếm PowerUp ID " + id + " (" + duration + "s)");
    }

    public void applyEffect(int id) {
        switch (id) {
            case 0:
                GameManager.setScore(GameManager.getScore() + 50);
                showMessage("+50 điểm!");
                break;

            case 1:
                showMessage("Tăng chiều dài Paddle");

                if (GameManager.paddle.getWidth() < Paddle.getDefaultWidth() + PADDLE_WIDTH_CHANGE) {
                    GameManager.paddle.setWidth(GameManager.paddle.getWidth() + PADDLE_WIDTH_CHANGE);
                }
                GameManager.paddle.updateSpriteByWidth();
                if (timers.containsKey(5) && timers.get(5) != null) {
                    cancelEffect(5);
                } else {
                    countdown(1, 30, () -> {
                        cancelEffect(1);
                    });
                }
                break;

            case 2:
                if (GameManager.getLives() < 5)
                    GameManager.setLives(GameManager.getLives() + 1);
                showMessage("+1 mạng");
                break;

            case 3:
                cancelEffect(8);
                cancelEffect(10);
                for(Ball b : GameManager.balls) {
                    if (b.getDamage() == 1 && b.getHeight() == Ball.getDefaultSize()) {
                        b.setAndReloadSpritePath("/images/balls/ball_extended.png");
                        double multi = 1.5;
                        b.setDamage(2);
                        b.setHeight((int) (Ball.getDefaultSize() * multi));
                        b.setWidth((int) (Ball.getDefaultSize() * multi));
                    }
                }
                countdown(3, 30, () -> {
                    cancelEffect(3);
                });
                showMessage("Tăng kích cỡ bóng");
                break;

            case 4:
                double multi = 0.5;
                if (Math.abs(GameManager.getPaddle().getSpeed()) == GameManager.getPaddle().getDefaultSpeed()) {
                    GameManager.getPaddle().setSpeed(GameManager.getPaddle().getSpeed() * multi);
                }
                countdown(4, 30, () -> {
                    cancelEffect(4);
                });
                showMessage("Giảm tốc độ Paddle");
                break;

            case 5:
                if (GameManager.paddle.getWidth() > Paddle.getDefaultWidth() - PADDLE_WIDTH_CHANGE) {
                    GameManager.paddle.setWidth(GameManager.paddle.getWidth() - PADDLE_WIDTH_CHANGE);
                }
                GameManager.paddle.updateSpriteByWidth();
                if (timers.containsKey(1) && timers.get(1) != null) {
                    cancelEffect(1);
                } else {
                    countdown(5, 30, () -> {
                        cancelEffect(5);
                    });
                }
                showMessage("Giảm chiều dài Paddle");
                break;

            case 6:
                GameManager.getPaddle().setSpeed(GameManager.getPaddle().getSpeed() * -1);
                if (timers.containsKey(6) && timers.get(6) != null) {
                    cancelEffect(6);
                } else {
                    countdown(6, 30, () -> {
                        cancelEffect(6);
                    });
                }
                showMessage("Đảo ngược Paddle");
                break;

            case 7:
                Ball b1 = new Ball(0, 0, Ball.getDefaultSize());
                b1.setPaddle(GameManager.getPaddle());
                b1.setBricks(GameManager.getBricks());
                b1.setAndReloadSpritePath("images/balls/ball_default.png");
                b1.resetBall();
                b1.launch();
                GameManager.balls.add(b1);
                if (timers.get(3) != null) {
                    int timeleft = remainingTimes.get(3);
                    applyEffect(3);
                    remainingTimes.put(3, timeleft);
                }
                else if (timers.get(8) != null) {
                    int timeleft = remainingTimes.get(8);
                    applyEffect(8);
                    remainingTimes.put(8, timeleft);
                }
                else if (timers.get(10) != null) {
                    int timeleft = remainingTimes.get(10);
                    applyEffect(10);
                    remainingTimes.put(10, timeleft);
                }
                showMessage("Thêm 1 quả bóng");
                break;

            case 8:
                cancelEffect(3);
                cancelEffect(10);

                for(Ball b : GameManager.balls) {
                    b.setBounceBrick(false);
                    b.setAndReloadSpritePath("images/balls/ball_fire.png");
                }
                countdown(8, 5, () -> {
                    cancelEffect(8);
                });
                showMessage("Bóng lửa");
                break;

            case 9:
                cancelAllEffects();
                showMessage("Hủy hết mọi PowerUp đang có");
                break;

            case 10:
                cancelEffect(3);
                cancelEffect(8);

                // Nếu Paddle chưa đủ dài thì tăng
                for(Ball b : GameManager.balls) {
                    b.setAndReloadSpritePath("images/balls/ball_invisible.png");
                }
                countdown(10, 3, () -> {
                    cancelEffect(10);
                });
                showMessage("Bóng tàng hình");
                break;
            case 11:
                applyEffect((int) (Math.random() * numsOfPU));
                break;

            default:
                break;
        }
    }

    /** Hàm gọi khi bóng rơi hoặc Game Over để hủy hiệu ứng */
    public static void cancelEffect(int id) {
        if (!remainingTimes.containsKey(id)) return;

        Timer t = timers.get(id);
        if (t != null) {
            try {
                t.stop();
                timers.put(id, null);
            } catch (Exception ex) {
                System.err.println("Timer null hoặc đã dừng cho PowerUp ID " + id);
            }
        }

        remainingTimes.put(id, 0);
        System.out.println("Hủy hiệu ứng PowerUp ID " + id);

        // Nếu giữ sprite thì chỉ reset logic, không đổi sprite
        switch (id) {
            case 1:
            case 5: // Paddle quay về chiều dài ban đầu
                GameManager.getPaddle().setWidth(Paddle.getDefaultWidth());
                GameManager.paddle.updateSpriteByWidth();
                break;

            case 3: // Bóng to
                for (Ball b : GameManager.balls) {
                    if (b.getDamage() > 1) b.setDamage(b.getDamage() / 2);
                    b.setHeight(Ball.getDefaultSize());
                    b.setWidth(Ball.getDefaultSize());
                    b.setAndReloadSpritePath("images/balls/ball_default.png");
                }
                break;

            case 4:
                if (Math.abs(GameManager.getPaddle().getSpeed()) < GameManager.getPaddle().getDefaultSpeed()) {
                    GameManager.getPaddle().setSpeed((int) (GameManager.getPaddle().getSpeed() * 2));
                }
                break;

            case 6:
                if (GameManager.getPaddle().getSpeed() < 0) {
                    GameManager.getPaddle().setSpeed(GameManager.getPaddle().getSpeed() * -1);
                }
                break;

            case 8: // Bóng lửa
                for (Ball b : GameManager.balls) {
                    b.setBounceBrick(true);
                    b.setAndReloadSpritePath("images/balls/ball_default.png");
                }
                break;

            case 10: // Bóng tàng hình
                for (Ball b : GameManager.balls) {
                    b.setAndReloadSpritePath("images/balls/ball_default.png");
                }
                break;
            default:
                break;
        }
    }


    /** Hàm gọi khi bóng rơi hoặc Game Over để hủy hiệu ứng */
    public static void cancelAllEffects() {
//        for (Integer id : remainingTimes.keySet()) {
//            if(timers.containsKey(id) && timers.get(id) != null) {
//                timers.get(id).stop();
//                timers.put(id, null);
//                remainingTimes.put(id, 0);
//            }
//        }
        for (Integer id : remainingTimes.keySet()) {
            cancelEffect(id);
        }
        System.out.println("Hủy toàn bộ hiệu ứng PowerUp.");
    }

    public static Map<Integer, Integer> getRemainingTimes() {
        return remainingTimes;
    }

    public static Map<Integer, Timer> getTimers() {
        return timers;
    }
}

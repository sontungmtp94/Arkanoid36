package model.powerup;

import java.awt.*;
import javax.imageio.ImageIO;
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
    private int id;
    public static int numsOfPU = 12;
    private String[] FILE_IMAGES = new String[numsOfPU];
    private BufferedImage image;

    // === Biến static để quản lý hiệu ứng Paddle mở rộng ===
    private static Map<Integer, Timer> timers = new HashMap<>();
    private static Map<Integer, Integer> remainingTimes = new HashMap<>(); // giây còn lại

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
            if (GameManager.powerUps.get(i).getBounds().intersects(GameManager.getPaddle().getBounds())) {
                GameManager.powerUps.get(i).applyEffect(GameManager.powerUps.get(i).getId());
                GameManager.powerUps.remove(i);
                i--;
            }
            // Nếu power rơi ra khỏi màn hình
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
        timers.put(id, new Timer(1000, e -> {
            // if (GameManager.getGameState() == GameState.PLAYING) {
            int remain = remainingTimes.get(id) - 1;
            remainingTimes.put(id, remain);
            // }

            if (remainingTimes.get(id) <= 0) {
                if (onFinish != null) onFinish.run();
                timers.get(id).stop();
                timers.put(id, null);
                System.out.println("Hết thời gian PowerUp ID " + id);
            } else {
                System.out.println("PowerUp " + id + " còn " + remainingTimes.get(id) + "s");
            }
        }));

        //timers.put(id, t);
        timers.get(id).start();
        System.out.println("Bắt đầu đếm PowerUp ID " + id + " (" + duration + "s)");
    }

    public void applyEffect(int id) {
        switch (id) {
            case 0:
                GameManager.setScore(GameManager.getScore() + 50);
                System.out.println("PowerUp 0: +50 điểm");
                break;

            case 1:
                System.out.println("PowerUp 1: Tăng chiều dài Paddle (30s)");

                if (GameManager.paddle.getWidth() < Paddle.getDefaultWidth() + 60) {
                    GameManager.paddle.setWidth(GameManager.paddle.getWidth() + 60);
                    GameManager.paddle.setAndLoadSprite("images/paddles/galaxy/GalaxyPaddle_long.png");
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
                System.out.println("PowerUp 2: +1 mạng");
                break;

            case 3:
                cancelEffect(8);
                cancelEffect(10);
                for(Ball b : GameManager.balls) {
                    if (b.getDamage() == 1) {
                        b.setAndReloadSpritePath("/images/balls/ball_extended.png");
                        b.setDamage(b.getDamage() * 2);
                        b.setHeight(25);
                        b.setWidth(25);
                    }
                }
                countdown(3, 30, () -> {
                    cancelEffect(3);
                });
                System.out.println("PowerUp 3: Tăng kích cỡ bóng");
                break;

            case 4:
                double multi = 2;
                if (GameManager.getPaddle().getSpeed() == GameManager.getPaddle().getDefaultSpeed()) {
                    GameManager.getPaddle().setSpeed(GameManager.getPaddle().getSpeed() * multi);
                }
                countdown(4, 30, () -> {
                    cancelEffect(4);
                });
                System.out.println("PowerUp 4: Gấp đôi tốc độ Paddle");
                break;

            case 5:
                System.out.println("PowerUp 5: Giảm chiều dài Paddle");
                if (GameManager.paddle.getWidth() > Paddle.getDefaultWidth() - 60) {
                    GameManager.paddle.setWidth(GameManager.paddle.getWidth() - 60);
                    GameManager.paddle.setAndLoadSprite("images/paddles/galaxy/GalaxyPaddle_short.png");
                }
                GameManager.paddle.updateSpriteByWidth();
                if (timers.containsKey(1) && timers.get(1) != null) {
                    cancelEffect(1);
                } else {
                    countdown(5, 30, () -> {
                        cancelEffect(1);
                    });
                }
                break;

            case 6:
                System.out.println("PowerUp 6: Đảo ngược Paddle");
                GameManager.getPaddle().setSpeed(GameManager.getPaddle().getSpeed() * -1);
                if (timers.containsKey(6) && timers.get(6) != null) {
                    cancelEffect(6);
                } else {
                    countdown(6, 30, () -> {
                        cancelEffect(4);
                    });
                }
                break;

            case 7:
                System.out.println("PowerUp 7: Thêm 1 quả bóng");
                Ball b1 = new Ball(0, 0, 15, 15, 1, Color.BLACK);
                b1.setPaddle(GameManager.getPaddle());
                b1.setBricks(GameManager.getBricks());
                b1.setAndReloadSpritePath("images/balls/ball_default.png");
                b1.resetBall();
                b1.launch();
                GameManager.balls.add(b1);
                break;

            case 8:
                cancelEffect(3);
                cancelEffect(10);
                System.out.println("PowerUp 8: Bóng lửa (5s)");

                // Nếu Paddle chưa đủ dài thì tăng
                for(Ball b : GameManager.balls) {
                    b.setBounceBrick(false);
                    b.setAndReloadSpritePath("images/balls/ball_fire.png");
                }
                countdown(8, 5, () -> {
                    cancelEffect(8);
                });
                break;

            case 9:
                System.out.println("PowerUp 9: Hủy hết mọi PowerUp đang có");
                cancelAllEffects();
                break;

            case 10:
                cancelEffect(3);
                cancelEffect(8);
                System.out.println("PowerUp 10: Bóng tàng hình (3s)");

                // Nếu Paddle chưa đủ dài thì tăng
                for(Ball b : GameManager.balls) {
                    b.setAndReloadSpritePath("images/balls/ball_invisible.png");
                }
                countdown(10, 3, () -> {
                    cancelEffect(10);
                });
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
            } catch (Exception ex) {
                System.err.println("Timer null hoặc đã dừng cho PowerUp ID " + id);
            }
        }

        remainingTimes.put(id, 0);
        System.out.println("Hủy hiệu ứng PowerUp ID " + id);

        // Nếu giữ sprite thì chỉ reset logic, không đổi sprite
        switch (id) {
            case 1: // Paddle quay về chiều dài ban đầu
                GameManager.getPaddle().setWidth(Paddle.getDefaultWidth());
                GameManager.paddle.setAndLoadSprite("images/paddles/galaxy/GalaxyPaddle_default.png");
                break;

            case 3: // Bóng to
                for (Ball b : GameManager.balls) {
                    if (b.getDamage() > 1) b.setDamage(b.getDamage() / 2);
                    b.setHeight(15);
                    b.setWidth(15);
                    b.setAndReloadSpritePath("images/balls/ball_default.png");
                }
                break;

            case 4:
                if (GameManager.getPaddle().getSpeed() != GameManager.getPaddle().getDefaultSpeed()) {
                    GameManager.getPaddle().setSpeed(GameManager.getPaddle().getDefaultSpeed());
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
        for (Integer id : remainingTimes.keySet()) {
            remainingTimes.put(id, 0);
        }
        System.out.println("Hủy toàn bộ hiệu ứng PowerUp.");
    }
}

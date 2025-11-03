package model.ball;

import model.brick.BrickType;
import model.paddle.Paddle;
import model.brick.Brick;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test đơn giản: chỉ kiểm tra va chạm (collision) giữa Ball và Paddle/Brick.
 */
class BallCollisionTest {

    private Ball ball;
    private Paddle paddle;
    private Brick brick;

    /** Paddle giả vì Paddle là abstract */
    static class TestPaddle extends Paddle {
        public TestPaddle(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        @Override
        public void update() { }

        @Override
        public void castSkillC() {

        }

        @Override
        public void castSkillX() {

        }

        @Override
        public String getPathShort() {
            return "";
        }

        @Override
        public String getPathLong() {
            return "";
        }

        @Override
        public String getPathDefault() {
            return "";
        }
    }

    /** Brick giả (nếu Brick là abstract) */
    static class TestBrick extends Brick {
        public TestBrick(int x, int y, int width, int height, BrickType type) {
            super(x, y, width, height, type);
        }
    }

    @BeforeEach
    void setUp() {
        paddle = new TestPaddle(100, 600, 200, 20);
        brick = new TestBrick(200, 200, 60, 30, BrickType.RED);
        ball = new Ball(150, 580, Ball.getDefaultSize());
    }

    @Test
    void testBounceWithPaddle() {
        ball.setPaddle(paddle);
        ball.setMoving(true);
        ball.setDy(4);
        ball.setX(paddle.getX() + paddle.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(paddle.getY() - ball.getHeight());

        ball.bounce(paddle);

        assertTrue(ball.getDy() < 0, "Ball phải bật ngược lên khi chạm paddle");
    }

    @Test
    void testBounceWithBrick() {
        ball.setMoving(true);
        ball.setDy(-4);
        ball.setX(brick.getX() + brick.getWidth() / 2 - ball.getWidth() / 2);
        ball.setY(brick.getY() + brick.getHeight());

        ball.bounce(brick);

        assertTrue(ball.getDy() > 0, "Ball phải bật xuống khi chạm brick");
    }
}

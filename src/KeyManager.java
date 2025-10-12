import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager extends JPanel implements KeyListener {

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean pausePressed;

    /** Xử lý khi nhấn phím. */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            pausePressed = !pausePressed;
        }
    }

    /** Xử lý khi nhả phím. */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_RIGHT) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            pausePressed = !pausePressed;
        }
    }

    /** Không dùng đến */
    @Override
    public void keyTyped(KeyEvent e) {
    }


    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isPausePressed() {
        return pausePressed;
    }

}

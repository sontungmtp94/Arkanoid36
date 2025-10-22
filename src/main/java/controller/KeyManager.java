package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean leftPressed;
    private boolean rightPressed;
    private boolean pausePressed;
    private boolean restartPressed;
    private boolean nextLevelPressed;
    private boolean ballReleased;

    /** Xử lý khi nhấn phím. */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_ESCAPE -> pausePressed = true;
            case KeyEvent.VK_R -> restartPressed = true;
            case KeyEvent.VK_N -> nextLevelPressed = true;
            case KeyEvent.VK_SPACE -> ballReleased = true;
        }

    }

    /** Xử lý khi nhả phím. */
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT, KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_RIGHT, KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_R -> restartPressed = false;
            case KeyEvent.VK_N -> nextLevelPressed = false;
            case KeyEvent.VK_SPACE -> ballReleased = false;
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

    public boolean isRestartPressed() {
        return restartPressed;
    }

    public boolean isNextLevelPressed() {
        return nextLevelPressed;
    }

    public boolean isBallReleased() {
        return ballReleased;
    }

    public void clearPause() {
        pausePressed = false;
    }


}

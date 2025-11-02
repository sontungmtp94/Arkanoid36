package controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {

    private boolean upPressed;
    private boolean downPressed;
    private boolean leftPressed;
    private boolean rightPressed;
    private boolean skillXPressed;
    private boolean skillCPressed;
    private boolean arrowUpPressed;
    private boolean arrowDownPressed;
    private boolean arrowLeftPressed;
    private boolean arrowRightPressed;
    private boolean deployPressed;
    private boolean pausePressed;
    private boolean restartPressed;
    private boolean nextLevelPressed;
    private boolean ballReleased;

    /** Xử lý khi nhấn phím. */
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W -> upPressed = true;
            case KeyEvent.VK_S -> downPressed = true;
            case KeyEvent.VK_A -> leftPressed = true;
            case KeyEvent.VK_D -> rightPressed = true;
            case KeyEvent.VK_X -> skillXPressed = true;
            case KeyEvent.VK_C -> skillCPressed = true;
            case KeyEvent.VK_UP -> arrowUpPressed = true;
            case KeyEvent.VK_DOWN -> arrowDownPressed = true;
            case KeyEvent.VK_LEFT -> arrowLeftPressed = true;
            case KeyEvent.VK_RIGHT -> arrowRightPressed = true;
            case KeyEvent.VK_ENTER -> deployPressed = true;
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
            case KeyEvent.VK_W -> upPressed = false;
            case KeyEvent.VK_S -> downPressed = false;
            case KeyEvent.VK_A -> leftPressed = false;
            case KeyEvent.VK_D -> rightPressed = false;
            case KeyEvent.VK_X -> skillXPressed = false;
            case KeyEvent.VK_C -> skillCPressed = false;
            case KeyEvent.VK_UP -> arrowUpPressed = false;
            case KeyEvent.VK_DOWN -> arrowDownPressed = false;
            case KeyEvent.VK_LEFT -> arrowLeftPressed = false;
            case KeyEvent.VK_RIGHT -> arrowRightPressed = false;
            case KeyEvent.VK_ENTER -> deployPressed = false;
            case KeyEvent.VK_R -> restartPressed = false;
            case KeyEvent.VK_N -> nextLevelPressed = false;
            case KeyEvent.VK_SPACE -> ballReleased = false;
        }
    }

    /** Không dùng đến */
    @Override
    public void keyTyped(KeyEvent e) {
    }

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isDownPressed() {
        return downPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }

    public boolean isSkillXPressed() {
        return skillXPressed;
    }

    public boolean isSkillCPressed() {
        return skillCPressed;
    }

    public boolean isArrowUpPressed() {
        return arrowUpPressed;
    }

    public boolean isArrowDownPressed() {
        return arrowDownPressed;
    }

    public boolean isArrowLeftPressed() {
        return arrowLeftPressed;
    }

    public boolean isArrowRightPressed() {
        return arrowRightPressed;
    }

    public boolean isDeployPressed() {
        return deployPressed;
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

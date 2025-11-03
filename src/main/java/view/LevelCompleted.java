package view;

import javax.swing.*;
import java.awt.Image;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;

import controller.GameManager;
import audio.SoundManager;
import audio.SoundId;
import controller.GameState;
import game.ArkanoidGame;
import java.util.List;
import java.util.ArrayList;
import view.LeaderBoard;


/**
 * Lớp LevelCompleted hiển thị thông báo "Level Completed" và hướng dẫn next level/ restart.
 */
public class LevelCompleted extends JPanel {
    private final GameManager gameManager;

    public LevelCompleted(int panelWidth, int panelHeight, GameManager gameManager) {
        this.gameManager = gameManager;

        setLayout(null);
        setOpaque(false);

        BufferedImage winningImage = SpritesView.loadSprite("images/utils/you_win.png");
        BufferedImage mainMenuBtn = SpritesView.loadSprite("images/utils/main_menu.png");
        BufferedImage restartBtn = SpritesView.loadSprite("images/utils/restart.png");
        BufferedImage nextLevelBtn = SpritesView.loadSprite("images/utils/next_level.png");

        Image scaledLabel = winningImage.getScaledInstance(panelWidth, -1, Image.SCALE_SMOOTH);
        Image scaledMainMenu = mainMenuBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledRestart = restartBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);
        Image scaledNextLevel = nextLevelBtn.getScaledInstance(panelWidth / 10, -1, Image.SCALE_SMOOTH);

        JLabel winningLabel = new JLabel(new ImageIcon(scaledLabel));
        JButton mainMenuButton = new JButton(new ImageIcon(scaledMainMenu));
        JButton restartButton = new JButton(new ImageIcon(scaledRestart));
        JButton nextLevelButton = new JButton(new ImageIcon(scaledNextLevel));

        int labelHeight = scaledLabel.getHeight(null);
        int buttonWidth = scaledMainMenu.getWidth(null);
        int buttonHeight = scaledMainMenu.getHeight(null);

        int spacing = 60;

        int startX = (panelWidth - 3 * buttonWidth - 2 * spacing) / 2;

        winningLabel.setBounds(0, panelHeight / 2 - labelHeight, panelWidth, labelHeight);
        mainMenuButton.setBounds(startX, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        restartButton.setBounds(startX + buttonWidth + spacing, panelHeight / 2 + 20, buttonWidth, buttonHeight);
        nextLevelButton.setBounds(startX + 2 * (buttonWidth + spacing), panelHeight / 2 + 20, buttonWidth, buttonHeight);


        mainMenuButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            gameManager.stopGame();
            ((ArkanoidGame) SwingUtilities.getWindowAncestor(this)).changeState(GameState.MENU);
        });

        restartButton.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            gameManager.restartGame();
        });

        nextLevelButton.addActionListener(e -> {SoundManager.get().playSfx(SoundId.SFX_CLICK);
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            gameManager.nextLevel();
        });

        add(winningLabel);
        add(mainMenuButton);
        add(restartButton);
        add(nextLevelButton);

        setVisible(false);
    }

    /** Ghi lại thông tin người chơi và trạng thái mở khóa */
    protected static void updateDataPlayer() {
        try {
            File file = new File("src/main/resources/DataPlayer.txt");
            List<String> lines = new ArrayList<>();

            if (file.exists()) {
                Scanner sc = new Scanner(file, "UTF-8");
                while (sc.hasNextLine()) lines.add(sc.nextLine());
                sc.close();
            }

            while (lines.size() < 2) lines.add("");

            StringBuilder unlock = new StringBuilder();
            for (int i = 0; i < LevelChoose.unlock.length; i++) {
                unlock.append(LevelChoose.unlock[i]).append(" ");
            }

            lines.set(1, unlock.toString());

            try (PrintWriter pw = new PrintWriter(file, "UTF-8")) {
                for (String line : lines) pw.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unlockNewLevel() {
        // Mở khóa màn tiếp theo.
        if(GameManager.getCurrentLevel() < 20) {
            if (LevelChoose.unlock[GameManager.getCurrentLevel()] == 0) {
                LevelChoose.unlock[GameManager.getCurrentLevel()] = 1;
            }
        }

        //Cập nhật file DataPlayer.txt
        LevelCompleted.updateDataPlayer();
    }

    /** Hiển thị bảng Level Completed. */
    public void showPanel() {
        setVisible(true);
        repaint();
        SoundManager.get().playSfx(SoundId.SFX_WIN);

        //In ra điểm và cập nhật leaderboard
        System.out.println(GameManager.playerName + ": " + GameManager.getScore());

        try {
            LeaderBoard.updateLeaderboard(GameManager.playerName, GameManager.getScore());
            System.out.println("Leaderboard updated for " + GameManager.playerName);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật LeaderBoard:");
            e.printStackTrace();
        }
    }

    /** Ẩn bảng Level Completed. */
    public void hidePanel() {
        setVisible(false);
    }
}

package game;

import controller.GameManager;
import view.*;
import audio.SoundManager;
import audio.SoundId;

import javax.swing.*;
import java.awt.*;

/**
 * Enum ScreenSwitcher đại diện cho các màn hình khác nhau của trò chơi.
 * Giúp chuyển đổi giữa MainMenu, Game, LevelCompleted, GameOver, Pause.
 */
public enum ScreenSwitcher {
    MAIN_MENU {
        @Override
        public void show(ArkanoidGame game) {
            SoundManager.get().playBgm(SoundId.BGM_MENU, 1200);
            JPanel panel = new MainMenu(GAME);
            switchPanel(game, panel);
        }
    },

    GAME {
        @Override
        public void show(ArkanoidGame game) {
            SoundManager.get().playBgm(SoundId.BGM_GAME, 1200);
            JPanel panel = new GameManager(ArkanoidGame.getGameWidth(), ArkanoidGame.getGameHeight(), game);
            panel.setPreferredSize(new Dimension(ArkanoidGame.getGameWidth(), ArkanoidGame.getGameHeight()));
            switchPanel(game, panel);
            panel.requestFocusInWindow();
        }
    },

    LEVEL_COMPLETED {
        @Override
        public void show(ArkanoidGame game) {
            JPanel panel = new LevelCompleted(
                    ArkanoidGame.getGameWidth(),
                    ArkanoidGame.getGameHeight(),
                    null // GameManager sẽ tự truyền nếu cần
            );
            switchPanel(game, panel);
            SoundManager.get().playSfx(SoundId.SFX_WIN);
        }
    },

    GAME_OVER {
        @Override
        public void show(ArkanoidGame game) {
            JPanel panel = new GameOver(
                    ArkanoidGame.getGameWidth(),
                    ArkanoidGame.getGameHeight(),
                    null // GameManager sẽ tự truyền nếu cần
            );
            switchPanel(game, panel);
            SoundManager.get().playSfx(SoundId.SFX_LOSE);
        }
    },

    PAUSE {
        @Override
        public void show(ArkanoidGame game) {
            // Tạm placeholder, khi bạn có class Paused sẽ thay vào đây.
            JPanel panel = new JPanel();
            panel.setBackground(new Color(0, 0, 0, 128));
            JLabel lbl = new JLabel("Game Paused", SwingConstants.CENTER);
            lbl.setFont(new Font("Arial", Font.BOLD, 40));
            lbl.setForeground(Color.WHITE);
            panel.setLayout(new BorderLayout());
            panel.add(lbl, BorderLayout.CENTER);
            switchPanel(game, panel);
        }
    };

    /** Hàm trừu tượng để hiển thị mỗi loại màn hình. */
    public abstract void show(ArkanoidGame game);

    /** Hàm tiện ích thay thế panel hiện tại bằng panel mới. */
    protected void switchPanel(ArkanoidGame game, JPanel newPanel) {
        Container content = game.getContentPane();
        content.removeAll();
        content.add(newPanel);
        content.revalidate();
        content.repaint();
    }
}

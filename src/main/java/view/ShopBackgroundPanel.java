package view;

import controller.GameState;
import controller.GameManager;
import game.ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/**
 * Cửa hàng background: người chơi dùng điểm để mở khóa background.
 * Hai background đầu (2 & 3) là miễn phí.
 */

public class ShopBackgroundPanel extends JPanel {
    private final ArkanoidGame game;
    private final String[] backgrounds = {
            "/images/utils/background_game/background0.png",
            "/images/utils/background_game/background1.png",
            "/images/utils/background_game/background2.png",
            "/images/utils/background_game/background3.png",
            "/images/utils/background_game/background4.png",
            "/images/utils/background_game/background5.png",
            "/images/utils/background_game/background6.png",
            "/images/utils/background_game/background7.png"
    };

    private final Set<Integer> unlocked = new HashSet<>();
    private final File unlockFile;


    public ShopBackgroundPanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);
        setBackground(Color.BLACK);

        // File riêng cho từng người chơi
        String player = GameManager.playerName == null ? "default" : GameManager.playerName;
        this.unlockFile = new File("unlocked_backgrounds_" + player + ".txt");

        loadUnlocked();
        initUI();
    }

    /** Đọc danh sách background đã mở từ file */
    private void loadUnlocked() {
        unlocked.add(0); // background2
        unlocked.add(1); // background3 (free)
        if (unlockFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(unlockFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    unlocked.add(Integer.parseInt(line.trim()));
                }
            } catch (IOException ignored) {}
        }
    }

    /** Ghi danh sách background đã mở */
    private void saveUnlocked() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(unlockFile))) {
            for (int id : unlocked) pw.println(id);
        } catch (IOException ignored) {}
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("SELECT BACKGROUND", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 1200, 60);
        add(lblTitle);

        String player = GameManager.playerName;
        JLabel lblPoints = new JLabel("Your Points: " + LeaderBoard.getScoreByName(player));
        lblPoints.setFont(new Font("IntelOne Display Bold", Font.PLAIN, 22));
        lblPoints.setForeground(Color.YELLOW);
        lblPoints.setBounds(40, 85, 400, 30);
        add(lblPoints);

        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBounds(100, 130, 1000, 400);
        grid.setOpaque(false);
        add(grid);

        for (int i = 0; i < backgrounds.length; i++) {
            final int index = i;
            ImageIcon icon = new ImageIcon(getClass().getResource(backgrounds[i]));
            Image scaled = icon.getImage().getScaledInstance(240, 130, Image.SCALE_SMOOTH);

            JLabel label = new JLabel(new ImageIcon(scaled));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            int price = (i < 2) ? 0 : 50 + (i - 2) * 20;
            String text = unlocked.contains(i)
                    ? "Unlocked"
                    : (i < 2 ? "Free" : "Price: " + price);

            JLabel lblPrice = new JLabel(text, SwingConstants.CENTER);
            lblPrice.setForeground(Color.WHITE);
            lblPrice.setFont(new Font("Arial", Font.PLAIN, 16));

            JPanel item = new JPanel(new BorderLayout());
            item.setOpaque(false);
            item.add(label, BorderLayout.CENTER);
            item.add(lblPrice, BorderLayout.SOUTH);

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String playerName = GameManager.playerName;
                    int playerPoints = LeaderBoard.getScoreByName(playerName);

                    if (unlocked.contains(index)) {
                        GameBackground.setCurrentBackground(backgrounds[index]);
                        JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                "Background selected!");
                        game.changeState(GameState.SHOP);
                        return;
                    }

                    // Nếu chưa mua
                    if (price == 0) {
                        unlocked.add(index);
                        saveUnlocked();
                        lblPrice.setText("Unlocked");
                        JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                "Background unlocked (Free)!");
                        return;
                    }

                    // Nếu đủ điểm để mua
                    if (playerPoints >= price) {
                        int confirm = JOptionPane.showConfirmDialog(
                                ShopBackgroundPanel.this,
                                "Buy this background for " + price + " points?",
                                "Confirm Purchase",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            LeaderBoard.addScore(playerName, -price);
                            unlocked.add(index);
                            saveUnlocked();
                            lblPrice.setText("Unlocked");
                            lblPoints.setText("Your Points: " + LeaderBoard.getScoreByName(playerName));
                            JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                    "Purchased successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                "Not enough points!");
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    label.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                }
            });

            grid.add(item);
        }

        JButton btnBack = new JButton("BACK");
        btnBack.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 32));
        btnBack.setBounds(500, 560, 180, 60);
        btnBack.addActionListener(e -> game.changeState(GameState.SHOP));
        add(btnBack);
    }
}

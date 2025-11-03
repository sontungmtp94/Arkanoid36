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
 * Cửa hàng Paddle: người chơi có thể mua paddle bằng điểm từ LeaderBoard.
 * Paddle đầu tiên miễn phí, hai paddle còn lại giá 100 mỗi cái.
 */

public class ShopPaddlePanel extends JPanel {
    private final ArkanoidGame game;

    private final String[] paddles = {
            "/images/paddles/normal/NormalPaddle_short.png",
            "/images/paddles/normal/NormalPaddle_default.png",
            "/images/paddles/normal/NormalPaddle_long.png"
    };

    private final Set<Integer> unlocked = new HashSet<>();
    private final File unlockFile;
    private Image backgroundMenu;

    public ShopPaddlePanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);
        setBackground(Color.BLACK);

        backgroundMenu = new ImageIcon(getClass().getResource("/images/utils/background_menu.png")).getImage();

        // File riêng cho từng người chơi
        String player = GameManager.playerName == null ? "default" : GameManager.playerName;
        this.unlockFile = new File("unlocked_backgrounds_" + player + ".txt");

        loadUnlocked();
        initUI();
    }

    /** Đọc danh sách paddle đã mở khóa */
    private void loadUnlocked() {
        unlocked.add(0); // Paddle đầu tiên miễn phí
        if (unlockFile.exists()) {
            try (BufferedReader br = new BufferedReader(new FileReader(unlockFile))) {
                String line;
                while ((line = br.readLine()) != null) {
                    unlocked.add(Integer.parseInt(line.trim()));
                }
            } catch (IOException ignored) {}
        }
    }

    /** Ghi danh sách paddle đã mở khóa */
    private void saveUnlocked() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(unlockFile))) {
            for (int id : unlocked) pw.println(id);
        } catch (IOException ignored) {}
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("SELECT PADDLE", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 48));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 20, 1200, 60);
        add(lblTitle);

        String player = GameManager.playerName;
        JLabel lblPoints = new JLabel("Your Points: " + LeaderBoard.getScoreByName(player));
        lblPoints.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 22));
        lblPoints.setForeground(Color.YELLOW);
        lblPoints.setBounds(40, 90, 400, 30);
        add(lblPoints);

        JPanel grid = new JPanel(new GridLayout(1, 3, 15, 10)); // nhỏ gap lại cho cân ảnh
        grid.setBounds(150, 200, 900, 150);
        grid.setOpaque(false);
        add(grid);

        for (int i = 0; i < paddles.length; i++) {
            final int index = i;
            ImageIcon icon = new ImageIcon(getClass().getResource(paddles[i]));
            Image img = icon.getImage();

            JPanel label = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(img, -1, -1, getWidth() + 2, getHeight() + 2, this); // phủ kín viền
                }
            };
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            label.setOpaque(true);

            int price = (i == 0) ? 0 : 100;
            String text = unlocked.contains(i)
                    ? "Unlocked"
                    : (price == 0 ? "Free" : "Price: " + price);

            JLabel lblPrice = new JLabel(text, SwingConstants.CENTER);
            lblPrice.setForeground(Color.WHITE);
            lblPrice.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 18));

            JPanel item = new JPanel(new BorderLayout());
            item.setOpaque(false);
            item.add(label, BorderLayout.CENTER);
            item.add(lblPrice, BorderLayout.SOUTH);

            label.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    String playerName = GameManager.playerName;
                    int playerPoints = LeaderBoard.getScoreByName(playerName);

                    // Nếu đã mở → chọn paddle
                    if (unlocked.contains(index)) {
                        PaddleAssets.setCurrentPaddle(paddles[index]);
                        JOptionPane.showMessageDialog(ShopPaddlePanel.this,
                                "Paddle selected!");
                        game.changeState(GameState.SHOP);
                        return;
                    }

                    // Nếu miễn phí → mở luôn
                    if (price == 0) {
                        unlocked.add(index);
                        saveUnlocked();
                        lblPrice.setText("Unlocked");
                        JOptionPane.showMessageDialog(ShopPaddlePanel.this,
                                "Unlocked free paddle!");
                        return;
                    }

                    // Nếu đủ điểm để mua
                    if (playerPoints >= price) {
                        int confirm = JOptionPane.showConfirmDialog(
                                ShopPaddlePanel.this,
                                "Buy this paddle for " + price + " points?",
                                "Confirm Purchase",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            LeaderBoard.addScore(playerName, -price);
                            unlocked.add(index);
                            saveUnlocked();
                            lblPrice.setText("Unlocked");
                            lblPoints.setText("Your Points: " + LeaderBoard.getScoreByName(playerName));
                            JOptionPane.showMessageDialog(ShopPaddlePanel.this,
                                    "Purchased successfully!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ShopPaddlePanel.this,
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
        btnBack.setBounds(520, 480, 180, 60);
        btnBack.addActionListener(e -> game.changeState(GameState.SHOP));
        add(btnBack);
    }

    /** Vẽ nền background_menu.png */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundMenu != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backgroundMenu, 0, 0, getWidth(), getHeight(), this);
        }
    }
}


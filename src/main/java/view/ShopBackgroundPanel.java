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
 * Hai background đầu là miễn phí.
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
    private final Image backgroundMenu;

    public ShopBackgroundPanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);

        backgroundMenu = new ImageIcon(getClass().getResource("/images/utils/background_menu.png")).getImage();

        /** File riêng cho từng người chơi */
        String playerId = GameManager.getPlayerId();
        File dir = new File("src/main/resources/players/" + playerId);
        if (!dir.exists()) dir.mkdirs();
        this.unlockFile = new File(dir, "shop_background.txt");

        loadUnlocked();
        initUI();
    }

    /** Đọc danh sách background đã mở từ file */
    private void loadUnlocked() {
        unlocked.add(0);
        unlocked.add(1);
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
        JLabel lblPoints = new JLabel("YOUR POINTS: " + LeaderBoard.getScoreByName(player));
        lblPoints.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 26));
        lblPoints.setForeground(Color.YELLOW);
        lblPoints.setBounds(40, 90, 500, 30);
        add(lblPoints);

        JPanel grid = new JPanel(new GridLayout(2, 4, 8, 8));
        grid.setBounds(100, 130, 1000, 400);
        grid.setOpaque(false);
        add(grid);

        for (int i = 0; i < backgrounds.length; i++) {
            final int index = i;
            ImageIcon icon = new ImageIcon(getClass().getResource(backgrounds[i]));
            Image img = icon.getImage();

            JPanel label = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2.drawImage(img, -1, -1, getWidth() + 2, getHeight() + 2, this); // phủ tràn viền
                }
            };
            label.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            label.setOpaque(true);

            int price = (i < 2) ? 0 : 50 + (i - 2) * 20;
            String text = unlocked.contains(i)
                    ? "UNLOCKED"
                    : (i < 2 ? "FREE" : "PRICE: " + price);

            JLabel lblPrice = new JLabel(text, SwingConstants.CENTER);
            lblPrice.setForeground(Color.WHITE);
            lblPrice.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 20));

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
                        JOptionPane.showMessageDialog(
                                ShopBackgroundPanel.this,
                                "BACKGROUND SELECTED!",
                                "SHOP",
                                JOptionPane.INFORMATION_MESSAGE);
                        game.changeState(GameState.SHOP);
                        return;
                    }

                    /** Nếu chưa mua */
                    if (price == 0) {
                        unlocked.add(index);
                        saveUnlocked();
                        lblPrice.setText("UNLOCKED");
                        JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                "BACKGROUND UNLOCKED (FREE)!");
                        return;
                    }

                    /** Nếu đủ điểm để mua */
                    if (playerPoints >= price) {
                        int confirm = JOptionPane.showConfirmDialog(
                                ShopBackgroundPanel.this,
                                "BUY THIS BACKGROUND FOR " + price + " POINTS?",
                                "CONFIRM PURCHASE",
                                JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            LeaderBoard.addScore(playerName, -price);
                            unlocked.add(index);
                            saveUnlocked();
                            lblPrice.setText("UNLOCKED");
                            lblPoints.setText("YOUR POINTS: " + LeaderBoard.getScoreByName(playerName));
                            JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                    "PURCHASED SUCCESSFULLY!");
                        }
                    } else {
                        JOptionPane.showMessageDialog(ShopBackgroundPanel.this,
                                "NOT ENOUGH POINTS!");
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
        btnBack.setBounds(500, 565, 180, 60);
        btnBack.setForeground(Color.WHITE);
        btnBack.setFocusPainted(false);
        btnBack.setContentAreaFilled(false);
        btnBack.setBorderPainted(true);
        btnBack.setOpaque(false);
        btnBack.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
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

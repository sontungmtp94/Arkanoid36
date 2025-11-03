package view;

import controller.GameManager;
import controller.GameState;
import game.ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

/**
 * Màn hình chọn level.
 */
public class LevelChoose extends JPanel {
    private final ArkanoidGame game;
    private int selectedLevel = -1;
    private Image backgroundImg;
    private Image levelImg1;
    private Image levelImg2;
    private Image levelHoverImg;
    public static int[] unlock = new int[20];

    /**
     * Khởi tạo LevelChoose.
     */
    public LevelChoose(int width, int height, ArkanoidGame game) {
        this.game = game;

        setLayout(null);
        setOpaque(true);
        setPreferredSize(new Dimension(width, height));

        // === Load ảnh ===
        BufferedImage bg = SpritesView.loadSprite("images/utils/background_menu.png");
        BufferedImage bar1 = SpritesView.loadSprite("images/utils/levelbar.png");
        BufferedImage bar2 = SpritesView.loadSprite("images/utils/unlevelbar.png");
        if (bg != null) backgroundImg = bg.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        if (bar1 != null) {
            levelImg1 = bar1.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            levelHoverImg = brighten(bar1, 1.4f).getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        }
        if (bar2 != null) {
            levelImg2 = bar2.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        }

        // === Đọc dữ liệu level từ file ===
        try (Scanner sc = new Scanner(new File("src/main/resources/DataPlayer.txt"))) {
            // Bỏ qua dòng đầu
            if (sc.hasNextLine()) sc.nextLine();

            // Đọc dòng thứ 2
            if (sc.hasNextLine()) {
                String line = sc.nextLine();
                Scanner lineScanner = new Scanner(line);
                for (int i = 0; i < unlock.length && lineScanner.hasNextInt(); i++) {
                    unlock[i] = lineScanner.nextInt();
                }
                lineScanner.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // === Tiêu đề ===
        JLabel title = new JLabel("CHOOSE YOUR LEVEL", SwingConstants.CENTER);
        title.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 60));
        title.setForeground(Color.WHITE);
        title.setBounds(0, 50, width, 60);
        add(title);

        // === Tạo lưới nút 5x4 ===
        int rows = 4, cols = 5;
        int buttonW = 80, buttonH = 80;
        int gapX = 20, gapY = 20;
        int gridWidth = cols * buttonW + (cols - 1) * gapX;
        int gridHeight = rows * buttonH + (rows - 1) * gapY;
        int startX = (width - gridWidth) / 2;
        int startY = (height - gridHeight) / 2;

        for (int i = 1; i <= 20; i++) {
            int row = (i - 1) / cols;
            int col = (i - 1) % cols;
            int x = startX + col * (buttonW + gapX);
            int y = startY + row * (buttonH + gapY);

            JButton btn = new JButton(String.valueOf(i));
            btn.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 50));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            if (unlock[i-1] == 1) btn.setIcon(new ImageIcon(levelImg1));
            else btn.setIcon(new ImageIcon(levelImg2));
            btn.setHorizontalTextPosition(SwingConstants.CENTER);
            btn.setMargin(new Insets(0, 0, 0, 0));
            btn.setPreferredSize(new Dimension(buttonW, buttonH));
            btn.setMinimumSize(new Dimension(buttonW, buttonH));
            btn.setMaximumSize(new Dimension(buttonW, buttonH));
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            int levelNum = i;
            if(unlock[i-1] == 1) {
                btn.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (selectedLevel != levelNum)
                            btn.setIcon(new ImageIcon(levelHoverImg));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if (selectedLevel != levelNum)
                            btn.setIcon(new ImageIcon(levelImg1));
                    }

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        selectedLevel = levelNum;
                        highlightSelected(levelNum);
                        GameManager.setCurrentLevel(levelNum);

                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(LevelChoose.this);
                        // Chuyển sang game trước
                        game.changeState(GameState.READY);

                        // Sau khi GameManager đã được add vào frame, tìm instance và đổi level
                        SwingUtilities.invokeLater(() -> {
                            if (frame != null && frame.getContentPane().getComponentCount() > 0) {
                                Component c = frame.getContentPane().getComponent(0);
                                if (c instanceof GameManager gm) {
                                    gm.setCurrentLevel(levelNum);   // đổi currentLevel
                                    gm.restartGame();               // reload map
                                }
                            }
                        });
                    }
                });
            }
                btn.setBounds(x, y, buttonW, buttonH);
                add(btn);
        }

        // === Nút Main Menu ===
        JButton backBtn = new JButton("MAIN MENU");
        backBtn.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 36));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(true);
        backBtn.setBackground(new Color(0, 0, 120));
        backBtn.setForeground(Color.WHITE);
        backBtn.setBounds(width / 2 - 100, height - 90, 200, 50);
        backBtn.addActionListener(e -> game.changeState(GameState.MENU));
        add(backBtn);
    }

    /**
     * Vẽ nền full cửa sổ.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImg != null) {
            g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    /**
     * Tăng độ sáng ảnh (hover).
     */
    private BufferedImage brighten(BufferedImage src, float factor) {
        BufferedImage img = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < src.getHeight(); y++) {
            for (int x = 0; x < src.getWidth(); x++) {
                int rgba = src.getRGB(x, y);
                Color c = new Color(rgba, true);
                int r = Math.min((int) (c.getRed() * factor), 255);
                int g = Math.min((int) (c.getGreen() * factor), 255);
                int b = Math.min((int) (c.getBlue() * factor), 255);
                img.setRGB(x, y, new Color(r, g, b, c.getAlpha()).getRGB());
            }
        }
        return img;
    }

    /**
     * Làm nổi bật nút được chọn.
     */
    private void highlightSelected(int selectedLevel) {
        for (Component c : getComponents()) {
            if (c instanceof JButton btn && btn.getText().matches("\\d+")) {
                int num = Integer.parseInt(btn.getText());
                if (num == selectedLevel) {
                    btn.setIcon(new ImageIcon(levelHoverImg));
                    btn.setForeground(Color.YELLOW);
                } else {
                    btn.setIcon(new ImageIcon(levelImg1));
                    btn.setForeground(Color.WHITE);
                }
            }
        }
    }
}

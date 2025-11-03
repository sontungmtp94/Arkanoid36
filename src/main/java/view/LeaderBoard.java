package view;

import controller.GameState;
import game.ArkanoidGame;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Màn hình Leaderboard hiển thị nền + 1 ảnh tổng hợp bảng điểm.
 * Nút Main Menu ở góc phải dưới, tiêu đề ở giữa phía trên.
 */
public class LeaderBoard extends JPanel {
    private final ArkanoidGame game;
    private Image backgroundImg;
    private Image boardImg;

    private static final ArrayList<String> names = new ArrayList<>();
    private static final ArrayList<Integer> scores = new ArrayList<>();

    /**
     * Đường dẫn đến file lưu thông tin bảng xếp hạng.
     */
    private static final File SAVE_FILE = new File("src/main/resources/LeaderBoard.txt");

    /**
     * Khởi tạo LeaderBoard.
     */
    public LeaderBoard(int width, int height, ArkanoidGame game) {
        this.game = game;

        setLayout(null);
        setOpaque(true);
        setPreferredSize(new Dimension(width, height));

        // === Load nền và bảng điểm ===
        BufferedImage bg = SpritesView.loadSprite("images/utils/background_game.png");
        BufferedImage full = SpritesView.loadSprite("images/utils/leaderboardBar.png");

        if (bg != null)
            backgroundImg = bg.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        if (full != null)
            boardImg = full; // Giữ nguyên tỉ lệ, vẽ giữa màn hình

        //Đọc file
        loadLeaderboard();

        //Nút Main Menu
        JButton backBtn = new JButton("MENU");
        backBtn.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 36));
        backBtn.setFocusPainted(false);
        backBtn.setContentAreaFilled(true);
        backBtn.setBackground(new Color(0, 0, 120));
        backBtn.setForeground(Color.WHITE);
        int btWidth = 120;
        int btHeight = 40;
        backBtn.setBounds(width - btWidth, height - btHeight, btWidth, btHeight);
        backBtn.addActionListener(e -> game.changeState(GameState.MENU));
        add(backBtn);
    }

    /**
     * Đọc dữ liệu LeaderBoard.txt từ thư mục resources.
     */
    protected static void loadLeaderboard() {
        names.clear();
        scores.clear();
        try {
            if (!SAVE_FILE.exists()) {
                SAVE_FILE.getParentFile().mkdirs();
                try (PrintWriter pw = new PrintWriter(SAVE_FILE, StandardCharsets.UTF_8)) {
                    for (int i = 0; i < 10; i++) pw.println("Unknown - 0");
                }
            }

            try (Scanner sc = new Scanner(SAVE_FILE, StandardCharsets.UTF_8)) {
                while (sc.hasNextLine()) {
                    String line = sc.nextLine().trim();
                    if (line.isEmpty()) continue;
                    String[] parts = line.split(" - ");
                    if (parts.length == 2) {
                        names.add(parts[0].trim());
                        scores.add(Integer.parseInt(parts[1].trim()));
                    }
                }
            }

            System.out.println("Đã đọc " + names.size() + " dòng từ " + SAVE_FILE.getPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cập nhật leaderboard khi người chơi có điểm cao hơn.
     */
    public static void updateLeaderboard(String playerName, int newScore) {
        loadLeaderboard();

        boolean found = false;
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(playerName)) {
                if (newScore > scores.get(i)) scores.set(i, newScore);
                found = true;
                break;
            }
        }
        if (!found) {
            names.add(playerName);
            scores.add(newScore);
        }

        sortLeaderboard();
        saveLeaderboard();

        System.out.println("Update leaderboard thành công cho " + playerName + "!");
    }

    /**
     * Trả về điểm hiện tại của player.
     */
    public static int getScoreByName(String playerName) {
        loadLeaderboard();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(playerName)) {
                return scores.get(i);
            }
        }
        return 0;
    }

    /**
     * Cộng/trừ điểm cho player (dùng trong shop).
     */
    public static void addScore(String playerName, int delta) {
        loadLeaderboard();
        boolean found = false;
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).equalsIgnoreCase(playerName)) {
                scores.set(i, Math.max(0, scores.get(i) + delta));
                found = true;
                break;
            }
        }
        if (!found) {
            names.add(playerName);
            scores.add(Math.max(0, delta));
        }
        sortLeaderboard();
        saveLeaderboard();
    }

    /**
     * Ghi lại danh sách điểm vào file
     */
    private static void saveLeaderboard() {
        try (PrintWriter pw = new PrintWriter(SAVE_FILE, StandardCharsets.UTF_8)) {
            for (int i = 0; i < Math.min(10, names.size()); i++) {
                pw.println(names.get(i) + " - " + scores.get(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sắp xếp giảm dần theo điểm
     */
    private static void sortLeaderboard() {
        for (int i = 0; i < scores.size() - 1; i++) {
            for (int j = i + 1; j < scores.size(); j++) {
                if (scores.get(j) > scores.get(i)) {
                    int tmpScore = scores.get(i);
                    scores.set(i, scores.get(j));
                    scores.set(j, tmpScore);

                    String tmpName = names.get(i);
                    names.set(i, names.get(j));
                    names.set(j, tmpName);
                }
            }
        }
    }

    /**
     * Hàm vẽ giao diện bảng xếp hạng.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int w = getWidth();
        int h = getHeight();

        if (backgroundImg != null)
            g2.drawImage(backgroundImg, 0, 0, w, h, this);
        else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, w, h);
        }

        // Bảng nền
        if (boardImg != null) {
            int iw = boardImg.getWidth(null);
            int ih = boardImg.getHeight(null);
            double scale = 0.25;
            int dw = (int) (iw * scale);
            int dh = (int) (ih * scale);
            int x = (w - dw) / 2;
            int y = (h - dh) / 2 + 60;
            g2.drawImage(boardImg, x, y, dw, dh, this);
        }

        // Tiêu đề
        g2.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 110));
        g2.setColor(new Color(0, 0, 0, 120));
        g2.drawString("LEADERBOARD", 262, 92);
        g2.setColor(Color.WHITE);
        g2.drawString("LEADERBOARD", 260, 90);

        // Dữ liệu
        int startY = 196;
        double lineHeight = 44.5;
        int nameColumnX = 420;
        int scoreColumnX = 675;

        for (int i = 0; i < Math.min(10, names.size()); i++) {
            int y = (int) (startY + i * lineHeight);

            g2.setFont(new Font("IntelOne Display Bold", Font.PLAIN, 28));
            g2.setColor(Color.WHITE);
            g2.drawString(names.get(i), nameColumnX, y);

            g2.setFont(new Font("Vermin Vibes 1989", Font.PLAIN, 36));
            g2.drawString(String.valueOf(scores.get(i)), scoreColumnX, y);
        }
    }
}

package model.powerup;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;

/**
 * Hiển thị danh sách các PowerUp đang hoạt động cùng vòng tròn đếm ngược.
 */
public class PowerUpView {

    private static final int ICON_SIZE = 30;
    private static final int GAP = 8;
    private static final int MARGIN = 6;
    private static final int TOTAL_DURATION_DEFAULT = 30; // fallback nếu ko có timer

    private BufferedImage[] icons;

    public PowerUpView() {
        loadIcons();
    }

    /**
     * Nạp sẵn toàn bộ ảnh PowerUp.
     */
    private void loadIcons() {
        icons = new BufferedImage[PowerUp.numsOfPU];
        for (int i = 0; i < PowerUp.numsOfPU; i++) {
            try {
                icons[i] = ImageIO.read(getClass().getResourceAsStream("/images/powerups/PU" + i + ".png"));
            } catch (IOException | IllegalArgumentException e) {
                System.err.println("⚠️ Không thể load icon PowerUp ID=" + i);
                icons[i] = null;
            }
        }
    }

    /**
     * Hàm vẽ icon đếm ngược.
     */
    public void draw(Graphics2D g, int panelWidth, int panelHeight) {
        Map<Integer, Integer> remaining = PowerUp.getRemainingTimes();
        if (remaining == null || remaining.isEmpty()) return;

        int x = 850;
        int y = panelHeight - ICON_SIZE - MARGIN;

        for (Map.Entry<Integer, Integer> entry : remaining.entrySet()) {
            int id = entry.getKey();
            int remain = entry.getValue();
            if (remain <= 0) continue; // bỏ qua power-up đã hết

            BufferedImage img = icons[id];
            drawIcon(g, img, x, y, id, remain);
            x += ICON_SIZE + GAP;
        }
    }

    /**
     * Vẽ từng icon kèm "cánh quạt" thời gian.
     */
    private void drawIcon(Graphics2D g, BufferedImage img, int x, int y, int id, int remain) {
        int total = guessTotalDuration(id);
        float ratio = 1f - Math.max(0f, (float) remain / total);
        float angle = 360f * ratio;

        // Vẽ icon
        if (img != null)
            g.drawImage(img, x, y, ICON_SIZE, ICON_SIZE, null);
        else {
            g.setColor(Color.GRAY);
            g.fillOval(x, y, ICON_SIZE, ICON_SIZE);
        }

        // Lớp phủ "cánh quạt" hiển thị phần còn lại
        Shape arc = new Arc2D.Float(x, y, ICON_SIZE, ICON_SIZE, 90, -angle, Arc2D.PIE);
        g.setColor(new Color(0, 0, 0, 150));
        g.fill(arc);

        // Viền trắng
        g.setColor(Color.WHITE);
        g.drawOval(x, y, ICON_SIZE, ICON_SIZE);

        // Hiển thị số giây
        g.setFont(new Font("IntelOne Display Bold", Font.BOLD, 12));
        String sec = String.valueOf(remain);
        FontMetrics fm = g.getFontMetrics();
        int tx = x + (ICON_SIZE - fm.stringWidth(sec)) / 2;
        int ty = y + (ICON_SIZE + fm.getAscent()) / 2 - 4;
        // Vẽ bóng trước (lệch 1–2 px)
        g.setColor(new Color(0, 0, 0, 180));
        g.drawString(sec, tx + 2, ty + 2);
        // Vẽ chữ sau
        g.setColor(Color.WHITE);
        g.drawString(sec, tx, ty);
    }

    /**
     * Ước lượng thời gian tổng theo ID (nếu không có dữ liệu thực tế)
     */
    private int guessTotalDuration(int id) {
        switch (id) {
            case 1: case 3: case 4: case 5: return 30;
            case 8: return 5;
            case 10: return 3;
            default: return TOTAL_DURATION_DEFAULT;
        }
    }
}

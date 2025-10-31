package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// Không bị overlap khi chạy gif => chuyển gif => frame.

/**
 * Vẽ chuỗi ảnh (PNG/GIF tĩnh/JPG) theo pattern, ví dụ: "/anim2/E9_%03d.png"
 * Nếu frameCount <= 0, sẽ tự dò số frame cho tới khi không còn file khớp pattern.
 */
public class PngAnimator extends JComponent {
    private final List<BufferedImage> frames = new ArrayList<>();
    private int idx = 0;
    private final int fps;
    private final double scale;
    private Dimension base = new Dimension(1, 1);

    public PngAnimator(String patternOnClasspath, int frameCount, int fps, double scale) {
        this.fps = Math.max(1, fps);
        this.scale = Math.max(0.01, scale);

        // Nạp frame
        if (frameCount <= 0) {
            // auto-scan cho tới khi không còn frame
            for (int i = 0; i < 10000; i++) {
                String path = String.format(patternOnClasspath, i);
                BufferedImage img = loadImage(path);
                if (img == null) break;
                frames.add(img);
            }
        } else {
            for (int i = 0; i < frameCount; i++) {
                String path = String.format(patternOnClasspath, i);
                BufferedImage img = loadImage(path);
                if (img == null) {
                    System.err.println("Miss frame: " + path);
                } else {
                    frames.add(img);
                }
            }
        }

        if (!frames.isEmpty()) {
            base = new Dimension(frames.get(0).getWidth(), frames.get(0).getHeight());
        }

        int delay = 1000 / this.fps;
        new Timer(delay, e -> {
            if (!frames.isEmpty()) {
                idx = (idx + 1) % frames.size();
                repaint();
            }
        }).start();

        setOpaque(false);

        // cuối constructor sau khi nạp frames
        System.out.println("Loaded " + frames.size() + " frames for pattern: " + patternOnClasspath);

    }

    @Override public Dimension getPreferredSize() {
        return new Dimension((int)Math.round(base.width * scale),
                (int)Math.round(base.height * scale));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (frames.isEmpty()) return;
        BufferedImage img = frames.get(idx);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        int w = (int)Math.round(img.getWidth() * scale);
        int h = (int)Math.round(img.getHeight() * scale);
        g2.drawImage(img, 0, 0, w, h, null);
        g2.dispose();
    }

    private static BufferedImage loadImage(String path) {
        try {
            URL u = findOnClasspath(path);
            return (u == null) ? null : ImageIO.read(u);
        } catch (Exception ex) {
            System.err.println("Load error: " + path + " -> " + ex.getMessage());
            return null;
        }
    }

    /** Tìm resource trên classpath theo nhiều cách (ổn định hơn). */
    private static URL findOnClasspath(String path) {
        String noSlash = path.startsWith("/") ? path.substring(1) : path;

        String[] candidates = new String[] {
                path,                          // /anim2/E9_000.png
                "/" + noSlash,                 // phòng khi thiếu slash
                "/Resources/" + noSlash,       // /Resources/anim2/E9_000.png
                "Resources/" + noSlash
        };

        for (String p : candidates) {
            URL u = MainMenu.class.getResource(p);
            if (u == null) {
                String q = p.startsWith("/") ? p.substring(1) : p;
                u = Thread.currentThread().getContextClassLoader().getResource(q);
                if (u == null) u = ClassLoader.getSystemResource(q);
            }
            if (u != null) return u;
        }
        return null;
    }

}

package view;

import game.ScreenSwitcher;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.net.URL;

public class MainMenu extends JPanel {

    private final ScreenSwitcher screenSwitcher;

    // Vì Resources/ là Resources Root
    private static final String BG_PATH = "/images/bg.png";

    private static final String ANIM1_PATTERN = "/images/anim1/E9_%03d.png"; // 000..005 (6)
    private static final String ANIM2_PATTERN = "/images/anim2/ChibiHerta_%03d.png";         // 000..009 (10)

    // Số frame đúng; có thể set = 0 để auto-scan
    private static final int ANIM1_FRAMES = 0;
    private static final int ANIM2_FRAMES = 0;

    private static final int ANIM_FPS = 12;
    private static final double ANIM_SCALE = 1.0;

    public MainMenu(ScreenSwitcher switcher) {
        this.screenSwitcher = switcher;
        setLayout(new BorderLayout());

        JLayeredPane layers = new JLayeredPane();
        add(layers, BorderLayout.CENTER);

        BackgroundPanel bgPanel = new BackgroundPanel(loadImage(BG_PATH));
        bgPanel.setOpaque(true);
        layers.add(bgPanel, JLayeredPane.DEFAULT_LAYER);

        JPanel ui = buildUiLayer();
        ui.setOpaque(false);
        layers.add(ui, JLayeredPane.PALETTE_LAYER);

        addComponentListener(new ComponentAdapter() {
            @Override public void componentResized(ComponentEvent e) {
                Dimension d = getSize();
                layers.setPreferredSize(d);
                layers.setBounds(0, 0, d.width, d.height);
                bgPanel.setBounds(0, 0, d.width, d.height);
                ui.setBounds(0, 0, d.width, d.height);
                layers.revalidate();
                layers.repaint();
            }
        });

        // Debug: in thử đường dẫn resource (giữ lại để kiểm tra)
        System.out.println("CLASSPATH ROOT = " + MainMenu.class.getResource("/"));
        System.out.println("TEST FRAME = " + MainMenu.class.getResource("/anim2/E9_000.png"));
    }

    private JPanel buildUiLayer() {
        JPanel ui = new JPanel(new BorderLayout());

        // Menu bên trái
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JButton btnPlay    = new JButton("PLAY");
        JButton btnSetting = new JButton("SETTING");
        JButton btnShop    = new JButton("SHOP");
        JButton btnExit    = new JButton("EXIT");

        styleButton(btnPlay);
        styleButton(btnSetting);
        styleButton(btnShop);
        styleButton(btnExit);

        btnPlay.addActionListener(e -> screenSwitcher.showGame());
        btnSetting.addActionListener(e -> JOptionPane.showMessageDialog(this, "Setting (stub)"));
        btnShop.addActionListener(e -> JOptionPane.showMessageDialog(this, "Shop (stub)"));
        btnExit.addActionListener(e -> System.exit(0));

        left.add(btnPlay);    left.add(Box.createVerticalStrut(24));
        left.add(btnSetting); left.add(Box.createVerticalStrut(24));
        left.add(btnShop);    left.add(Box.createVerticalStrut(24));
        left.add(btnExit);

        ui.add(left, BorderLayout.WEST);

        // Hai animation PNG góc dưới-trái
        JPanel bottomLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 12));
        bottomLeft.setOpaque(false);

        // Có thể để frameCount=0 để tự quét nếu bạn thêm/bớt frame
        PngAnimator ani1 = new PngAnimator(ANIM1_PATTERN, ANIM1_FRAMES, ANIM_FPS, ANIM_SCALE);
        PngAnimator ani2 = new PngAnimator(ANIM2_PATTERN, ANIM2_FRAMES, ANIM_FPS, ANIM_SCALE);

        bottomLeft.add(ani1);
        bottomLeft.add(ani2);
        ui.add(bottomLeft, BorderLayout.SOUTH);

        return ui;
    }

    public void styleButton(JButton b) {
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setFont(b.getFont().deriveFont(Font.BOLD, 44f));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addChangeListener(e ->
                b.setForeground(b.getModel().isRollover() ? new Color(230,240,255) : Color.WHITE));
    }

    private static Image loadImage(String path) {
        URL u = findOnClasspath(path);
        return (u == null) ? null : new ImageIcon(u).getImage();
    }

    /** Tìm resource trên classpath theo nhiều cách. */
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

    /** Panel vẽ nền co giãn, giữ tỉ lệ */
    private static class BackgroundPanel extends JPanel {
        private final Image bg;
        BackgroundPanel(Image bg) { this.bg = bg; }
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bg == null) return;
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int w = getWidth(), h = getHeight();
            int iw = bg.getWidth(null), ih = bg.getHeight(null);
            double s = Math.max(w / (double) iw, h / (double) ih);
            int dw = (int)Math.ceil(iw * s), dh = (int)Math.ceil(ih * s);
            int x = (w - dw) / 2, y = (h - dh) / 2;
            g2.drawImage(bg, x, y, dw, dh, null);
            g2.dispose();
        }
    }
}

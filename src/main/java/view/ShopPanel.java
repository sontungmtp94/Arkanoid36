package view;

import controller.GameState;
import game.ArkanoidGame;
import audio.SoundManager;
import audio.SoundId;

import javax.swing.*;
import java.awt.*;

public class ShopPanel extends JPanel {

    private final ArkanoidGame game;
    private final Image backgroundMenu;

    public ShopPanel(ArkanoidGame game) {
        this.game = game;
        setLayout(null);
        backgroundMenu = new ImageIcon(
                getClass().getResource("/images/utils/background_menu.png")
        ).getImage();
        setBackground(Color.BLACK);
        initUI();
    }

    private void initUI() {
        JLabel lblTitle = new JLabel("SHOP", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Vermin Vibes 1989", Font.BOLD, 52));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(0, 60, 1200, 80);
        add(lblTitle);

        JButton btnBackground = new JButton("BACKGROUND");
        JButton btnPaddle = new JButton("PADDLE");
        JButton btnBack = new JButton("BACK");

        styleButton(btnBackground);
        styleButton(btnPaddle);
        styleButton(btnBack);

        btnBackground.setBounds(400, 220, 400, 60);
        btnPaddle.setBounds(460, 320, 280, 60);
        btnBack.setBounds(500, 500, 200, 40);

        add(btnBackground);
        add(btnPaddle);
        add(btnBack);

        btnBackground.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            game.changeState(GameState.SHOP_BACKGROUND);
        });

        btnPaddle.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            game.changeState(GameState.SHOP_PADDLE);
        });

        btnBack.addActionListener(e -> {
            SoundManager.get().playSfx(SoundId.SFX_CLICK);
            game.changeState(GameState.MENU);
        });
    }

    private void styleButton(JButton b) {
        b.setFont(new Font("Vermin Vibes 1989", Font.BOLD, 52));
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addChangeListener(e ->
                b.setForeground(b.getModel().isRollover() ? new Color(200,220,255) : Color.WHITE));
    }

    /** Vẽ nền background_menu.png */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundMenu != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2.drawImage(backgroundMenu, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

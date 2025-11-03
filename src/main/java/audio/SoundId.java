package audio;

/**
 * SoundID để kiểm soát nhạc nền và sfx.
 */
public enum SoundId {
    // Bg music.
    BGM_MENU("sounds/bg_menu.wav", true),
    BGM_GAME("sounds/bg_game.wav", true),

    // Sound effects.
    SFX_CLICK("sounds/click.wav", false),
    SFX_HIT("sounds/hit_brick.wav", false),
    SFX_POWERUP("sounds/powerup.wav", false),
    SFX_LOSE("sounds/lose.wav", false),
    SFX_WIN("sounds/win.wav", false);

    private final String path;
    private final boolean isBgm;

    SoundId(String path, boolean isBgm) {
        this.path = path;
        this.isBgm = isBgm;
    }
    public String path() {
        return path;
    }
    public boolean isBgm() {
        return isBgm;
    }
}

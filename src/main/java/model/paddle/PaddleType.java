package model.paddle;

public enum PaddleType {
    NORMAL(null, null, null, null, "images/paddles/NormalPaddle_default.png"),

    GALAXY("Gravitational Field",
           "Attract PowerUps close to the Paddle.",
           "Space Travel",
           "Paddle have boosted speed and can move freely",
           "images/paddles/GalaxyPaddle_default.png"),

    BOMB("Rhythmic Impact",
         "Make one Ball create an explosion every three bounces",
         "Chain Reaction",
         "Place a bomb that creates an explosion when triggered. Bombs are triggered by other explosions.",
         "images/paddles/BombPaddle_default.png");

    private final String passiveSkill;
    private final String passiveDescription;
    private final String activeSkill;
    private final String activeDescription;
    private final String defaultSpritePath;

    /**
     * Constructor for PaddleType.
     *
     * @param passiveSkill       Tên kỹ năng bị động
     * @param passiveDescription Mô tả kỹ năng bị động
     * @param activeSkill        Tên kỹ năng chủ động
     * @param activeDescription  Mô tả kỹ năng chủ động
     * @param defaultSpritePath  Path đến ảnh Paddle mặc định
     */
    PaddleType(String passiveSkill, String passiveDescription,
               String activeSkill, String activeDescription,
               String defaultSpritePath) {
        this.passiveSkill = passiveSkill;
        this.passiveDescription = passiveDescription;
        this.activeSkill = activeSkill;
        this.activeDescription = activeDescription;
        this.defaultSpritePath = defaultSpritePath;
    }

    public void castActiveSkill() {

    }

    public void applyPassiveSkill() {

    }

    // Các getter.

    public String getPassiveSkill() {
        return passiveSkill;
    }

    public String getPassiveDescription() {
        return passiveDescription;
    }

    public String getActiveSkill() {
        return activeSkill;
    }

    public String getActiveDescription() {
        return activeDescription;
    }

    public String getDefaultSpritePathSpritePath() {
        return defaultSpritePath;
    }
}

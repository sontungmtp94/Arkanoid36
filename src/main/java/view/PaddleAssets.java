package view;

public class PaddleAssets {
    private static String currentPaddlePath = "/images/paddles/normal/NormalPaddle_default.png";

    public static String getCurrentPaddlePath() {
        return currentPaddlePath;
    }

    public static void setCurrentPaddle(String path) {
        currentPaddlePath = path;
    }
}

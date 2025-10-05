import java.awt.*;

public abstract class GameObject {
    /** Tọa độ góc trên bên trái của GameObjext. */
    private int x;
    private int y;

    /** Kích thước của GameObject. */
    private int width;
    private int height;

    /**
     * Constructor cho GameObject với vị trí và kích thước.
     *
     * @param x: Tọa độ x, Chiều ngang từ Trái sang Phải
     * @param y: Tọa độ y, Chiều dọc từ Trên xuống Dưới
     * @param width: Chiều rộng
     * @param height: Chiều cao
     */
    public GameObject(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Render đối tượng lên màn hình.
     *
     * @param g: Dùng để render
     */
    public abstract void render(Graphics2D g);

    /**
     * Cập nhật đối tượng, được lớp con Override.
     */
    public abstract void update();

    /**
     * Lấy vùng chiếm chỗ là Rectangle bao quanh đối tượng.
     * Dùng để kiểm tra va chạm.
     *
     * @return Trả về vùng chiếm chỗ của đối tượng
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    // Các getter và setter
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}

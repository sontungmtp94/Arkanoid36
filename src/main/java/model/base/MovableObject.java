package model.base;

public abstract class MovableObject extends GameObject {
    /** Vận tốc theo 2 trục x, y. */
    private double dx;
    private double dy;

    /**
     * Constructor cho MovableObject với vị trí, kích thước và tốc độ.
     *
     * @param x      Tọa độ x (ngang)
     * @param y      Tọa độ y (dọc)
     * @param width  Chiều rộng
     * @param height Chiều cao
     */
    public MovableObject(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.dx = 0;
        this.dy = 0;
    }

    // Các getter và setter

    public double getDx() {
        return dx;
    }

    public void setDx(double dx) {
        this.dx = dx;
    }

    public double getDy() {
        return dy;
    }

    public void setDy(double dy) {
        this.dy = dy;
    }

    public void setVelocity(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }
}
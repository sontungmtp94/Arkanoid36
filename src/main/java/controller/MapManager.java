package controller;

import model.brick.Brick;
import model.brick.BrickType;
import view.SpritesView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * controller.MapManager - Quản lý nhiều bản đồ (map) trong game Arkanoid.
 * Mỗi map được định danh bằng một ID (số nguyên).
 * Một map chứa danh sách các viên gạch (model.brick.Brick).
 */
public class MapManager {

    // map để lưu trữ gạch
    private Map<Integer, ArrayList<Brick>> maps;

    //thông số cố định của điểm bắt đầu vẽ gạch.
    private int startX = 40;
    private int startY = 40;

    // thông số cố định của gạch.
    private final int WIDTH = 70;
    private final int HEIGHT = 40;

    // thông số cố định của khoảng cách giữa các gạch.
    private int spaceX = WIDTH + 5;
    private int spaceY = HEIGHT + 5;

    // số map trong game và số string chứa đường dẫn tới file txt tạo map gạch.
    private static final int numOfMaps = 20;
    private String[] pathFiles = new String[numOfMaps];

    /**
     * Constructor - khởi tạo controller.MapManager.
     */
    public MapManager() {
        maps = new HashMap<>();
        for (int i = 0; i < numOfMaps; i++) {
            pathFiles[i] = "src/main/resources/maps/Map" + (i + 1) + ".txt";
        }
        ListOfMap();
    }

    /**
     * Thêm một map mới vào danh sách quản lý
     * @param id: Mã ID của map
     * @param map: Danh sách gạch của map
     */
    public void addMap(int id, ArrayList<Brick> map) {
        maps.put(id, map);
    }

    /**
     * Lấy danh sách gạch từ một map theo ID
     * @param id: Mã ID của map
     * @return danh sách gạch (nếu có), null nếu không tồn tại
     */
    public ArrayList<Brick> loadMap(int id) {
        ArrayList<Brick> original = maps.get(id);
        if (original == null) return null;

        // Tạo danh sách gạch mới
        ArrayList<Brick> cloneList = new ArrayList<>();
        for (Brick b : original) {
            Brick clone = new Brick(
                    b.getX(),
                    b.getY(),
                    b.getWidth(),
                    b.getHeight(),
                    b.getType() // cùng loại
            );
            cloneList.add(clone);
        }
        return cloneList;
    }


    /**
     * Tạo sẵn một số map mặc định.
     */
    public void ListOfMap() {
        for (int i = 1; i <= numOfMaps; i++) {
            mapTxt(i);
        }
    }

    /**
     * Đọc map gạch từ file txt.
     */
    public void mapTxt(int id) {
        ArrayList<Brick> map = new ArrayList<>();
        File file = new File(pathFiles[id - 1]);

        try (Scanner sc = new Scanner(file)) {
            int row = 0;
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                for (int col = 0; col < tokens.length; col++) {
                    String cell = tokens[col];
                    if (cell.equals("0")) continue;

                    BrickType type;
                    int hp;

                    if (cell.equals("*")) {
                        type = BrickType.METAL;
                        hp = type.getHitPoints();
                    } else {
                        hp = Integer.parseInt(cell);
                        type = getTypeByHP(hp);
                    }

                    Brick brick = new Brick(
                            startX + spaceX * col,
                            startY + spaceY * row,
                            WIDTH, HEIGHT,
                            type
                    );
                    brick.setHitPoints(hp);
                    map.add(brick);
                }
                row++;
            }

            addMap(id, map);
        System.out.printf("Loaded Map%d with %d bricks.%n", id, map.size());

    } catch (IOException e) {
        System.err.println("Không thể đọc file Map" + id + ".txt: " + e.getMessage());
    }
}

    /**
     * Chọn loại gạch theo các ký tự trong file (0: không có, 1-6: Gạch 1-6 máu, mặc định: Gạch k phá được).
     */
    private BrickType getTypeByHP(int hp) {
        return switch (hp) {
            case 1 -> BrickType.CYAN;
            case 2 -> BrickType.BLUE;
            case 3 -> BrickType.PURPLE;
            case 4 -> BrickType.YELLOW;
            case 5 -> BrickType.ORANGE;
            case 6 -> BrickType.RED;
            default -> BrickType.METAL;
        };
    }

    private BufferedImage spriteSheet;
    public static int getNumOfMaps() {
        return numOfMaps;
    }
}

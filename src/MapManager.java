import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * MapManager - Quản lý nhiều bản đồ (map) trong game Arkanoid.
 * Mỗi map được định danh bằng một ID (số nguyên).
 * Một map chứa danh sách các viên gạch (Brick).
 */
public class MapManager {

    // map để lưu trữ gạch
    private Map<Integer, ArrayList<Brick>> maps;

    // thông số cố định của gạch.
    private final int WIDTH = 70;
    private final int HEIGHT = 40;

    // thông số cố định của khoảng cách giữa các gạch.
    private int spaceX = WIDTH + 5;
    private int spaceY = HEIGHT + 5;

    /**
     * Constructor - khởi tạo MapManager.
     */
    public MapManager() {
        maps = new HashMap<>();
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
        return maps.get(id);
    }

    /**
     * Tạo sẵn một số map mặc định.
     */
    public void ListOfMap() {
        map1();
        map2();
        map3();
        map4();
    }

    /**
     * Map 1: 5 hàng gạch, mỗi hàng 15 khối.
     */
    public void map1() {
        ArrayList<Brick> map = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 15; j++) {
                Brick t = new Brick(40 + spaceX * j, 40 + spaceY * i, WIDTH, HEIGHT, "normal", 1, Color.BLUE);
                map.add(t);
            }
        }
        addMap(1, map);
    }

    /**
     * Map 2: Hình kim tự tháp úp ngược.
     */
    public void map2() {
        ArrayList<Brick> map = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for(int j = i; j < 15 - i; j++) {
                Brick t = new Brick(40 + spaceX * j, 40 + spaceY * i, WIDTH, HEIGHT, "normal", 1, Color.MAGENTA);
                map.add(t);
            }
        }
        addMap(2, map);
    }

    /**
     * Map 3: 8 cột gạch cách nhau 1 khoảng, mỗi cột 8 khối.
     */
    public void map3() {
        ArrayList<Brick> map = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for(int j = 0; j < 15; j++) {
                if (j % 2 == 0) {
                    Brick t = new Brick(40 + spaceX * j, 40 + spaceY * i, WIDTH, HEIGHT, "normal", 1, Color.RED);
                    map.add(t);
                }
            }
        }
        addMap(3, map);
    }

    /**
     * Map 4: 5 chùm khối gạch xếp hình chữ X.
     */
    public void map4() {
        ArrayList<Brick> map = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            if (i < 2 || i > 3) {
                for (int j = 0; j < 15; j++) {
                    if (j < 5 || j > 9) {
                        Brick t = new Brick(40 + spaceX * j, 40 + spaceY * i, WIDTH, HEIGHT, "normal", 1, Color.YELLOW);
                        map.add(t);
                    }
                }
            } else {
                for (int j = 0; j < 15; j++) {
                    if (j >= 5 && j <= 9) {
                        Brick t = new Brick(40 + spaceX * j, 40 + spaceY * i, WIDTH, HEIGHT, "normal", 1, Color.YELLOW);
                        map.add(t);
                    }
                }
            }
        }
        addMap(4, map);
    }
}

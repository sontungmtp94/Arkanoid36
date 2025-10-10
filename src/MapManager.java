import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * MapManager - Quản lý nhiều bản đồ (map) trong game Arkanoid
 * Mỗi map được định danh bằng một ID (số nguyên).
 * Một map chứa danh sách các viên gạch (Brick).
 */
public class MapManager {

    // Lưu trữ nhiều map: key = mapID, value = danh sách gạch
    private Map<Integer, ArrayList<Brick>> maps;

    /**
     * Constructor - khởi tạo MapManager
     */
    public MapManager() {
        maps = new HashMap<>();
        initDefaultMaps(); // tạo sẵn một số map mặc định
    }

    /**
     * Thêm một map mới vào danh sách quản lý
     * @param id: Mã ID của map
     * @param bricks: Danh sách gạch của map
     */
    public void addMap(int id, ArrayList<Brick> bricks) {
        maps.put(id, bricks);
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
     * Lấy danh sách ID của các map có sẵn
     * @return Set<Integer> gồm các ID
     */
    public Set<Integer> getAvailableMaps() {
        return maps.keySet();
    }

    /**
     * Tạo sẵn một số map mặc định
     */
    private void initDefaultMaps() {
        final int w = 100;
        final int h = 40;
        // Map 1: Dòng gạch đơn giản
        ArrayList<Brick> map1 = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            map1.add(new Brick(50 + i * (w + 5), 50, w, h, "normal", 1, Color.RED));
        }
        addMap(1, map1);

        // Map 2: 2 hàng gạch
        ArrayList<Brick> map2 = new ArrayList<>();
        for (int col = 0; col < 10; col++) {
            map2.add(new Brick(50 + col * (w + 5), 50, w, h, "normal", 1, Color.BLUE));
            map2.add(new Brick(50 + col * (w + 5), 50 + (h + 5), w, h, "normal", 1, Color.GREEN));
        }
        addMap(2, map2);

        // Map 3: Map 5x10 gạch
        ArrayList<Brick> map3 = new ArrayList<>();
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 10; col++) {
                map3.add(new Brick(40 + col * (w + 5), 50 + row * (h + 5), w, h, "normal", 1, Color.BLUE));
            }
        }
        addMap(3, map3);
    }
}

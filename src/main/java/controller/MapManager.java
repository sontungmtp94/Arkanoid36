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

    public void mapTxt(int id) {
        ArrayList<Brick> map = new ArrayList<>();
        try(Scanner sc = new Scanner(new File(pathFiles[id - 1]))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (!tokens[i].equals("0") && !tokens[i].equals("*")) {
                        BrickType randomType = BrickType.getRandomType();
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, randomType);
                        t.setHitPoints(Integer.parseInt(tokens[i]));
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map" + (id + 1) + ".txt" + e.getMessage());
        }
        addMap(id, map);
    }
    /**
     * Hàm tạo map thủ công (phục vụ cho việc xếp Brick không theo vị trí cụ thể).
     */
    public void map1() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = pathFiles[1];

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        BrickType randomType = BrickType.getRandomType();
                        Brick t = new Brick(40 + spaceX * i, 40 + spaceY * row, WIDTH, HEIGHT, randomType);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map" + 1 + ".txt" + e.getMessage());
        }
        addMap(1, map);
    }

    private BufferedImage spriteSheet;
    public static int getNumOfMaps() {
        return numOfMaps;
    }
}

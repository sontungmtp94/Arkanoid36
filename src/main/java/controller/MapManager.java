package controller;

import model.brick.Brick;
import model.brick.BrickType;

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

    /**
     * Constructor - khởi tạo controller.MapManager.
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
        map5();
        map6();
    }

    /**
     * Map 1: 5 hàng gạch, mỗi hàng 15 khối.
     */
    public void map1() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = "src/main/resources/maps/Map1.txt";

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, BrickType.NORMAL);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map1.txt: " + e.getMessage());
        }
        addMap(1, map);
    }

    /**
     * Map 2: Hình kim tự tháp úp ngược.
     */
    public void map2() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = "src/main/resources/maps/Map2.txt";

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, BrickType.NORMAL);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map1.txt: " + e.getMessage());
        }
        addMap(2, map);
    }

    /**
     * Map 3: 8 cột gạch cách nhau 1 khoảng, mỗi cột 6 khối.
     */
    public void map3() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = "src/main/resources/maps/Map3.txt";

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, BrickType.NORMAL);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map1.txt: " + e.getMessage());
        }
        addMap(3, map);
    }

    /**
     * Map 4: 5 chùm khối gạch xếp hình chữ X.
     */
    public void map4() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = "src/main/resources/maps/Map4.txt";

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, BrickType.NORMAL);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map1.txt: " + e.getMessage());
        }
        addMap(4, map);
    }

    public void map5() {
        ArrayList<Brick> map = new ArrayList<>();
        String path = "src/main/resources/maps/Map5.txt";

        try(Scanner sc = new Scanner(new File(path))) {
            int row = 0;
            while(sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                String[] tokens = line.split("\\s+");
                for(int i = 0; i < tokens.length; i++) {
                    if (tokens[i].equals("1")) {
                        Brick t = new Brick(startX + spaceX * i, startY + spaceY * row, WIDTH, HEIGHT, BrickType.NORMAL);
                        map.add(t);
                    }
                }
                row++;
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file Map1.txt: " + e.getMessage());
        }
        addMap(5, map);
    }

    public void map6() {
        ArrayList<Brick> map = new ArrayList<>();
        Brick t = new Brick(startX + 120, startY + 120, WIDTH * 5, HEIGHT * 5, BrickType.NORMAL);
        map.add(t);
        addMap(6, map);
    }
}

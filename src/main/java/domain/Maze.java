package domain;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Maze {
    private double noOfWalls;
    private int noOfRows;
    private int noOfColumns;
    private int mazeMaxCellId;
    private int mazeLastColumnNo;
    private int[][] adjacencyMatrix;
    private Properties applicationConfig;
    private Set<Integer> wallCellIds;
    private Map<String, Integer> possibleMoves;


    private static final int MAZE_MIN_CELL_ID = 0;
    private static final int MAZE_FIRST_COLUMN_NO = 0;
    private static final int EDGE_DOES_NOT_EXIST = 0;
    private static final int EDGE_EXISTS = 1;

    public static final String TEXT_COLOR_RESET = "\033[0m";
    public static final String TEXT_COLOR_RED = "\033[0;31m";
    public static final String TEXT_COLOR_GREEN = "\033[0;32m";
    public static final String TEXT_COLOR_YELLOW = "\033[0;33m";

    public Maze(Properties properties) {
        applicationConfig = properties;
        noOfRows = Integer.parseInt(applicationConfig.getProperty("rows"));
        noOfColumns = Integer.parseInt(applicationConfig.getProperty("columns"));
        listPossibleMoves();
        mazeMaxCellId = noOfRows * noOfColumns - 1;
        mazeLastColumnNo = noOfColumns - 1;
        adjacencyMatrix = new int[mazeMaxCellId + 1][mazeMaxCellId + 1];
    }

    public void listPossibleMoves() {
        possibleMoves = new HashMap<String, Integer>();
        possibleMoves.put("UP", -noOfColumns);
        possibleMoves.put("DOWN", noOfColumns);
        possibleMoves.put("RIGHT", 1);
        possibleMoves.put("LEFT", -1);
        possibleMoves.put("UPPERRIGHT", -(noOfColumns - 1));
        possibleMoves.put("UPPERLEFT", -(noOfColumns + 1));
        possibleMoves.put("LOWERRIGHT", noOfColumns + 1);
        possibleMoves.put("LOWERLEFT", noOfColumns - 1);
    }

    public void getCustomWalls() {
        noOfWalls = Integer.parseInt(applicationConfig.getProperty("no.of.walls"));
        String walls = applicationConfig.getProperty("wall.ids");
        List<String> wallList = new ArrayList<>(Arrays.asList(walls.split(",")));
        wallCellIds = new HashSet<Integer>();

        for (int i = 0; i < noOfWalls; i++) {
            int wallId = Integer.parseInt(wallList.get(i));
            wallCellIds.add(wallId);
        }

    }

    public void getRandomWalls() {
        noOfWalls = Integer.parseInt(applicationConfig.getProperty("no.of.walls"));
        wallCellIds = new HashSet<Integer>();
        while (wallCellIds.size() != noOfWalls) {
            wallCellIds.add(ThreadLocalRandom.current().nextInt(MAZE_MIN_CELL_ID, mazeMaxCellId + 1));
        }
    }

    public void generateAdjacencyMatrix() {

        for (int i = MAZE_MIN_CELL_ID; i <= mazeMaxCellId; i++) {
            findEdges(i);
        }
    }

    public void findEdges(int cellId) {
        Arrays.fill(adjacencyMatrix[cellId], EDGE_DOES_NOT_EXIST);
        for (Map.Entry<String, Integer> entry : possibleMoves.entrySet()) {
            int move = entry.getValue();
            int adjacentCellId = cellId + move;
            int cellX = cellId % noOfColumns;

            if (cellX == MAZE_FIRST_COLUMN_NO && (move == possibleMoves.get("LEFT") || move == possibleMoves.get("UPPERLEFT") || move == possibleMoves.get("LOWERLEFT")))
                continue;
            if (cellX == mazeLastColumnNo && (move == possibleMoves.get("RIGHT") || move == possibleMoves.get("LOWERRIGHT") || move == possibleMoves.get("UPPERRIGHT")))
                continue;
            if (adjacentCellId >= MAZE_MIN_CELL_ID && adjacentCellId <= mazeMaxCellId && !wallCellIds.contains(adjacentCellId))
                adjacencyMatrix[cellId][adjacentCellId] = EDGE_EXISTS;
        }
    }

//    public void printGrid() {
//        System.out.println(TEXT_COLOR_YELLOW + "Maze with walls:" + TEXT_COLOR_RESET);
//
//        for (int i = MAZE_MIN_CELL_ID; i <= mazeMaxCellId; i++) {
//            if (wallCellIds.contains(i)) {
//                System.out.print(TEXT_COLOR_RED + "X" + TEXT_COLOR_RESET);
//                printWhitespaces(1);
//            } else {
//                System.out.print(i);
//                printWhitespaces(i);
//            }
//
//            if (i % noOfColumns == mazeLastColumnNo) {
//                System.out.println();
//                System.out.println();
//            }
//
//        }
//    }

    public void printGridWithPath(List<Double> path) {
        System.out.println(TEXT_COLOR_YELLOW + "Maze with walls and path:" + TEXT_COLOR_RESET);
        for (int i = MAZE_MIN_CELL_ID; i <= mazeMaxCellId; i++) {
            if (wallCellIds.contains(i)) {
                System.out.print(TEXT_COLOR_RED + "X" + TEXT_COLOR_RESET);
                printWhitespaces(1);
            } else {
                if (path.contains((double) i)) {
                    System.out.print(TEXT_COLOR_GREEN + i + TEXT_COLOR_RESET);
                    printWhitespaces(i);
                } else {
                    System.out.print(i);
                    printWhitespaces(i);
                }
            }

            if (i % noOfColumns == mazeLastColumnNo) {
                System.out.println();
                System.out.println();
            }

        }
    }

    public void printWhitespaces(int cellId) {
        int noOfDigits = String.valueOf(cellId).length();
        int noOfWhitespaces = String.valueOf(mazeMaxCellId).length() + 1 - noOfDigits;
        for (int n = 0; n < noOfWhitespaces; n++)
            System.out.print(" ");
    }

    public int[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }

    public void setAdjacencyMatrix(int[][] adjacencyMatrix) {
        this.adjacencyMatrix = adjacencyMatrix;
    }

    public Set<Integer> getWallCellIds() {
        return wallCellIds;
    }

    public void setWallCellIds(Set<Integer> wallCellIds) {
        this.wallCellIds = wallCellIds;
    }
}

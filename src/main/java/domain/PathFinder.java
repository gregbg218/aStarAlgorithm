package domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PathFinder {
    private boolean invalidFlag;
    private boolean generateRandomWalls;
    private double sourceId;
    private double destinationId;
    private Properties applicationConfig;
    private List<Double> path;
    private PriorityQueue<Cell> closedList;
    private PriorityQueue<Cell> openList;
    private Maze maze;
    public static Logger logger = LoggerFactory.getLogger(PathFinder.class);

    public PathFinder(Properties properties) {
        applicationConfig = properties;
        maze = new Maze(applicationConfig);
        sourceId = Double.parseDouble(applicationConfig.getProperty("source"));
        destinationId = Double.parseDouble(applicationConfig.getProperty("destination"));
        generateRandomWalls = Boolean.parseBoolean(applicationConfig.getProperty("random.walls"));
    }

    public void selectWallGenerationMethod() {
        if (generateRandomWalls) {
            maze.getRandomWalls();
        } else {
            maze.getCustomWalls();
        }
        maze.generateAdjacencyMatrix();
        logger.info("Source: " + sourceId + " Destination: " + destinationId);
        logger.debug("Source: " + sourceId + " Destination: " + destinationId);
    }

    public void findPath() {
        calculatePath();
        if (!path.contains(destinationId) || !path.contains(sourceId)) {
            System.out.println("Invalid parameters. No path could be found");
            logger.info("Invalid parameters. No path could be found");
            logger.debug("Invalid parameters. No path could be found");
            System.exit(0);
        }

    }

    public void calculatePath() {

        Cell currentCell = new Cell(applicationConfig, sourceId, sourceId, destinationId, null);
        closedList = new PriorityQueue<Cell>(new CostComparator());
        closedList.add(currentCell);
        openList = new PriorityQueue<Cell>(new CostComparator());
        path = new ArrayList<Double>();

        while (currentCell.getCellId() != destinationId) {
            currentCell.listAdjacentCells(maze.getAdjacencyMatrix());
            List<Double> adjacentCells = currentCell.getAdjacentCells();
            for (double adjacentCellId : adjacentCells) {
                Cell newCell = new Cell(applicationConfig, adjacentCellId, sourceId, destinationId, currentCell);

                boolean replacementFlag;
                if (checkOpenList(adjacentCellId)) {
                    replacementFlag = checkReplacementOpenList(newCell);
                    if (!replacementFlag) {
                        continue;
                    }
                }
                if (checkClosedList(adjacentCellId)) {
                    replacementFlag = checkReplacementClosedList(newCell);
                    if (!replacementFlag) {
                        continue;
                    }
                }

                openList.add(newCell);
                logger.debug("Adding neighbour cell " + newCell.getCellId() + " to Open List");
            }

            currentCell = openList.poll();
            if (currentCell == null) {
                invalidFlag = true;
                break;
            }
            closedList.add(currentCell);
            logger.debug("Adding cell " + currentCell.getCellId() + " to Closed List");

        }
        if (!invalidFlag)
            createPathList();

    }

    public boolean checkOpenList(double targetCellId) {
        for (Cell member : openList) {
            if (member.getCellId() == targetCellId)
                return true;
        }
        return false;
    }

    public boolean checkClosedList(double targetCellId) {
        for (Cell member : closedList) {
            if (member.getCellId() == targetCellId)
                return true;
        }
        return false;
    }

    public boolean checkReplacementOpenList(Cell newCell) {
        Iterator<Cell> iterator = openList.iterator();
        while (iterator.hasNext()) {
            Cell oldCell = iterator.next();
            if (oldCell.getCellId() == newCell.getCellId() && oldCell.getCostFromSource() > newCell.getCostFromSource()) {
                iterator.remove();
                return true;
            }

        }
        return false;
    }

    public boolean checkReplacementClosedList(Cell newCell) {
        Iterator<Cell> iterator = closedList.iterator();
        while (iterator.hasNext()) {
            Cell oldCell = iterator.next();
            if (oldCell.getCellId() == newCell.getCellId() && oldCell.getCostFromSource() > newCell.getCostFromSource()) {
                iterator.remove();
                return true;
            }

        }
        return false;
    }

    public void createPathList() {
        Cell destinationCell = closedList.peek();
        for (Cell member : closedList) {
            if (member.getCellId() == destinationId)
                destinationCell = member;
        }
        path.add(destinationId);
        while (!(destinationCell.getCellId() == sourceId)) {
            Cell currentCell = destinationCell.getParentCell();
            path.add(currentCell.getCellId());
            destinationCell = currentCell;
        }
        Collections.reverse(path);
    }


    public void printPath() {
        System.out.println("Source: " + sourceId + " Destination: " + destinationId);
        maze.printGridWithPath(path);
        System.out.println("Route: ");
        logger.info("Route: ");
        logger.debug("Route: ");
        for (double currentCell : path) {
            System.out.print((int) currentCell + " -> ");
            logger.info((int) currentCell + " -> ");
            logger.debug((int) currentCell + " -> ");
        }
        System.out.println();
        System.out.println();


    }


    public boolean isInvalidFlag() {
        return invalidFlag;
    }

    public void setInvalidFlag(boolean invalidFlag) {
        this.invalidFlag = invalidFlag;
    }

    public double getSourceId() {
        return sourceId;
    }

    public void setSourceId(double sourceId) {
        this.sourceId = sourceId;
    }

    public double getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(double destinationId) {
        this.destinationId = destinationId;
    }

    public Maze getMaze() {
        return maze;
    }

    public void setMaze(Maze maze) {
        this.maze = maze;
    }

    public List<Double> getPath() {
        return path;
    }

    public void setPath(List<Double> path) {
        this.path = path;
    }

    public PriorityQueue<Cell> getClosedList() {
        return closedList;
    }

    public void setClosedList(PriorityQueue<Cell> closedList) {
        this.closedList = closedList;
    }

    public PriorityQueue<Cell> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Cell> openList) {
        this.openList = openList;
    }
}

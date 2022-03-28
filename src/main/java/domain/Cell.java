package domain;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Cell {

    private int noOfRowsMaze;
    private int noOfColumnsMaze;
    private int adjMatrixLastColumnNo;
    private double cellId;
    private Cell parentCell;
    private double destination;
    private double costFromSource;
    private double heuristicCost;
    private double finalCost;
    private double cellX;
    private double cellY;
    private double source;
    private Properties applicationConfig;
    private List<Double> adjacentCells;


    private static final int ADJACENCY_MATRIX_FIRST_COLUMN_NO = 0;
    private static final int EDGE_EXISTS = 1;
    private static final double CARDINAL_MOVE_COST = 1;
    private static final double DIAGONAL_MOVE_COST = Math.sqrt(2);

    public Cell(Properties properties, double cellId, double source, double destination, Cell parentCell) {
        applicationConfig = properties;
        noOfRowsMaze = Integer.parseInt(applicationConfig.getProperty("rows"));
        noOfColumnsMaze = Integer.parseInt(applicationConfig.getProperty("columns"));
        this.adjMatrixLastColumnNo = noOfColumnsMaze * noOfRowsMaze - 1;
        this.cellId = cellId;
        this.source = source;
        this.destination = destination;
        this.parentCell = parentCell;
        cellX = cellId % noOfColumnsMaze;
        cellY = (cellId - cellX) / noOfRowsMaze;
        calculateFinalCost();

    }


    public void calculateFinalCost() {
        calculateCostFromSource();
        calculateHeuristicCost();
        finalCost = costFromSource + heuristicCost;
    }

    public void calculateCostFromSource() {
        double parentCellId;
        double parentCellCost;
        if (parentCell == null) {
            parentCellId = cellId;
            parentCellCost = 0;
        } else {
            parentCellId = parentCell.getCellId();
            parentCellCost = parentCell.costFromSource;
        }
        double a = (parentCellId % noOfColumnsMaze) - (cellId % noOfColumnsMaze);
        double b = (parentCellId - (cellId + a)) / noOfRowsMaze;
        costFromSource = usePythagorasTheorem(a, b) + parentCellCost;
    }

    public void listAdjacentCells(int[][] adjacencyMatrix) {
        adjacentCells = new ArrayList<Double>();
        for (int i = ADJACENCY_MATRIX_FIRST_COLUMN_NO; i <= adjMatrixLastColumnNo; i++) {
            if (adjacencyMatrix[(int) cellId][i] == EDGE_EXISTS)
                adjacentCells.add((double) i);
        }
    }

    public double usePythagorasTheorem(double a, double b) {
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    public void calculateHeuristicCost() {
        double destX = destination % noOfColumnsMaze;
        double destY = (destination - destX) / noOfRowsMaze;
        double dx = Math.abs(cellX - destX);
        double dy = Math.abs(cellY - destY);
        double e = 2 * CARDINAL_MOVE_COST - DIAGONAL_MOVE_COST;
        heuristicCost = (e * Math.abs(dx - dy) + DIAGONAL_MOVE_COST * (dx + dy)) / 2;
    }

    @Override
    public String toString() {
        double parent;
        if (parentCell == null)
            parent = cellId;
        else
            parent = parentCell.getCellId();

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(3);
        String output = "CellId=" + cellId + "  Parent=" + parent +
                "  finalCost=" + df.format(finalCost) + "  Gcost=" + df.format(costFromSource) + " and Hcost=" + df.format(heuristicCost);
        return output;
    }

    public double getCostFromSource() {
        return costFromSource;
    }

    public void setCostFromSource(double costFromSource) {
        this.costFromSource = costFromSource;
    }

    public double getHeuristicCost() {
        return heuristicCost;
    }

    public void setHeuristicCost(double heuristicCost) {
        this.heuristicCost = heuristicCost;
    }

    public double getFinalCost() {
        return finalCost;
    }

    public void setFinalCost(double finalCost) {
        this.finalCost = finalCost;
    }

    public double getCellId() {
        return cellId;
    }

    public void setCellId(double cellId) {
        this.cellId = cellId;
    }

    public List<Double> getAdjacentCells() {
        return adjacentCells;
    }

    public void setAdjacentCells(List<Double> adjacentCells) {
        this.adjacentCells = adjacentCells;
    }

    public double getSource() {
        return source;
    }

    public void setSource(double source) {
        this.source = source;
    }

    public double getDestination() {
        return destination;
    }

    public void setDestination(double destination) {
        this.destination = destination;
    }

    public Cell getParentCell() {
        return parentCell;
    }

    public void setParentCell(Cell parentCell) {
        this.parentCell = parentCell;
    }

    public double getCellX() {
        return cellX;
    }

    public void setCellX(double cellX) {
        this.cellX = cellX;
    }

    public double getCellY() {
        return cellY;
    }

    public void setCellY(double cellY) {
        this.cellY = cellY;
    }
}

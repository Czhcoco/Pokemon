import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Map {

    public static final char WALL = '#', PATH = ' ', START = 'B', DESTINATION = 'D', STATION = 'S', POKEMON = 'P';

    private Cell[][] cells;
    private int MAX_ROW;
    private int MAX_COL;
    private Cell start, destination;

    private ArrayList<Cell> visitList;
    private ArrayList<Cell> wholeList;

    private int[][] distance;

    {
        wholeList = new ArrayList<Cell>();
        visitList = new ArrayList<Cell>();
    }

    Map(int row, int col) {
        this.MAX_ROW = row;
        this.MAX_COL = col;
        cells = new Cell[row][col];
    }

    public void setPath(int row, int col, char type) {
        if (row < MAX_ROW && row >= 0 && col < MAX_COL && col >= 0) {
            switch (type) {
                case WALL:
                    cells[row][col].setValid(false);
                    break;
                case START:
                    start = new Cell(row, col);
                    break;
                case DESTINATION:
                    destination = new Cell(row, col);
                    break;
            }
        }
    }

    public void addStation(Station station) {
        visitList.add(station);
    }

    public void addPokemon(Pokemon pokemon) {
        visitList.add(pokemon);
    }

    public Cell getStart() {
        return start;
    }

    public Cell getDestination() {
        return destination;
    }

    public ArrayList<Cell> getVisitList() {
        return visitList;
    }

    public int[][] getDistance() {
        return distance;
    }

    public int getDistance(Cell a, Cell b) {
        return distance[wholeList.indexOf(a)][wholeList.indexOf(b)];
    }

    public void setWholeList() {
        wholeList.addAll(visitList);
    }

    public boolean isValid(int row, int col) {
        return (row >= 0 && row < MAX_ROW && col >= 0 && col < MAX_COL);
    }

    private static final int rowNum[] = {-1, 0, 0, 1};
    private static final int colNum[] = {0, -1, 1, 0};

    public void findShortestPath() {
        distance = new int [visitList.size()][visitList.size()];
        Arrays.fill(distance, 0);
        for (int i = 0; i < visitList.size(); i++) {
            for (int j = 0; j < visitList.size(); j++) {
                boolean[][] visited = new boolean[visitList.size()][visitList.size()];
                int rowI = visitList.get(i).getRow();
                int colI = visitList.get(i).getCol();
                visited[rowI][colI] = true;

                Queue<Cell> queue = new LinkedList<>();
                queue.add(visitList.get(i));

                while (!queue.isEmpty()) {
                    Cell current = queue.peek();
                    int dist = current.getDist();

                    if (current.equals(visitList.get(j))) {
                        distance[i][j] = dist;
                        break;
                    }

                    for (int k = 0; k < 4; k++) {
                        int rowAdj = rowI + rowNum[k];
                        int colAdj = colI + colNum[k];
                        if (isValid(rowAdj, colAdj) && !visited[rowAdj][colAdj]) {
                            visited[rowAdj][colAdj] = true;
                            Cell adjCell = new Cell(rowAdj, colAdj);
                            adjCell.setDist(dist + 1);
                            queue.add(adjCell);
                        }
                    }
                }
            }
        }
    }
}

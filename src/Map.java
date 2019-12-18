import java.util.*;

public class Map {

    private static final char WALL = '#', PATH = ' ', START = 'B', DESTINATION = 'D', STATION = 'S', POKEMON = 'P';

    /**
     * Parameters of map's size, start point and destination point
     */
    private Cell[][] cells;
    private static int MAX_ROW;
    private static int MAX_COL;
    private Cell start, destination;

    /**
     * List of pokemon and stations
     */
    private ArrayList<Cell> visitList;

    /**
     * List of pokemon, stations, start point and destination point
     */
    private ArrayList<Cell> wholeList;

    /**
     * Distance between one pokemon/station and another pokemon/station
     */
    private int[][] distance;

    /**
     * List of paths from one pokemon/station to another pokemon/station
     */
    private ArrayList<Path> wholePath;

    {
        wholeList = new ArrayList<>();
        visitList = new ArrayList<>();
        wholePath = new ArrayList<>();
    }

    /**
     * Create map with given row and column
     *
     * @param row row number of map
     * @param col column number of map
     */
    Map(int row, int col) {
        this.MAX_ROW = row;
        this.MAX_COL = col;
        cells = new Cell[row][col];
    }

    public static int getRow() {
        return MAX_ROW;
    }

    public static int getCol() {
        return MAX_COL;
    }

    public Type getCellType(int row, int col) {
        if (cells[row][col] instanceof Station)
            return Type.STATION;
        else if (cells[row][col] instanceof Pokemon)
            return Type.POKEMON;
        else if (cells[row][col].equals(start))
            return Type.START;
        else if (cells[row][col].equals(destination))
            return Type.DESTINATION;
        else if (!cells[row][col].isValid())
            return Type.WALL;
        else
            return Type.PATH;
    }

    /**
     * Convert input character to corresponding type of cell
     *
     * @param row row of given cell
     * @param col column of given cell
     * @param type type of given cell
     */
    void setPath(int row, int col, char type) {
        if (row < MAX_ROW && row >= 0 && col < MAX_COL && col >= 0) {
            switch (type) {
                case WALL:
                    cells[row][col] = new Cell(row, col);
                    cells[row][col].setValid(false);
                    break;
                case START:
                    start = new Cell(row, col);
                    cells[row][col] = start;
                    break;
                case DESTINATION:
                    destination = new Cell(row, col);
                    cells[row][col] = destination;
                    break;
                case PATH:
                    cells[row][col] = new Cell(row, col);
                    break;
            }
        }
    }

    /**
     * Add a station to the visit list
     *
     * @param station station to be added
     */
    void addStation(Station station) {
        visitList.add(station);
        cells[station.getRow()][station.getCol()] = station;
    }

    /**
     * Add a pokemon to the visit list
     *
     * @param pokemon pokemon to be added
     */
    void addPokemon(Pokemon pokemon) {
        visitList.add(pokemon);
        cells[pokemon.getRow()][pokemon.getCol()] = pokemon;
    }

    /**
     * Get the start point for player
     *
     * @return start point
     */
    Cell getStart() {
        return start;
    }

    /**
     * Get the destination point for player
     *
     * @return destination point
     */
    Cell getDestination() {
        return destination;
    }

    /**
     * Get the visit list for player
     *
     * @return visit list
     */
    ArrayList<Cell> getVisitList() {
        return visitList;
    }

    /**
     * Get the distance between two cells. Every cell should be one of station, pokemon, start, or destination
     *
     * @param a cell a
     * @param b cell b
     * @return distance between cell a and cell b
     */
    int getDistance(Cell a, Cell b) {
        return distance[wholeList.indexOf(a)][wholeList.indexOf(b)];
    }

    /**
     * Whole list is consist of all of the pokemon, stations, start and destination
     */
    void setWholeList() {
        wholeList.addAll(visitList);
    }

    /**
     * Check whether the location is valid
     *
     * @param row row of cell
     * @param col column of cell
     * @return whether the location can be passed
     */
    private boolean isValid(int row, int col) {
        return (row >= 0 && row < MAX_ROW && col >= 0 && col < MAX_COL && cells[row][col].isValid());
    }

    /**
     * UP, LEFT, RIGHT, DOWN
     */
    private static final int[] rowNum = {-1, 0, 0, 1};
    private static final int[] colNum = {0, -1, 1, 0};


    /**
     * Use BFS to find the shortest path from pokemon/station A to pokemon/station B
     * Store the shortest paths in wholePath
     */
    void findShortestPath() {
        distance = new int [visitList.size()][visitList.size()];
        for (int i = 0; i < visitList.size(); i++) {
            Path pathI = new Path();
            for (int j = 0; j < visitList.size(); j++) {
                boolean[][] visited = new boolean[MAX_ROW][MAX_COL];
                int rowI = visitList.get(i).getRow();
                int colI = visitList.get(i).getCol();
                visited[rowI][colI] = true;
//                System.out.println(i + ", " + j + ": " + rowI + ", " + colI );

                Queue<Cell> queue = new LinkedList<>();
                queue.add(visitList.get(i));

                while (!queue.isEmpty()) {
                    Cell current = queue.peek();
                    int dist = current.getDist();

                    if (current.equals(visitList.get(j))) {
                        distance[i][j] = dist;
//                        System.out.println(i + " to " + j + ": " + distance[i][j]);
//                        System.out.println(current.getPath().size());
                        pathI.addPath(current.getPath());
                        break;
                    }

                    queue.remove();

                    for (int k = 0; k < 4; k++) {
                        int rowAdj = current.getRow() + rowNum[k];
                        int colAdj = current.getCol() + colNum[k];
                        if (isValid(rowAdj, colAdj) && !visited[rowAdj][colAdj]) {
                            visited[rowAdj][colAdj] = true;
                            Cell adjCell = new Cell(rowAdj, colAdj);
                            adjCell.setDist(dist + 1);
                            adjCell.setPath(current.getPath());
                            adjCell.getPath().add(adjCell);
//                            System.out.println(rowAdj + ", " + colAdj + " visited");
                            queue.add(adjCell);
//                            System.out.println("queue's size:" + queue.size());
                        }
                    }
                }
            }
            wholePath.add(pathI);
        }
    }

    /**
     * Print the path from cell a to cell b
     *
     * @param a cell a
     * @param b cell b
     */
    void printPath(Cell a, Cell b) {
        ArrayList<Cell> path = wholePath.get(wholeList.indexOf(a)).getPath(wholeList.indexOf(b));
        for (int i = 0; i < path.size(); i++) {
            System.out.print("<" + path.get(i).getRow() + "," + path.get(i).getCol() + ">");
            if (i < path.size() - 1) {
                System.out.print("->");
            }
        }
    }

    /**
     * Preparation for writing the path from cell a to cell b into file
     * @param a cell a
     * @param b cell b
     * @return string containing the path from cell a to cell b
     */
    String writePath(Cell a, Cell b) {
        StringBuilder pathString = new StringBuilder();
        ArrayList<Cell> path = wholePath.get(wholeList.indexOf(a)).getPath(wholeList.indexOf(b));
        for (int i = 0; i < path.size(); i++) {
            pathString.append("<").append(path.get(i).getRow()).append(",").append(path.get(i).getCol()).append(">");
            if (i < path.size() - 1) {
                pathString.append("->");
            }
        }
        return pathString.toString();
    }

    ArrayList<Cell> returnPath(Cell a, Cell b) {
        return wholePath.get(wholeList.indexOf(a)).getPath(wholeList.indexOf(b));
    }
}

import java.io.File;
import java.util.*;

public class Map {

    public static final char WALL = '#', PATH = ' ', START = 'B', DESTINATION = 'D', STATION = 'S', POKEMON = 'P';

    private Cell[][] cells;
    private int MAX_ROW;
    private int MAX_COL;
    private Cell start, destination;

    private ArrayList<Cell> visitList;
    private ArrayList<Cell> wholeList;

    private int[][] distance;
    private ArrayList<Path> wholePath;

    {
        wholeList = new ArrayList<>();
        visitList = new ArrayList<>();
        wholePath = new ArrayList<>();
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

    public void addStation(Station station) {
        visitList.add(station);
        cells[station.getRow()][station.getCol()] = station;
    }

    public void addPokemon(Pokemon pokemon) {
        visitList.add(pokemon);
        cells[pokemon.getRow()][pokemon.getCol()] = pokemon;
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

    public int getDistance(Cell a, Cell b) {
        return distance[wholeList.indexOf(a)][wholeList.indexOf(b)];
    }

    public void setWholeList() {
        wholeList.addAll(visitList);
    }

    public boolean isValid(int row, int col) {
        return (row >= 0 && row < MAX_ROW && col >= 0 && col < MAX_COL && cells[row][col].isValid());
    }

    private static final int rowNum[] = {-1, 0, 0, 1};
    private static final int colNum[] = {0, -1, 1, 0};

//    public void findShortestPath() {
//        distance = new int [visitList.size()][visitList.size()];
//        for (int i = 0; i < visitList.size(); i++) {
//            for (int j = 0; j < visitList.size(); j++) {
//                boolean[][] visited = new boolean[MAX_ROW][MAX_COL];
//                int rowI = visitList.get(i).getRow();
//                int colI = visitList.get(i).getCol();
//                visited[rowI][colI] = true;
////                System.out.println(i + ", " + j + ": " + rowI + ", " + colI );
//
//                Queue<Cell> queue = new LinkedList<>();
//                queue.add(visitList.get(i));
//
//                while (!queue.isEmpty()) {
//                    Cell current = queue.peek();
//                    int dist = current.getDist();
//
//                    if (current.equals(visitList.get(j))) {
//                        distance[i][j] = dist;
//                        System.out.println(i + " to " + j + ": " + distance[i][j]);
//                        break;
//                    }
//
//                    queue.remove();
//
//                    for (int k = 0; k < 4; k++) {
//                        int rowAdj = current.getRow() + rowNum[k];
//                        int colAdj = current.getCol() + colNum[k];
//                        if (isValid(rowAdj, colAdj) && !visited[rowAdj][colAdj]) {
//                            visited[rowAdj][colAdj] = true;
//                            Cell adjCell = new Cell(rowAdj, colAdj);
//                            adjCell.setDist(dist + 1);
////                            System.out.println(rowAdj + ", " + colAdj + " visited");
//                            queue.add(adjCell);
////                            System.out.println("queue's size:" + queue.size());
//                        }
//                    }
//                }
//            }
//        }
//    }

        public void findShortestPath() {
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

    public void printPath(Cell a, Cell b) {
        ArrayList<Cell> path = wholePath.get(wholeList.indexOf(a)).getPath(wholeList.indexOf(b));
        for (int i = 0; i < path.size(); i++) {
            System.out.print("<" + path.get(i).getRow() + "," + path.get(i).getCol() + ">");
            if (i < path.size() - 1) {
                System.out.print("->");
            }
        }
    }

    public String writePath(Cell a, Cell b) {
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
}

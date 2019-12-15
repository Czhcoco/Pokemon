import java.util.ArrayList;

public class Cell {

    private int row;
    private int col;

    boolean valid;
    int dist;

    ArrayList<Cell> path;

    {
        path = new ArrayList<>();
    }

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.valid = true;
        this.dist = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public ArrayList<Cell> getPath() {
        return path;
    }

    public void setPath(ArrayList<Cell> path) {
        this.path = new ArrayList<>(path);
    }

    @Override
    public boolean equals(Object o) {
        Cell temp = (Cell) o;
        return temp.row == this.row && temp.col == this.col;
    }

    @Override
    public String toString() {
        return "Cell [row=" + row + ", col=" + col + "]";
    }
}

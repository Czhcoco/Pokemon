public class Cell {

    private int row;
    private int col;

    boolean visited;
    boolean valid;
    int dist;

    Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.visited = false;
        this.valid = true;
        this.dist = 0;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean isVisited() {
        return visited;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getDist() {
        return dist;
    }

    public void setDist(int dist) {
        this.dist = dist;
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

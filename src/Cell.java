import java.util.ArrayList;

public class Cell {

    /**
     * row and column of cell
     */
    private int row;
    private int col;

    /**
     * Whether the cell can be passed
     */
    private boolean valid;

    /**
     * Distance from a given source
     */
    private int dist;

    /**
     * Path from given source to this cell
     */
    private ArrayList<Cell> path;

    {
        path = new ArrayList<>();
    }

    /**
     * Create a cell with specific row and column, set valid to true and dist to 0
     *
     * @param row the row of cell
     * @param col the column of cell
     */
    Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.valid = true;
        this.dist = 0;
    }

    /**
     * Get the row of cell
     *
     * @return Cell's row
     */
    int getRow() {
        return row;
    }

    /**
     * Get the column of cell
     *
     * @return Cell's column
     */
    int getCol() {
        return col;
    }

    /**
     * Check whether the cell can be passes
     *
     * @return false for wall, true otherwise
     */
    boolean isValid() {
        return valid;
    }

    /**
     * Set valid for cell
     *
     * @param valid whether the cell is a wall. False for wall, true otherwise
     */
    void setValid(boolean valid) {
        this.valid = valid;
    }

    /**
     * Get the distance of the cell from known source
     *
     * @return the distance from source
     */
    int getDist() {
        return dist;
    }

    /**
     * Set the distance from this cell to a known source
     *
     * @param dist distance to source
     */
    void setDist(int dist) {
        this.dist = dist;
    }

    /**
     * Get the path from a known source cell to this cell
     *
     * @return path from source cell
     */
    ArrayList<Cell> getPath() {
        return path;
    }

    /**
     * Set the path from a known source cell to this cell
     *
     * @param path path from source cell
     */
    void setPath(ArrayList<Cell> path) {
        this.path = new ArrayList<>(path);
    }

    /**
     * Check whether two cell are in the same place
     *
     * @param o object to be compared
     * @return true for same place, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        Cell temp = (Cell) o;
        return temp.row == this.row && temp.col == this.col;
    }

//    @Override
//    public String toString() {
//        return "Cell [row=" + row + ", col=" + col + "]";
//    }
}

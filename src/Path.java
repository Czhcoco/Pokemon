import java.util.ArrayList;

class Path {
    private ArrayList<ArrayList<Cell>> paths;

    {
        paths = new ArrayList<>();
    }

    /**
     * Add path from this pokemon/station to another pokemon/station
     * @param path path from this pokemon/station to another pokemon/station
     */
    void addPath(ArrayList<Cell> path) {
        paths.add(path);
    }

    /**
     * Get the path from this pokemon/station to a given pokemon/station
     *
     * @param i the index of the specified pokemon/station in the wholeList
     * @return the path from this pokemon/station to another given pokemon/station
     */
    ArrayList<Cell> getPath(int i) {
        return paths.get(i);
    }
}

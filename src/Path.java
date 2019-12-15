import java.util.ArrayList;

public class Path {
    private ArrayList<ArrayList<Cell>> paths;

    {
        paths = new ArrayList<>();
    }

    public void addPath(ArrayList<Cell> path) {
        paths.add(path);
    }

    public ArrayList<Cell> getPath(int i) {
        return paths.get(i);
    }
}

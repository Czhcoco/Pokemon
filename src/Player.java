import java.util.ArrayList;

public class Player {

    private int row;
    private int col;
    private ArrayList<Pokemon> pokemonCaught;
    private ArrayList<Cell> path;
    private ArrayList<String> caughtTypes;
    private int ballInBag;
    private int steps;
    private int maxCP;

    {
        pokemonCaught = new ArrayList<Pokemon>();
        path = new ArrayList<Cell>();
        caughtTypes = new ArrayList<String>();
    }

    public Player(int row, int col) {
        this.row = row;
        this.col = col;
        steps = 0;
        ballInBag = 0;
        maxCP = 0;
    }

    public Player(Player player) {
        this.row = player.row;
        this.col = player.col;
        this.ballInBag = player.ballInBag;
        this.maxCP = player.maxCP;
        this.pokemonCaught = new ArrayList<>(player.pokemonCaught);
        this.path = new ArrayList<>(player.path);
        this.caughtTypes = new ArrayList<>(player.caughtTypes);
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public ArrayList<Pokemon> getPokemonCaught() {
        return pokemonCaught;
    }

    public ArrayList<Cell> getPath() {
        return path;
    }

    public int getMaxCP() {
        return maxCP;
    }

    public void addPath(Cell cell) {
        path.add(cell);
        if (cell instanceof Pokemon) {
            addPokemonToBag((Pokemon) cell);
            changeBallInBag(-((Pokemon) cell).getNumOfRequiredBalls());
        } else if (cell instanceof Station){
            changeBallInBag(((Station) cell).getNumOfBalls());
        }
    }

    public void removePath(Cell cell) {
        path.remove(cell);
        if (cell instanceof Pokemon) {
            removePokemonInBag((Pokemon) cell);
            changeBallInBag(((Pokemon) cell).getNumOfRequiredBalls());
        } else if (cell instanceof Station){
            changeBallInBag(-((Station) cell).getNumOfBalls());
        }
    }

    public ArrayList<String> getCaughtTypes() {
        return caughtTypes;
    }

    public int getSteps() {
        return steps;
    }

    public int getBallInBag() {
        return ballInBag;
    }

    public void changeBallInBag(int ballInBag) {
        this.ballInBag += ballInBag;
    }

    public void addSteps(int steps) {
        this.steps += steps;
    }

    public void addPokemonToBag(Pokemon pokemon) {
        this.pokemonCaught.add(pokemon);
        if (!caughtTypes.contains(pokemon.getType())) {
            caughtTypes.add(pokemon.getType());
        }
        if (pokemon.getCombatPower() > maxCP) {
            maxCP = pokemon.getCombatPower();
        }
    }

    public void removePokemonInBag(Pokemon pokemon) {
        pokemonCaught.remove(pokemon);
        boolean typeRepeated = false;
        for (var c: pokemonCaught) {
            if (c.getType() == pokemon.getType())
                typeRepeated = true;
        }
        if (!typeRepeated) {
            caughtTypes.remove(pokemon.getType());
        }
    }

    public int newType(Pokemon pokemon) {
        if (!caughtTypes.contains(pokemon.getType()))
            return 1;
        return 0;
    }

    public int cpChange(Cell cell) {
        if (cell instanceof Pokemon) {
            if (((Pokemon) cell).getCombatPower() > maxCP)
                return ((Pokemon) cell).getCombatPower() - maxCP;
        }
        return 0;
    }
}

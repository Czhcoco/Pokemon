import java.util.ArrayList;

public class Player {

    public int row;
    public int col;
    public ArrayList<Pokemon> pokemonCaught;
    public ArrayList<Cell> path;
    private ArrayList<String> caughtTypes;
    public int ballInBag;
    public int steps;
    public int maxCP;

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

    public void addPath(Cell cell) {
        path.add(cell);
        if (cell instanceof Pokemon) {
            addPokemonToBag((Pokemon) cell);
        }
    }

    public void removePath(Cell cell) {
        path.remove(cell);
        if (cell instanceof Pokemon) {
            removePokemonInBag((Pokemon) cell);
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

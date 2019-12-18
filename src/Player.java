import java.util.ArrayList;

public class Player {

    /**
     * Location of player, ball in player's bag, and max combat power of pokemon in bag
     */
    private int row;
    private int col;
    private int ballInBag;
    private int maxCP;

    /**
     * List of pokemon caught by player
     */
    private ArrayList<Pokemon> pokemonCaught;

    /**
     * Current path containing only pokemon and station(s)
     */
    private ArrayList<Cell> path;

    /**
     * List of types of pokemon being caught
     */
    private ArrayList<String> caughtTypes;


    {
        pokemonCaught = new ArrayList<>();
        path = new ArrayList<>();
        caughtTypes = new ArrayList<>();
    }

    /**
     * Create a player with given row and column
     * Initialize the number of balls in bag to 0 and max combat power to 0
     *
     * @param row row of player
     * @param col column of player
     */
    Player(int row, int col) {
        this.row = row;
        this.col = col;
        ballInBag = 0;
        maxCP = 0;
    }

    /**
     * Create a player according to a given player
     *
     * @param player player to be copied
     */
    Player(Player player) {
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

    /**
     * Get the list of pokemon caught by the player
     *
     * @return list of pokemon caught by the player
     */
    ArrayList<Pokemon> getPokemonCaught() {
        return pokemonCaught;
    }

    /**
     * Get the path of the player
     *
     * @return current path of the player
     */
    ArrayList<Cell> getPath() {
        return path;
    }

    /**
     * Get the max combat power of pokemon caught by the player
     *
     * @return max combat power
     */
    int getMaxCP() {
        return maxCP;
    }

    /**
     * Add a pokemon/station to path of the player
     *
     * @param cell pokemon/station to be added
     */
    void addPath(Cell cell) {
        path.add(cell);
        if (cell instanceof Pokemon) {
            addPokemonToBag((Pokemon) cell);
            changeBallInBag(-((Pokemon) cell).getNumOfRequiredBalls());
        } else if (cell instanceof Station){
            changeBallInBag(((Station) cell).getNumOfBalls());
        }
    }

//    public void removePath(Cell cell) {
//        path.remove(cell);
//        if (cell instanceof Pokemon) {
//            removePokemonInBag((Pokemon) cell);
//            changeBallInBag(((Pokemon) cell).getNumOfRequiredBalls());
//        } else if (cell instanceof Station){
//            changeBallInBag(-((Station) cell).getNumOfBalls());
//        }
//    }

    /**
     * Get the list of types of pokemon caught by the player
     *
     * @return list of types
     */
    ArrayList<String> getCaughtTypes() {
        return caughtTypes;
    }

    /**
     * Get the number of balls in bag
     *
     * @return number of balls in bag
     */
    int getBallInBag() {
        return ballInBag;
    }

    /**
     * Add/Remove balls in bag
     *
     * @param ballInBag number of balls to be modified
     */
    private void changeBallInBag(int ballInBag) {
        this.ballInBag += ballInBag;
    }

    /**
     * Add a pokemon to bag
     *
     * @param pokemon pokemon to be added
     */
    private void addPokemonToBag(Pokemon pokemon) {
        this.pokemonCaught.add(pokemon);
        if (!caughtTypes.contains(pokemon.getType())) {
            caughtTypes.add(pokemon.getType());
        }
        if (pokemon.getCombatPower() > maxCP) {
            maxCP = pokemon.getCombatPower();
        }
    }

//    public void removePokemonInBag(Pokemon pokemon) {
//        pokemonCaught.remove(pokemon);
//        boolean typeRepeated = false;
//        for (var c: pokemonCaught) {
//            if (c.getType() == pokemon.getType())
//                typeRepeated = true;
//        }
//        if (!typeRepeated) {
//            caughtTypes.remove(pokemon.getType());
//        }
//    }

    /**
     * Check whether the type of pokemon to be caught is a new type
     *
     * @param pokemon pokemon to be caught
     * @return whether it is a new type
     */
    int newType(Pokemon pokemon) {
        if (!caughtTypes.contains(pokemon.getType()))
            return 1;
        return 0;
    }

    /**
     * Calculate the change of maximum combat power if the given cell is passed
     *
     * @param cell cell to be passed
     * @return the change of the maximum combat power
     */
    int cpChange(Cell cell) {
        if (cell instanceof Pokemon) {
            if (((Pokemon) cell).getCombatPower() > maxCP)
                return ((Pokemon) cell).getCombatPower() - maxCP;
        }
        return 0;
    }

    void setPosition(Cell a) {
        this.row = a.getRow();
        this.col = a.getCol();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Station)
            return ((Station) o).getRow() == this.row && ((Station) o).getCol() == this.col;
        if (o instanceof Pokemon)
            return ((Pokemon) o).getRow() == this.row && ((Pokemon) o).getCol() == this.col;
        else if (o instanceof Cell)
            return ((Cell) o).getRow() == this.row && ((Cell) o).getCol() == this.col;
        return false;
    }

    int getScore(Cell cell) {
        if (cell instanceof Station) {
            return ((Station) cell).getNumOfBalls();
        } else if (cell instanceof Pokemon) {
            return 5 + newType(((Pokemon) cell)) * 10 + cpChange(cell) - ((Pokemon) cell).getNumOfRequiredBalls();
        }
        return 0;
    }
}

class Pokemon extends Cell {

    /**
     * Pokemon's name, type, combat power, and number of balls needed to catch it
     */
    private String name;
    private String type;
    private int combatPower;
    private int numOfRequiredBalls;

    /**
     * Create a pokemon with given row, column, name, type, combat power, and number of balls required to catch it
     *
     * @param row row of pokemon
     * @param col column of pokemon
     * @param name name of pokemon
     * @param type type of pokemon
     * @param combatPower combat power of pokemon
     * @param numOfRequiredBalls number of balls required to catch this pokemon
     */
    Pokemon(int row, int col, String name, String type, int combatPower, int numOfRequiredBalls) {
        super(row, col);
        this.name = name;
        this.type = type;
        this.combatPower = combatPower;
        this.numOfRequiredBalls = numOfRequiredBalls;
    }

    /**
     * Get the combat power of pokemon
     *
     * @return combat power of pokemon
     */
    int getCombatPower() {
        return combatPower;
    }

    /**
     * Get the number of balls needed to catch this pokemon
     *
     * @return number of balls required
     */
    int getNumOfRequiredBalls() {
        return numOfRequiredBalls;
    }

    /**
     * Get the type of the pokemon
     *
     * @return type of the pokemon
     */
    String getType() {
        return type;
    }

}

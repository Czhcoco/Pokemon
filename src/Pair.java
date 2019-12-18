class Pair {
    private int score;
    private Player player;

    /**
     * Create a pair of given score and player
     *
     * @param score score of current path
     * @param player current player
     */
    Pair(int score, Player player) {
        this.score = score;
        this.player = new Player(player);
    }

    /**
     * Get the current player
     *
     * @return current player
     */
    Player getPlayer() {
        return player;
    }

    /**
     * Get the score of this player
     *
     * @return the score of this player
     */
    int getScore() {
        return score;
    }

    /**
     * Set the score of this player
     *
     * @param score score to be set
     */
    void setScore(int score) {
        this.score = score;
    }
}
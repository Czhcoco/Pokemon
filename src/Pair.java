class Pair {
    private int score;
    private Player player;

    Pair(int score, Player player) {
        this.score = score;
        this.player = new Player(player);
    }

    Player getPlayer() {
        return player;
    }

    int getScore() {
        return score;
    }

    void setScore(int score) {
        this.score = score;
    }
}
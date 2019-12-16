class Station extends Cell {

    private int numOfBalls;

    /**
     * Create a station with given row, column and number of balls
     *
     * @param row the row of station
     * @param col the column of station
     * @param numOfBalls number of balls in station
     */
    Station(int row, int col, int numOfBalls) {
        super(row, col);
        this.numOfBalls = numOfBalls;
    }

    /**
     * Get number of balls in the station
     *
     * @return number of balls
     */
    int getNumOfBalls() {
        return numOfBalls;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (!super.equals(obj))
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        Station other = (Station) obj;
//        if (numOfBalls != other.numOfBalls)
//            return false;
//        return true;
//    }

//    @Override
//    public String toString() {
//        return "Station [" + super.toString() +" numPokeBalls=" + numOfBalls + "]";
//    }
}

public class Station extends Cell {

    private int numOfBalls;

    Station(int row, int col, int numOfBalls) {
        super(row, col);
        this.numOfBalls = numOfBalls;
    }

    public int getNumOfBalls() {
        return numOfBalls;
    }

    public void setNumOfBalls(int numOfBalls) {
        this.numOfBalls = numOfBalls;
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

    @Override
    public String toString() {
        return "Station [" + super.toString() +" numPokeBalls=" + numOfBalls + "]";
    }
}

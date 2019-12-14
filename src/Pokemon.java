public class Pokemon extends Cell {
    private String name;
    private String type;
    private int combatPower;
    private int numOfRequiredBalls;

    Pokemon(int row, int col, String name, String type, int combatPower, int numOfRequiredBalls) {
        super(row, col);
        this.name = name;
        this.type = type;
        this.combatPower = combatPower;
        this.numOfRequiredBalls = numOfRequiredBalls;
    }

    public int getCombatPower() {
        return combatPower;
    }

    public int getNumOfRequiredBalls() {
        return numOfRequiredBalls;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public void setCombatPower(int combatPower) {
        this.combatPower = combatPower;
    }

    public void setNumOfRequiredBalls(int numOfRequiredBalls) {
        this.numOfRequiredBalls = numOfRequiredBalls;
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj)
//            return true;
//        if (!super.equals(obj))
//            return false;
//        if (getClass() != obj.getClass())
//            return false;
//        Pokemon other = (Pokemon) obj;
//        if (combatPower != other.combatPower)
//            return false;
//        if (name == null) {
//            if (other.name != null)
//                return false;
//        } else if (!name.equals(other.name))
//            return false;
//        if (numOfRequiredBalls != other.numOfRequiredBalls)
//            return false;
//        if (type == null) {
//            if (other.type != null)
//                return false;
//        } else if (!type.equals(other.type))
//            return false;
//        return true;
//    }

    public String toString() {
        return "Pokemon [name=" + name + ", types=" + type + ", cp=" + combatPower + ", numRequiredBalls="
                + numOfRequiredBalls + ", " + super.toString() + "]";
    }
}

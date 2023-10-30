import java.util.Objects;

public class OrderedPair {
    private final int first;
    private final int second;

    public OrderedPair(int first, int second) {
        this.first = first;
        this.second = second;
    }

    public int getFirst() {
        return first;
    }

    public int getSecond() {
        return second;
    }

    public String toString() {
        return "(" + first + ", " + second + ")";
    }

    @Override
    public boolean equals(Object other){
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()){
            return false;
        }
        OrderedPair test = (OrderedPair) other;
        return first == test.first && second == test.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }


}

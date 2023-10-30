
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

    public boolean equals(OrderedPair other){
        return (first == other.getFirst()) && (second == other.getSecond());
    }
}

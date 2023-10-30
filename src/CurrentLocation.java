import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CurrentLocation {
    private final Map<OrderedPair, Double> currentLocation = new HashMap<>();

    public CurrentLocation(ArrayList<OrderedPair> locOneMan){
        double value = (double) 1 / locOneMan.size();

        for(OrderedPair pairs : locOneMan) {
            currentLocation.put(pairs, value);
        }
    }


    public Map<OrderedPair, Double> getCurrentLocation() {
        return currentLocation;
    }
    public Double getCurrProbability(OrderedPair key){return currentLocation.get(key);}
    public int length() {
        return currentLocation.size();
    }
}

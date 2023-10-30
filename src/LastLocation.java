import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LastLocation {
    private Map<OrderedPair, Double> lastLocation = new HashMap<>();
    private int gridSize;

    public LastLocation (int gridSize, ArrayList<OrderedPair> allPosLocations){
        double initialProb = (double) 1 / (gridSize * gridSize);

        int i = 0;
        for (int r = 0; r < gridSize; r++){
            for (int c = 0; c < gridSize; c++){
                lastLocation.put(allPosLocations.get(i), initialProb);
                i++;
            }
        }
    }
    public Map<OrderedPair, Double> getDistribution(){
        return lastLocation;
    }

    public int getGridSize() {
        return gridSize;
    }

    public Map<OrderedPair, Double> setLastLocation(LastLocation newDistribution) {
        lastLocation = newDistribution.getDistribution();
        return lastLocation;
    }

    // have to calculate which part of the allPosLocations array this is a part of
    public Double getProbability(OrderedPair key) {
        return lastLocation.get(key);
    }

}

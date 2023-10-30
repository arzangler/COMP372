import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SoundSensor {
    private Map<OrderedPair, Double> soundSensor = new HashMap<>();

    public SoundSensor (int gridSize, OrderedPair currLoc) {
        ArrayList<OrderedPair> oneManAway = Main.possibleLocationsOneManhattan(gridSize, currLoc);
        ArrayList<OrderedPair> twoManAway = Main.possibleLocationsTwoManhattan(gridSize, currLoc);

        soundSensor.put(currLoc, 0.6);

        for (OrderedPair pair : oneManAway){
            soundSensor.put(pair, .3 / oneManAway.size());
        }

        for (OrderedPair pair : twoManAway){
            soundSensor.put(pair, .1 / twoManAway.size());
        }
    }
    public Double getSensorProb(OrderedPair key){return soundSensor.get(key);}
    public Map<OrderedPair, Double> getSoundSensor() {
        return soundSensor;
    }




}

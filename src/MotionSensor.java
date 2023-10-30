public class MotionSensor {
    private double trueProb;

    public MotionSensor(boolean M1, OrderedPair currLoc, int gridSize){
        if (M1){
            motionSensorM1(currLoc);
        }
        else {
            motionSensorM2(currLoc, gridSize);
        }
    }
    private void motionSensorM1(OrderedPair currLoc){
        // M1 is located top left (0,0)
        // Get distance from sensor
        if (currLoc.getFirst() == 0){
            int distance = currLoc.getSecond();
            trueProb = 0.9 - 0.1 * distance;
        }
        else if (currLoc.getSecond() == 0){
            int distance = currLoc.getFirst();
            trueProb = 0.9 - 0.1 * distance;
        }
        else {
            trueProb = 0.05;
        }

    }

    private void motionSensorM2(OrderedPair currLoc, int gridSize) {
        // M2 is located at (gridSize - 1, gridSize - 1)
        // get distance from sensor
        if (currLoc.getFirst() == gridSize - 1) {
            int distance = (gridSize - 1) - currLoc.getSecond();
            trueProb = 0.9 - 0.1 * distance;
        }
        else if (currLoc.getSecond() == gridSize - 1){
            int distance = (gridSize - 1) - currLoc.getFirst();
            trueProb = 0.9 - 0.1 * distance;
        }
        else{
            trueProb = 0.05;
        }
    }

    public double getTrueProb() {
        return trueProb;
    }
}

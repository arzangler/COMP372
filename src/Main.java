import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        // read files
        String fileName = "src\\monkey1.txt"; // change for dif tests or for final project

        // Create the grid
        double[][] grid = getGrid(fileName);
        System.out.println(Arrays.deepToString(grid));

        // test LastLocation on initialization

        ArrayList<OrderedPair> allPosLocations = allPossibleLocations(grid.length);
        LastLocation lastLTest = new LastLocation(grid.length, allPosLocations);



        // TODO DEBUGGING FILL OUT ALONG THE WAY.
        System.out.println("Last location distribution:");
        for (int i = 0; i < (grid.length * grid.length); i++){
            double lastDist = lastLTest.getProbability(allPosLocations.get(i));
            System.out.printf("Last location: " + allPosLocations.get(i) + ", prob: %.8f", lastDist);
            System.out.println();
        }

        System.out.println();
        System.out.println("Current location Distribution:");
        for (int i = 0; i < (grid.length * grid.length); i++){
            System.out.println("Last location: " + allPosLocations.get(i));
            ArrayList<OrderedPair> oneManAway = possibleLocationsOneManhattan(grid.length, allPosLocations.get(i));
            CurrentLocation currLocDistribution = new CurrentLocation(oneManAway);
            for (OrderedPair pair : oneManAway) {
                double currDist = currLocDistribution.getCurrProbability(pair);
                System.out.printf(" Current location: " + pair.toString() + ", prob: %.8f", currDist);
                System.out.println();
            }
        }

        System.out.println();
        System.out.println("Motion sensor #1 (top left) distribution");
        // go through all posible locations
        for (OrderedPair pair : allPosLocations){
            MotionSensor m1distribution = new MotionSensor(true, pair, grid.length);
            double trueProbM1 = m1distribution.getTrueProb();
            double falseProbM1 = 1-trueProbM1;
            System.out.printf("Current location: " + pair.toString() + ", true prob: " + "%.8f" + ", false prob: " + "%.8f", trueProbM1, falseProbM1);
            System.out.println();
        }

        System.out.println();
        System.out.println("Motion sensor #2 (bottom right) distribution");
        // go through all locations
        for (OrderedPair pair : allPosLocations){
            MotionSensor m1distribution = new MotionSensor(false, pair, grid.length);
            double trueProbM1 = m1distribution.getTrueProb();
            double falseProbM1 = 1-trueProbM1;
            System.out.printf("Current location: " + pair.toString() + ", true prob: " + "%.8f" + ", false prob: " + "%.8f", trueProbM1, falseProbM1);
            System.out.println();
        }

        System.out.println();
        System.out.println("Sound distribution: ");
        // go through all locations
        for (OrderedPair pair : allPosLocations) {
            SoundSensor sensorDist = new SoundSensor(grid.length, pair);
            Map<OrderedPair, Double> sensorProb = sensorDist.getSoundSensor();
            System.out.println("Current location: " + pair.toString());

            for (Map.Entry<OrderedPair, Double> entry: sensorProb.entrySet()){
                double val = entry.getValue();
                System.out.printf(" Sound reported at: " + entry.getKey().toString() + ", prob: %.8f", val);
                System.out.println();
            }
        }

        // this part should be outside of the debugging loop but it should then rerun when the next line is read.\
        for (Map.Entry<OrderedPair, Double> entry : lastLTest.getDistribution().entrySet()){
            grid[entry.getKey().getFirst()][entry.getKey().getSecond()] = entry.getValue();
        }
        for (int i = 0; i < grid.length; i++){
            for (int j = 0; j < grid.length; j++){
                double value = grid[i][j];
                System.out.printf("%.8f ", value);
            }
            System.out.println();
        }


        /*
        System.out.println("printing all possible locations");
        // ArrayList<OrderedPair> allPosLocations = allPossibleLocations(grid.length);
        listToString(allPosLocations);

        System.out.println("printing all locations one manhattan distance away:");
        OrderedPair test0 = new OrderedPair(1,2);
        ArrayList<OrderedPair> locationsOneManhattan = possibleLocationsOneManhattan(grid.length, test0);
        listToString(locationsOneManhattan);

        System.out.println("Printing all locations two manhattan distance away");
        OrderedPair test1 = new OrderedPair(2,1);
        ArrayList<OrderedPair> locationsTwoManhattan = possibleLocationsTwoManhattan(grid.length, test1);
        listToString(locationsTwoManhattan);

         */


    }


    /**
     * @param fileName Name of the monkey file
     * @return grid as a 2d double array
     * @throws IOException in case the file name cannot be found
     */
    public static double[][] getGrid(String fileName) throws IOException {
        FileInputStream fstream = new FileInputStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;

        strLine = br.readLine();
        String[] parsedLine = strLine.split(" ");
        int row = Integer.parseInt(parsedLine[0]);
        int col = Integer.parseInt(parsedLine[1]);

        return new double[row][col];
    }

    public static ArrayList<OrderedPair> allPossibleLocations(int gridSize) {
        ArrayList<OrderedPair> list = new ArrayList<>();

        for(int i = 0; i < gridSize; i++){
            for (int j = 0; j < gridSize; j++){
                list.add(new OrderedPair(i, j));
            }
        }
        return list;
    }

    public static ArrayList<OrderedPair> possibleLocationsOneManhattan(int gridSize, OrderedPair orderedPair){
        ArrayList<OrderedPair> list = new ArrayList<>();
        int r = orderedPair.getFirst();
        int c = orderedPair.getSecond();

        // check if square above is legal
        if (c-1 >= 0) {
            list.add(new OrderedPair(r, c-1));
        }
        if (r+1 < gridSize){
            list.add(new OrderedPair(r+1, c));
        }
        if (c+1 < gridSize) {
            list.add(new OrderedPair(r, c+1));
        }
        if (r-1 >= 0){
            list.add(new OrderedPair(r-1, c));
        }

        return list;
    }

    public static ArrayList<OrderedPair> possibleLocationsTwoManhattan(int gridSize, OrderedPair orderedPair){
        ArrayList<OrderedPair> list = new ArrayList<>();
        int r = orderedPair.getFirst();
        int c = orderedPair.getSecond();

        for (int x = 0; x < gridSize; x++){
            for (int y = 0; y < gridSize; y++){
                int distance = Math.abs(r - x) + Math.abs(c - y);
                if (distance == 2){
                    list.add(new OrderedPair(x, y));
                }
            }
        }

        return list;
    }

    public static void listToString(ArrayList<OrderedPair> list) {
        for (OrderedPair pair : list){
            System.out.println("(" + pair.getFirst() + ", " + pair.getSecond() + ")");
        }
    }





    /* consider defining this to read the next lines of the file to get sensor outputs for each stage.
    public static void readNextLine(String fileName){

    }
     */
}

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // read files
        System.out.print("Which number monkey file do you want?(1,2,3,4) ");
        String fileNum = scanner.nextLine();
        int fileNumAsNum = Integer.parseInt(fileNum);

        String fileName = "src\\monkey"+ fileNumAsNum + ".txt";

        // Create the grid
        FileInputStream fstream = new FileInputStream(fileName);
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
        String strLine;

        strLine = br.readLine();
        String[] parsedLine = strLine.split(" ");
        int row = Integer.parseInt(parsedLine[0]);
        int col = Integer.parseInt(parsedLine[1]);

        double[][] grid = new double[row][col];


        ArrayList<OrderedPair> allPosLocations = allPossibleLocations(grid.length);

            LastLocation lastLTest = new LastLocation(grid.length, allPosLocations);

            System.out.print("Do you want debugging?(y/n) ");
            String debug = scanner.nextLine();
            boolean d = debug.equals("y");

            if (d) {
                //DEBUGGING
                System.out.println("Last location distribution:");
                for (int i = 0; i < (grid.length * grid.length); i++) {
                    double lastDist = lastLTest.getProbability(allPosLocations.get(i));
                    System.out.printf("Last location: " + allPosLocations.get(i) + ", prob: %.8f", lastDist);
                    System.out.println();
                }

                System.out.println();
                System.out.println("Current location Distribution:");
                for (int i = 0; i < (grid.length * grid.length); i++) {
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
                for (OrderedPair pair : allPosLocations) {
                    MotionSensor m1distribution = new MotionSensor(true, pair, grid.length);
                    double trueProbM1 = m1distribution.getTrueProb();
                    double falseProbM1 = 1 - trueProbM1;
                    System.out.printf("Current location: " + pair.toString() + ", true prob: " + "%.8f" + ", false prob: " + "%.8f", trueProbM1, falseProbM1);
                    System.out.println();
                }

                System.out.println();
                System.out.println("Motion sensor #2 (bottom right) distribution");
                // go through all locations
                for (OrderedPair pair : allPosLocations) {
                    MotionSensor m1distribution = new MotionSensor(false, pair, grid.length);
                    double trueProbM1 = m1distribution.getTrueProb();
                    double falseProbM1 = 1 - trueProbM1;
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

                    for (Map.Entry<OrderedPair, Double> entry : sensorProb.entrySet()) {
                        double val = entry.getValue();
                        System.out.printf(" Sound reported at: " + entry.getKey().toString() + ", prob: %.8f", val);
                        System.out.println();
                    }
                }

                for (Map.Entry<OrderedPair, Double> entry : lastLTest.getDistribution().entrySet()) {
                    grid[entry.getKey().getFirst()][entry.getKey().getSecond()] = entry.getValue();
                }

                System.out.println();
                System.out.println("Initial distribution of monkey's last location: ");

                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid.length; j++) {
                        double value = grid[i][j];
                        System.out.printf("%.8f ", value);
                    }
                    System.out.println();
                }
                System.out.println();


                strLine = br.readLine();
                int step = 0;

                while (strLine != null) {
                    String[] parseLine = strLine.split(" ");
                    boolean m1 = Integer.parseInt(parseLine[0]) == 1;
                    boolean m2 = Integer.parseInt(parseLine[1]) == 1;
                    OrderedPair sound = new OrderedPair(Integer.parseInt(parseLine[2]), Integer.parseInt(parseLine[3]));

                    System.out.println("Obervation: Motion1: " + m1 + ", Motion2: " + m2 + ", Sound Location: " + sound.toString());
                    System.out.println("Monkey's predicted current location at time step: " + step);
                    // go through all values
                    double[][] addedValues = new double[grid.length][grid.length];
                    for (OrderedPair pair : allPosLocations) {
                        System.out.println("  Calculating total prob for current location " + pair.toString());
                        ArrayList<Double> currLocValues = new ArrayList<>();
                        for (Map.Entry<OrderedPair, Double> entry : lastLTest.getDistribution().entrySet()) {
                            double total = 1.0;
                            System.out.print("Probs being multiplied for last location" + entry.getKey() + ": ");
                            System.out.print(entry.getValue() + " ");
                            total *= entry.getValue();

                            // get curr value
                            ArrayList<OrderedPair> oneManAway = possibleLocationsOneManhattan(grid.length, entry.getKey());
                            CurrentLocation currLocDistribution = new CurrentLocation(oneManAway);
                            if (currLocDistribution.getCurrentLocation().containsKey(pair)) {
                                System.out.print(currLocDistribution.getCurrProbability(pair) + " ");
                                total *= currLocDistribution.getCurrProbability(pair);
                            } else {
                                System.out.print("0 ");
                                total *= 0;
                            }

                            // get motion sensor1 values
                            MotionSensor motion1 = new MotionSensor(true, pair, grid.length);
                            double trueProbM1 = motion1.getTrueProb();
                            if (m1) {
                                System.out.print(trueProbM1 + " ");
                                total *= trueProbM1;
                            } else {
                                System.out.print(1 - trueProbM1 + " ");
                                total *= 1 - trueProbM1;
                            }
                            // motion sensor 2 values
                            MotionSensor motion2 = new MotionSensor(false, pair, grid.length);
                            double trueProbM2 = motion2.getTrueProb();
                            if (m2) {
                                System.out.print(trueProbM2 + " ");
                                total *= trueProbM2;
                            } else {
                                System.out.print(1 - trueProbM2 + " ");
                                total *= 1 - trueProbM2;
                            }

                            // sound sensor values need to check if 0 !
                            SoundSensor soundSensor = new SoundSensor(grid.length, pair);
                            if (soundSensor.getSensorProb(sound) != null) {
                                double sensorVal = soundSensor.getSensorProb(sound);
                                System.out.print(sensorVal + " ");
                                total *= sensorVal;
                            } else {
                                System.out.print("0 ");
                                total *= 0;
                            }

                            currLocValues.add(total);
                            System.out.println();

                        }

                        // Sum totals together
                        double totalTotals = 0.0;
                        for (double val : currLocValues) {
                            totalTotals += val;
                        }
                        //add to addedValues grid
                        int x = pair.getFirst();
                        int y = pair.getSecond();
                        addedValues[x][y] = totalTotals;
                        System.out.println();
                    }

                    System.out.println("Before normalization:");
                    double normalizationConstant = 0.0;
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid.length; j++) {
                            double value = addedValues[i][j];
                            normalizationConstant += value;
                            System.out.printf("%.8f ", value);
                        }
                        System.out.println();
                    }

                    System.out.println();
                    System.out.println("After normalization: ");
                    Map<OrderedPair, Double> newLastLocationMap = new HashMap<>();
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid.length; j++) {
                            double value = addedValues[i][j];
                            value /= normalizationConstant;
                            newLastLocationMap.put(new OrderedPair(i, j), value);
                            System.out.printf("%.8f ", value);
                        }
                        System.out.println();
                    }
                    System.out.println();

                    lastLTest.setLastLocation(newLastLocationMap);

                    step++;
                    strLine = br.readLine();
                }
            }

            else{ // todo NO DEBUGGING
                for (Map.Entry<OrderedPair, Double> entry : lastLTest.getDistribution().entrySet()) {
                    grid[entry.getKey().getFirst()][entry.getKey().getSecond()] = entry.getValue();
                }
                System.out.println("Initial distribution of monkey's last location: ");
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid.length; j++) {
                        double value = grid[i][j];
                        System.out.printf("%.8f ", value);
                    }
                    System.out.println();
                }
                System.out.println();

                strLine = br.readLine();
                int step = 0;

                while (strLine != null) {
                    String[] parseLine = strLine.split(" ");
                    boolean m1 = Integer.parseInt(parseLine[0]) == 1;
                    boolean m2 = Integer.parseInt(parseLine[1]) == 1;
                    OrderedPair sound = new OrderedPair(Integer.parseInt(parseLine[2]), Integer.parseInt(parseLine[3]));

                    System.out.println("Obervation: Motion1: " + m1 + ", Motion2: " + m2 + ", Sound Location: " + sound.toString());
                    System.out.println("Monkey's predicted current location at time step: " + step);
                    // go through all values
                    double[][] addedValues = new double[grid.length][grid.length];
                    for (OrderedPair pair : allPosLocations) {
                        ArrayList<Double> currLocValues = new ArrayList<>();
                        for (Map.Entry<OrderedPair, Double> entry : lastLTest.getDistribution().entrySet()) {
                            double total = 1.0;
                            total *= entry.getValue();

                            // get curr value
                            ArrayList<OrderedPair> oneManAway = possibleLocationsOneManhattan(grid.length, entry.getKey());
                            CurrentLocation currLocDistribution = new CurrentLocation(oneManAway);
                            if (currLocDistribution.getCurrentLocation().containsKey(pair)) {
                                total *= currLocDistribution.getCurrProbability(pair);
                            } else {
                                total *= 0;
                            }

                            // get motion sensor1 values
                            MotionSensor motion1 = new MotionSensor(true, pair, grid.length);
                            double trueProbM1 = motion1.getTrueProb();
                            if (m1) {
                                total *= trueProbM1;
                            } else {
                                total *= 1 - trueProbM1;
                            }
                            // motion sensor 2 values
                            MotionSensor motion2 = new MotionSensor(false, pair, grid.length);
                            double trueProbM2 = motion2.getTrueProb();
                            if (m2) {
                                total *= trueProbM2;
                            } else {
                                total *= 1 - trueProbM2;
                            }

                            // sound sensor values need to check if 0 !
                            SoundSensor soundSensor = new SoundSensor(grid.length, pair);
                            if (soundSensor.getSensorProb(sound) != null) {
                                double sensorVal = soundSensor.getSensorProb(sound);
                                total *= sensorVal;
                            } else {
                                total *= 0;
                            }

                            currLocValues.add(total);

                        }

                        // Sum totals together
                        double totalTotals = 0.0;
                        for (double val : currLocValues) {
                            totalTotals += val;
                        }
                        //add to addedValues grid
                        int x = pair.getFirst();
                        int y = pair.getSecond();
                        addedValues[x][y] = totalTotals;
                    }

                    double normalizationConstant = 0.0;
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid.length; j++) {
                            double value = addedValues[i][j];
                            normalizationConstant += value;
                        }
                    }

                    Map<OrderedPair, Double> newLastLocationMap = new HashMap<>();
                    for (int i = 0; i < grid.length; i++) {
                        for (int j = 0; j < grid.length; j++) {
                            double value = addedValues[i][j];
                            value /= normalizationConstant;
                            newLastLocationMap.put(new OrderedPair(i, j), value);
                            System.out.printf("%.8f ", value);
                        }
                        System.out.println();
                    }
                    System.out.println();

                    // todo change last location map.
                    lastLTest.setLastLocation(newLastLocationMap);

                    step++;
                    strLine = br.readLine();
                }
            }
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

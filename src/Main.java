import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        // read files
        String fileName = "src\\monkey4.txt"; // change for dif tests or for final project

        // Create the grid
        double[][] grid = getGrid(fileName);
        System.out.println(Arrays.deepToString(grid));

        System.out.println("printing all possible locations");
        ArrayList<OrderedPair> allPosLocations = allPossibleLocations(grid.length);
        listToString(allPosLocations);

        System.out.println("printing all locations one manhattan distance away:");
        OrderedPair test0 = new OrderedPair(1,2);
        ArrayList<OrderedPair> locationsOneManhattan = possibleLocationsOneManhattan(grid.length, test0);
        listToString(locationsOneManhattan);

        System.out.println("Printing all locations two manhattan distance away");
        OrderedPair test1 = new OrderedPair(2,1);
        ArrayList<OrderedPair> locationsTwoManhattan = possibleLocationsTwoManhattan(grid.length, test1);
        listToString(locationsTwoManhattan);


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

        // check if square above is legal
        if (c-2 >= 0) {
            list.add(new OrderedPair(r, c-2));
        }
        if (r+2 < gridSize){
            list.add(new OrderedPair(r+2, c));
        }
        if (c+2 < gridSize) {
            list.add(new OrderedPair(r, c+2));
        }
        if (r-2 >= 0){
            list.add(new OrderedPair(r-2, c));
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

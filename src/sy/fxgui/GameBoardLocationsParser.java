package sy.fxgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

class GameBoardLocationsParser {
    private static final String positionsFile = "src/resources/scotpos.txt";

    //Read the cell coordinates file
    public static void fetchPointsInto(ArrayList<Cell> cellList) {
        String readLine = "";
        int i = 1;
        cellList.add(null);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(positionsFile));
            while ((readLine = reader.readLine()) != null) {
                String[] readStrings = readLine.split(" ");
                cellList.add(new Cell(
                        i++,
                        Double.parseDouble(readStrings[1]),
                        Double.parseDouble(readStrings[2])
                ));
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}

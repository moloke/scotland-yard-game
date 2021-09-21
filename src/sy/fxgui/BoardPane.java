package sy.fxgui;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.util.ArrayList;

class BoardPane extends StackPane {
    private ImageView boardBackground;

    public BoardPane(ArrayList<Cell> cellList) throws Exception {
        this.setBoardBackground(new ImageView(
                new Image(
                        new FileInputStream("src/resources/boardmap.png")
                )
        )); // Load the map .png

        this.getBoardBackground().setPreserveRatio(true);

        this.minWidth(1018);
        this.minHeight(764);
        this.maxWidth(1018);
        this.maxHeight(764); // Ensure it maintains correct size

        this.getChildren().addAll(getBoardBackground());

        displayCells(cellList);
    }

    public void displayCells(ArrayList<Cell> cellList) throws Exception {
        for (Cell cell : cellList) {
            if (cell == null) { //First cell is null to start indexes from 1 for easy of use
                continue;
            }

            cell.setTranslateX(cell.getCellLocation().getX() - 507); //Set offset so cells are aligned correctly. This is a magic number from testing.
            cell.setTranslateY(cell.getCellLocation().getY() - 385);

            cell.minWidth(32);
            cell.minHeight(32);
            cell.maxWidth(32);
            cell.maxHeight(32);
            cell.prefHeight(32);
            cell.prefHeight(32);
            cell.setMaxSize(32, 32); //Enforce stackpane size to get events from it more easily

            setAlignment(cell, Pos.CENTER);

            cell.relocate(cell.getCellLocation().getX(), cell.getCellLocation().getY());

            this.getChildren().add(cell);

        }
    }

    public ImageView getBoardBackground() {
        return boardBackground;
    }

    public void setBoardBackground(ImageView boardBackground) {
        this.boardBackground = boardBackground;
    }
}

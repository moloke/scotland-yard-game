package sy.fxgui;

import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

class MrXScrollPane extends ScrollPane {
    private ArrayList<MrXMoveTemplate> movesList;
    private final VBox movesVBox;

    //Initialize the scroll pane and fill it with templates.
    public MrXScrollPane() throws Exception {
        movesVBox = new VBox();
        movesVBox.setSpacing(10);
        movesVBox.setPadding(new Insets(10, 10, 10, 10));

        setMovesList(new ArrayList<>());

        MrXMoveTemplate temp;

        for (int i = 1; i <= 24; i++) {
            temp = new MrXMoveTemplate(i,
                    (i == 3 || i == 8 || i == 13 || i == 18 || i == 24) ? "?" : "");
            getMovesList().add(temp);
            movesVBox.getChildren().addAll(temp);
        }

        this.setContent(movesVBox);
    }

    public ArrayList<MrXMoveTemplate> getMovesList() {
        return movesList;
    }

    public void setMovesList(ArrayList<MrXMoveTemplate> movesList) {
        this.movesList = movesList;
    }
}

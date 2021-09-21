package sy.fxgui;

import sy.game.Action;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileInputStream;

class MrXMoveTemplate extends HBox {
    private final String path = "src/resources/";
    private Text position;
    private final VBox textVBox;
    private final Text turnNumber;
    private ImageView flavorImage;


    //Initialize the moves template
    public MrXMoveTemplate(int turnNumber, String position) throws Exception {
        textVBox = new VBox();
        this.turnNumber = new Text("Turn " + turnNumber);
        this.setPosition(new Text(position));
        this.getPosition().setFont(Font.font("Arial", FontWeight.BOLD, 16));

        textVBox.getChildren().addAll(this.turnNumber, this.getPosition());
        textVBox.setSpacing(10);

        flavorImage = new ImageView(
                new Image(
                        new FileInputStream(path + "undone.png")
                )
        );

        this.setSpacing(10);
        this.setPadding(new Insets(10, 10, 10, 10));
        this.getChildren().addAll(textVBox, flavorImage);
    }

    //Change the icon displayed depending on move
    public void changeMoveDone(Action.Transportation action) throws Exception {
        this.getChildren().remove(flavorImage);
        flavorImage = new ImageView(
                new Image(
                        new FileInputStream(path + action.name().toLowerCase() + ".png")
                )
        );
        this.getChildren().add(flavorImage);
    }

    public Text getPosition() {
        return position;
    }

    public void setPosition(Text position) {
        this.position = position;
    }
}

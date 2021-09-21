package sy.fxgui;

import sy.players.Seeker;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

class PlayerStatusTemplate extends VBox {
    private final String path = "src/resources/";
    private Text playerName;
    private final ImageView flavorImage;
    private Text taxiCount;
    private Text busCount;
    private Text undergroundCount;

    private Text blackFareCount;
    private Text doubleMoveCount;

    private VBox statsVBox;
    private HBox picStatsHBox;

    private boolean shown;

    //Constructor for detectives
    public PlayerStatusTemplate(String name, int taxi, int bus, int underGround, Seeker.Color color) throws Exception {
        //detective
        flavorImage = new ImageView(
                new Image(
                        new FileInputStream(path + color.name().toLowerCase() + "seeker.jpg")
                )
        );

        createPlayer(name, taxi, bus, underGround, true);

    }

    //Constructor for Mr. X
    public PlayerStatusTemplate(int taxi, int bus, int underGround, int blackFare, int doubleMove, boolean shown) throws Exception {
        //Mr. X
        flavorImage = new ImageView(
                new Image(
                        new FileInputStream(path + "mrx.jpg")
                )
        );

        createPlayer("Mr. X", taxi, bus, underGround, shown);

        if (shown) {
            blackFareCount = new Text(blackFare + "");
            blackFareCount.setFill(Color.BLACK);
            doubleMoveCount = new Text(doubleMove + "");
            doubleMoveCount.setFill(Color.CRIMSON);

            statsVBox.getChildren().addAll(blackFareCount, doubleMoveCount);
        }

    }

    //Common operations between the constructors
    private void createPlayer(String name, int taxi, int bus, int underGround, boolean shown) throws FileNotFoundException {
        statsVBox = new VBox();
        playerName = new Text(name);
        this.shown = shown;

        if (shown) {

            taxiCount = new Text(taxi + "");
            taxiCount.setFill(Color.OLIVE);
            busCount = new Text(bus + "");
            busCount.setFill(Color.LIME);
            undergroundCount = new Text(underGround + "");
            busCount.setFill(Color.FUCHSIA);

            statsVBox.getChildren().addAll(taxiCount, busCount, undergroundCount);
        }

        statsVBox.setSpacing(2);

        picStatsHBox = new HBox();
        picStatsHBox.getChildren().addAll(flavorImage, statsVBox);

        this.getChildren().addAll(playerName, picStatsHBox);
    }

    //Display colored background to mark player as active
    public void setActive(boolean active) {
        if (active) {
            this.setBackground(new Background(new BackgroundFill(Color.PEACHPUFF, CornerRadii.EMPTY, Insets.EMPTY)));
        } else {
            this.setBackground(null);
        }
    }

    //Update displayed player card amounts
    public void updateValues(int taxi, int bus, int underGround, int blackFare, int doubleMove) {
        taxiCount = new Text(taxi + "");
        taxiCount.setFill(Color.OLIVE);
        busCount = new Text(bus + "");
        busCount.setFill(Color.LIME);
        undergroundCount = new Text(underGround + "");
        busCount.setFill(Color.FUCHSIA);
        if (shown) {
            statsVBox.getChildren().clear();
            statsVBox.getChildren().addAll(taxiCount, busCount, undergroundCount);
        }
        if (this.blackFareCount != null) {
            blackFareCount = new Text(blackFare + "");
            blackFareCount.setFill(Color.BLACK);
            doubleMoveCount = new Text(doubleMove + "");
            doubleMoveCount.setFill(Color.CRIMSON);
            if (shown) {
                statsVBox.getChildren().addAll(blackFareCount, doubleMoveCount);
            }
        }
    }
}

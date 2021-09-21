package sy.fxgui;

import sy.players.Seeker;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

class PlayerPane extends VBox {
    private PlayerStatusTemplate redSeeker;
    private PlayerStatusTemplate blueSeeker;
    private PlayerStatusTemplate greenSeeker;
    private PlayerStatusTemplate yellowSeeker;
    private PlayerStatusTemplate blackSeeker;
    private PlayerStatusTemplate mrX;
    private final ProgressIndicator pin;
    private final Text textIndicator;

    //Initialize the player pane and fill it with player status templates
    PlayerPane(boolean seekerShown) throws Exception {
        this.setSpacing(10);
        this.setPadding(new Insets(10));

        setRedSeeker(new PlayerStatusTemplate("Red Detective", 10, 8, 4, Seeker.Color.RED));
        setBlueSeeker(new PlayerStatusTemplate("Blue Detective", 10, 8, 4, Seeker.Color.BLUE));
        setGreenSeeker(new PlayerStatusTemplate("Green Detective", 10, 8, 4, Seeker.Color.GREEN));
        setYellowSeeker(new PlayerStatusTemplate("Yellow Detective", 10, 8, 4, Seeker.Color.YELLOW));
        setBlackSeeker(new PlayerStatusTemplate("Black Detective", 10, 8, 4, Seeker.Color.BLACK));
        setMrX(new PlayerStatusTemplate(4, 3, 3, 2, 5, seekerShown));

        pin = new ProgressIndicator();
        pin.setProgress(-1);

        textIndicator = new Text("AI's turn");
        textIndicator.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        this.getChildren().addAll(getRedSeeker(), getBlackSeeker(), getBlueSeeker(), getGreenSeeker(), getYellowSeeker(), getMrX());
    }

    //Display spinner and text
    void showPin() {
        this.getChildren().add(pin);
        this.getChildren().add(textIndicator);

    }

    //Hide spinner and text
    void hidePin() {
        this.getChildren().remove(pin);
        this.getChildren().remove(textIndicator);

    }

    public PlayerStatusTemplate getRedSeeker() {
        return redSeeker;
    }

    public void setRedSeeker(PlayerStatusTemplate redSeeker) {
        this.redSeeker = redSeeker;
    }

    public PlayerStatusTemplate getBlueSeeker() {
        return blueSeeker;
    }

    public void setBlueSeeker(PlayerStatusTemplate blueSeeker) {
        this.blueSeeker = blueSeeker;
    }

    public PlayerStatusTemplate getGreenSeeker() {
        return greenSeeker;
    }

    public void setGreenSeeker(PlayerStatusTemplate greenSeeker) {
        this.greenSeeker = greenSeeker;
    }

    public PlayerStatusTemplate getYellowSeeker() {
        return yellowSeeker;
    }

    public void setYellowSeeker(PlayerStatusTemplate yellowSeeker) {
        this.yellowSeeker = yellowSeeker;
    }

    public PlayerStatusTemplate getBlackSeeker() {
        return blackSeeker;
    }

    public void setBlackSeeker(PlayerStatusTemplate blackSeeker) {
        this.blackSeeker = blackSeeker;
    }

    public PlayerStatusTemplate getMrX() {
        return mrX;
    }

    public void setMrX(PlayerStatusTemplate mrX) {
        this.mrX = mrX;
    }
}

package sy.fxgui;

import sy.game.Action;
import sy.players.Player;
import sy.players.Seeker;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

import java.io.FileInputStream;
import java.util.ArrayList;

class Cell extends StackPane {
    private final String path = "src/resources/";
    private int cellNumber;
    private Point2D cellLocation;
    private Action taxiAction;
    private Action busAction;
    private Action undergroundAction;
    private Action blackfareAction;
    private int actionNumber = 0;
    private final ArrayList<ImageView> playersIcons;
    private final ArrayList<ImageView> indicatorsIcons;


    //Construct a cell with this specific number and these coordinates
    public Cell(int cellNumber, double x, double y) {
        this.setCellNumber(cellNumber);
        setCellLocation(new Point2D(x, y));
        playersIcons = new ArrayList<>();
        indicatorsIcons = new ArrayList<>();

    }

    //Adds a single player icon
    public void addPiece(Player player) throws Exception {
        ImageView pieceView = new ImageView(
                new Image(
                        new FileInputStream(getPath() + (player.isHider() ?
                                "mrx.gif" :
                                ((Seeker) player).getColor().name().toLowerCase() + "piece.png")
                        )
                )
        );

        pieceView.setPreserveRatio(true);

        getPlayersIcons().add(pieceView);

        this.getChildren().add(pieceView);

    }

    //Removes a single player icon
    public void removePieces() {
        for (ImageView icon : getPlayersIcons()) {
            this.getChildren().remove(icon);
        }
    }

    //Adds a single transport method indicator
    private void addIndicator(Action.Transportation method) throws Exception {

        ImageView pieceView = new ImageView(new Image(new FileInputStream(getPath() + method.name().toLowerCase() + "indicator.gif")));

        pieceView.setPreserveRatio(true);


        getIndicatorsIcons().add(pieceView);

        this.getChildren().add(pieceView);

    }

    //Removes a single transport method indicator
    private void removeIndicators() {
        for (ImageView icon : getIndicatorsIcons()) {
            this.getChildren().remove(icon);
        }
    }

    //Registers the existence of a potential action into the cell class and adds its corresponding indicator
    public void addAction(Action action) throws Exception {
        switch (action.getTransportation()) {
            case BUS:
                setBusAction(action);
                addIndicator(Action.Transportation.BUS);
                break;
            case TAXI:
                setTaxiAction(action);
                addIndicator(Action.Transportation.TAXI);
                break;
            case BLACK_FARE:
                setBlackfareAction(action);
                addIndicator(Action.Transportation.BLACK_FARE);
                break;
            case UNDERGROUND:
                setUndergroundAction(action);
                addIndicator(Action.Transportation.UNDERGROUND);
                break;
        }
        setActionNumber(getActionNumber() + 1);
    }

    //Removes all actions registered to this cell
    public void clearActions() {
        setBusAction(null);
        setTaxiAction(null);
        setBlackfareAction(null);
        setUndergroundAction(null);
        setActionNumber(0);

        removeIndicators();

    }


    public String getPath() {
        return path;
    }

    public int getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(int cellNumber) {
        this.cellNumber = cellNumber;
    }

    public Point2D getCellLocation() {
        return cellLocation;
    }

    public void setCellLocation(Point2D cellLocation) {
        this.cellLocation = cellLocation;
    }

    public Action getTaxiAction() {
        return taxiAction;
    }

    public void setTaxiAction(Action taxiAction) {
        this.taxiAction = taxiAction;
    }

    public Action getBusAction() {
        return busAction;
    }

    public void setBusAction(Action busAction) {
        this.busAction = busAction;
    }

    public Action getUndergroundAction() {
        return undergroundAction;
    }

    public void setUndergroundAction(Action undergroundAction) {
        this.undergroundAction = undergroundAction;
    }

    public Action getBlackfareAction() {
        return blackfareAction;
    }

    public void setBlackfareAction(Action blackfareAction) {
        this.blackfareAction = blackfareAction;
    }

    public int getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(int actionNumber) {
        this.actionNumber = actionNumber;
    }

    public ArrayList<ImageView> getPlayersIcons() {
        return playersIcons;
    }

    public ArrayList<ImageView> getIndicatorsIcons() {
        return indicatorsIcons;
    }
}

package sy.fxgui;

import sy.game.Action;
import sy.game.Board;
import sy.game.State;
import sy.mcts.Mcts;
import sy.players.Hider;
import sy.players.Player;
import sy.players.Seeker;
import sy.strategies.CoalitionReduction;
import sy.strategies.MoveFiltering;
import sy.strategies.Playouts;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;


class GameManager extends BorderPane {
    private static final int MCTS_ITERATIONS = 20000;
    private static final double HIDERS_EXPLORATION = 0.2;
    private static final double SEEKERS_EXPLORATION = 2;
    private static final int NUMBER_OF_PLAYERS = 6;
    private static final int HUMAN_AS_HIDER = 1;
    private static final int HUMAN_AS_SEEKERS = 2;
    private static final int TEST_PLAYERS = 3;
    private static Player.Type humanType;
    private static final int numberOfGames = 1;
    private static int numberOfSeekersWins = 0;
    private static int numberOfHidersWins = 0;
    private BoardPane gamePane;
    private PlayerPane playerPane;
    private MrXScrollPane movesPane;
    private ArrayList<Cell> cellList;
    private List<Action> previousActionList;
    private Service<Action> AINextMoveServices;

    GameManager() throws Exception {
        startNewGame();
    }

    //Initialize MCTS search
    private static Mcts<State, Action, Player> initializeSearch() {
        Mcts<State, Action, Player> mcts = Mcts.initializeIterations(MCTS_ITERATIONS);
        mcts.dontClone(Board.class);
        return mcts;
    }

    //Initialize players
    private static Player[] initializePlayers(Player.Type humanType) {
        if (humanType == Player.Type.HIDER)
            return initializePlayersWithOperator(Player.Operator.HUMAN, Player.Operator.MCTS);
        else if (humanType == Player.Type.SEEKER)
            return initializePlayersWithOperator(Player.Operator.MCTS, Player.Operator.HUMAN);
        else
            return initializePlayersWithOperator(Player.Operator.MCTS, Player.Operator.MCTS);
    }

    //Initialize how players will act
    private static Player[] initializePlayersWithOperator(Player.Operator hider, Player.Operator seeker) {
        Player[] players = new Player[NUMBER_OF_PLAYERS];
        players[0] = new Hider(hider, Playouts.Uses.GREEDY, CoalitionReduction.Uses.YES, MoveFiltering.Uses.YES);
        for (int i = 1; i < players.length; i++)
            players[i] = new Seeker(seeker, Seeker.Color.values()[i - 1], Playouts.Uses.GREEDY,
                    CoalitionReduction.Uses.YES, MoveFiltering.Uses.YES);
        return players;
    }

    //Does the current player have any moves left?
    private static boolean currentPlayerCanMove(State state) {
        return state.getAvailableActionsForCurrentAgent().size() > 0;
    }

    //Return the next action the AI will take from MCTS
    private static Action getActionFromSearch(State state, Mcts<State, Action, Player> mcts) {
        if (state.currentPlayerIsRandom())
            return getRandomAction(state);
        else
            return getActionFromMctsSearch(state, mcts);
    }

    //Return a random action
    private static Action getRandomAction(State state) {
        List<Action> actions = state.getAvailableActionsForCurrentAgent();
        Collections.shuffle(actions);
        return actions.get(0);
    }

    //Self explanatory
    private static void updateHidersMostProbablePosition(State state) {
        if (state.isTerminal())
            state.updateHidersProbablePosition();
    }

    //Self explanatory
    private static double getAppropriateExplorationParameter(State state) {
        if (state.currentPlayerIsHider())
            return HIDERS_EXPLORATION;
        else
            return SEEKERS_EXPLORATION;
    }

    //Return the next action the AI will take from MCTS
    private static Action getActionFromMctsSearch(State state, Mcts<State, Action, Player> mcts) {
        Action mostPromisingAction;
        state.setSearchModeOn();
        updateHidersMostProbablePosition(state);
        double explorationParameter = getAppropriateExplorationParameter(state);
        mostPromisingAction = mcts.uctSearchWithExploration(state, explorationParameter);
        state.setSearchModeOff();
        return mostPromisingAction;
    }

    //Displays the prompt that asks whether the human player wishes to use a double move card
    private static void askHumanForDoubleMoveConfidently(State state, Hider hider) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Do you wish to use a double move card?");
        alert.setHeaderText("Do you wish to use a double move card?\nYou have " + hider.getDoubleMoveCards() + " cards remaining.");
        alert.setContentText("Choose the corresponding button.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            state.skipAllSeekers();
            hider.removeDoubleMoveCard();
        }

    }

    //Should you ask the current player whether they want to use a double move card?
    private static boolean shouldAskForDoubleMove(State state) {
        return state.previousPlayerIsHider() && state.previousPlayerIsHuman();
    }

    //Displays the dialog that asks which type the player wants to be
    private void startGameDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose your player type");
        alert.setHeaderText("Do you wish to play as Mr. X or as a Detective?");
        alert.setContentText("Choose the corresponding button.");

        ButtonType mrXButton = new ButtonType("Mr. X");
        ButtonType detectiveButton = new ButtonType("Detective");


        alert.getButtonTypes().setAll(mrXButton, detectiveButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == mrXButton) {
            humanType = Player.Type.HIDER;
        } else if (result.get() == detectiveButton) {
            humanType = Player.Type.SEEKER;
        }
    }

    //Displays colored background over the player that's currently playing
    private void highlightCurrentPlayer(State state) {
        playerPane.getMrX().setActive(false);
        playerPane.getBlackSeeker().setActive(false);
        playerPane.getRedSeeker().setActive(false);
        playerPane.getYellowSeeker().setActive(false);
        playerPane.getGreenSeeker().setActive(false);
        playerPane.getBlueSeeker().setActive(false);

        if (state.getCurrentAgent().isHider()) {
            playerPane.getMrX().setActive(true);
            return;
        }

        switch (((Seeker) state.getCurrentAgent()).getColor()) {
            case BLUE:
                playerPane.getBlueSeeker().setActive(true);
                break;
            case BLACK:
                playerPane.getBlackSeeker().setActive(true);
                break;
            case RED:
                playerPane.getRedSeeker().setActive(true);
                break;
            case YELLOW:
                playerPane.getYellowSeeker().setActive(true);
                break;
            case GREEN:
                playerPane.getGreenSeeker().setActive(true);
                break;

        }
    }

    //Shows player positions on the map
    private void displayPlayerPositions(State state) throws Exception {
        Player[] players = state.getPlayersOnBoard().getPlayers();
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];

            if (player.cell != 0) {
                cellList.get(player.cell).removePieces();
            }

            if (humanType == Player.Type.SEEKER && player.isHider() && !state.isHiderSurfacesRound()) {
                continue;
            }

            Cell cell = cellList.get(state.getPlayersOnBoard().getPlayersActualPositions()[i]);
            cell.addPiece(player);
            player.cell = cell.getCellNumber();
        }
    }

    //Shows player tickets on the right
    private void displayPlayerStats(State state) {
        Player[] players = state.getPlayersOnBoard().getPlayers();
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];

            if (player.isHider()) {
                playerPane.getMrX().updateValues(player.getTaxiTickets(),
                        player.getBusTickets(),
                        player.getUndergroundTickets(),
                        ((Hider) player).getBlackFareTickets(),
                        ((Hider) player).getDoubleMoveCards());
                continue;
            }

            switch (((Seeker) player).getColor()) {
                case BLUE:
                    playerPane.getBlueSeeker().updateValues(player.getTaxiTickets(),
                            player.getBusTickets(),
                            player.getUndergroundTickets(),
                            0,
                            0);
                    break;
                case BLACK:
                    playerPane.getBlackSeeker().updateValues(player.getTaxiTickets(),
                            player.getBusTickets(),
                            player.getUndergroundTickets(),
                            0,
                            0);
                    break;
                case RED:
                    playerPane.getRedSeeker().updateValues(player.getTaxiTickets(),
                            player.getBusTickets(),
                            player.getUndergroundTickets(),
                            0,
                            0);
                    break;
                case YELLOW:
                    playerPane.getYellowSeeker().updateValues(player.getTaxiTickets(),
                            player.getBusTickets(),
                            player.getUndergroundTickets(),
                            0,
                            0);
                    break;
                case GREEN:
                    playerPane.getGreenSeeker().updateValues(player.getTaxiTickets(),
                            player.getBusTickets(),
                            player.getUndergroundTickets(),
                            0,
                            0);
                    break;

            }
        }
    }

    //Show the potential actions that the player can take
    private void getNextAction(State state) throws Exception {
        populateBoardWithOptions(state, state.currentPlayerIsHuman());

        if (!state.currentPlayerIsHuman()) {
            playerPane.showPin(); //Shows the AI is thinking spinner
            AINextMoveServices.restart(); // Starts the AI thread
        }

    }

    //Gets human action or shows which actions a bot can take
    private void populateBoardWithOptions(State state, boolean humanPlayer) throws Exception {
        if (previousActionList != null && !previousActionList.isEmpty()) {
            for (Action action : previousActionList) {
                Cell cell = cellList.get(action.getDestination());
                cell.clearActions();
            }
        }

        List<Action> availableActions = state.getAvailableActionsForCurrentAgent();
        previousActionList = availableActions;

        for (Action action : availableActions) {
            Cell cell = cellList.get(action.getDestination());

            if (!humanPlayer && state.getCurrentAgent().isHider()) {
                continue;
            }

            cell.addAction(action);

            if (!humanPlayer) {
                continue;
            }

            cell.setOnMouseReleased(e -> {
                if (cell.getActionNumber() == 1) {
                    Action action1;
                    if (cell.getUndergroundAction() != null) {
                        action1 = cell.getUndergroundAction();
                    } else if (cell.getTaxiAction() != null) {
                        action1 = cell.getTaxiAction();
                    } else if (cell.getBlackfareAction() != null) {
                        action1 = cell.getBlackfareAction();
                    } else {
                        action1 = cell.getBusAction();
                    }
                    try {
                        if (action1 != null) {
                            executeAction(state, action1);
                        }
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                } else {
                    try {
                        showTransportMethodDialog(state, cell.getUndergroundAction(), cell.getTaxiAction(), cell.getBusAction(), cell.getBlackfareAction());
                    } catch (Exception exception) {
                        System.out.println("Exception: " + exception.getMessage());
                    }
                }
                cell.clearActions();

            });

        }
    }

    //Asks which transport method the player would like to choose if there are several
    private void showTransportMethodDialog(State state, Action... actions) throws Exception {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Choose your transport type");
        alert.setHeaderText("How do you wish to travel to the selected cell?");
        alert.setContentText("Choose the corresponding button.");

        alert.getButtonTypes().clear();

        for (Action action : actions) {
            if (action != null) {
                alert.getButtonTypes().add(new ButtonType(action.getTransportation().name()));
            }
        }

        if(alert.getButtonTypes().size() == 0){
            return;
        }


        Optional<ButtonType> result = alert.showAndWait();

        for (Action action : actions) {
            if (action != null && result.get().getText().equals(action.getTransportation().name())) {
                executeAction(state, action);
            }
        }

    }

    //Main game loop. Recursive.
    private void gameLoop(State state) throws Exception {
        if (!state.isTerminal()) {
            updateGUI(state);

            if (currentPlayerCanMove(state)) {
                getNextAction(state);
            } else {
                state.skipCurrentAgent();
                gameLoop(state);
            }
        } else {
            gameConclusionAndRetryPrompt(state);
        }
    }

    //Displayed when game ends
    private void gameConclusionAndRetryPrompt(State state) throws Exception {
        String winner;
        if (state.seekersWon()) {
            numberOfSeekersWins++;
            winner = "the detectives";
        } else {
            numberOfHidersWins++;
            winner = "Mr. X";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("The game has ended.");
        alert.setHeaderText("It seems " + winner + " has won.");
        alert.setContentText("Total number of games: " + (numberOfSeekersWins + numberOfHidersWins) + "\n" +
                "Detectives won " + numberOfSeekersWins + " times\n" +
                "Mr. X won " + numberOfHidersWins + " times\n" +
                "Do you want to play again? Please choose the corresponding button.");

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");

        alert.getButtonTypes().setAll(yesButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.get() == yesButton) {
            startNewGame();
        } else {
            System.exit(0);
        }
    }

    //Display which actions Mr. X took on the left pane
    private void updateMrXMoves(State state, Action action) throws Exception {
        if (state.getPreviousAgent().isHider()) {
            int actualRound = state.getCurrentRound() - (state.isUsedDoubleMove() ? 2 : 1); //Corrects for double moves
            MrXMoveTemplate mrXMoveTemplateCurrentRound = movesPane.getMovesList().get(actualRound);
            mrXMoveTemplateCurrentRound.changeMoveDone(action.getTransportation());
            if ((state.isHiderSurfacesRound() && state.isHiderSurfacesRound(actualRound + 1)) || state.isHiderSurfacesRound(actualRound + 1)) {
                Player[] players = state.getPlayersOnBoard().getPlayers();
                for (int i = 0; i < players.length; i++) {
                    Player player = players[i];
                    if (!player.isHider()) {
                        continue;
                    }
                    mrXMoveTemplateCurrentRound.getPosition().setText(state.getPlayersOnBoard().getPlayersActualPositions()[i] + "");
                }
            }
        }
    }

    //Perform whatever action the player or AI took and prompt for double moves.
    private void executeAction(State state, Action action) throws Exception {
        state.performActionForCurrentAgent(action);
        updateMrXMoves(state, action);

        if (shouldAskForDoubleMove(state)) {
            Hider hider = (Hider) state.getPreviousAgent();
            if (hider.hasDoubleMoveCard())
                askHumanForDoubleMoveConfidently(state, hider);
        }

        gameLoop(state);

    }

    //Initialize board and start the game loop
    private void startNewGame() throws Exception {
        Mcts<State, Action, Player> mcts = initializeSearch();

        startGameDialog();

        Player[] players = initializePlayers(humanType);
        State state = State.initialize(players);

        cellList = new ArrayList<>(199);
        GameBoardLocationsParser.fetchPointsInto(cellList);

        gamePane = new BoardPane(cellList);
        playerPane = new PlayerPane(humanType == Player.Type.HIDER);
        movesPane = new MrXScrollPane();

        this.setCenter(gamePane);
        this.setLeft(movesPane);
        this.setRight(playerPane);

        AINextMoveServices = new AINextMoveService(state, mcts);


        gameLoop(state);
    }

    //Update GUI to display the game's current state
    private void updateGUI(State state) throws Exception {
        highlightCurrentPlayer(state);
        displayPlayerPositions(state);
        displayPlayerStats(state);
    }

    //Runs in a separate thread. AI takes 30 seconds or so to get a move, so we put it on its own thread to not hang the main program.
    private class AINextMoveService extends Service<Action> {
        private final sy.game.State state;
        private final Mcts<sy.game.State, Action, Player> mcts;

        AINextMoveService(sy.game.State state, Mcts<sy.game.State, Action, Player> mcts) {
            this.state = state;
            this.mcts = mcts;
            this.setExecutor(Executors.newCachedThreadPool());
            this.setOnSucceeded(e -> {
                try {
                    playerPane.hidePin();
                    executeAction(state, AINextMoveServices.getValue());
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            });
        }

        @Override
        protected Task<Action> createTask() {
            return new Task<Action>() {
                @Override
                protected Action call() throws Exception {
                    return getActionFromSearch(state, mcts);
                }
            };
        }
    }
}

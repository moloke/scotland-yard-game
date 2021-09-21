package sy.players;

import sy.game.Action;
import sy.game.State;
import sy.strategies.CoalitionReduction;
import sy.strategies.MoveFiltering;
import sy.strategies.Playouts;

public class Seeker extends Player {

    private static final int TAXI_TICKETS = 10;
    private static final int BUS_TICKETS = 8;
    private static final int UNDERGROUND_TICKETS = 4;
    private static final double COALITION_REDUCTION_PARAMETER = 0.25;

    public Color getColor() {
        return color;
    }

    public enum Color {
        BLACK, BLUE, YELLOW, RED, GREEN
    }

    private final Color color;

    public Seeker(Operator operator, Color color, Playouts.Uses playout, CoalitionReduction.Uses coalitionReduction,
                  MoveFiltering.Uses moveFiltering) {
        super(operator, Type.SEEKER, TAXI_TICKETS, BUS_TICKETS, UNDERGROUND_TICKETS, playout,
                coalitionReduction, moveFiltering);
        this.color = color;
    }

    @Override
    protected Action getActionForHiderFromStatesAvailableActionsForSimulation(State state) {
        if (this.usesBiasedPlayout())
            return Playouts.getGreedyBiasedActionForHider(state);
        else
            return Playouts.getRandomAction(state);
    }

    @Override
    protected Action getActionForSeekerFromStatesAvailableActionsForSimulation(State state) {
        if (this.usesBiasedPlayout())
            return Playouts.getGreedyBiasedActionForSeeker(state);
        else
            return Playouts.getRandomAction(state);
    }

    @Override
    public double getRewardFromTerminalState(State state) {
        if (state.searchInvokingPlayerUsesCoalitionReduction())
            return CoalitionReduction.getCoalitionReductionRewardFromTerminalState(state, this);
        else
            return CoalitionReduction.getNormalRewardFromTerminalState(state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Seeker seeker = (Seeker) o;
        return color == seeker.color;
    }

    @Override
    public int hashCode() {
        return color != null ? color.hashCode() : 0;
    }

    @Override
    public String toString() {
        return color + " Seeker";
    }
}
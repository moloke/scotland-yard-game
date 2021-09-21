package sy.mcts;

import com.rits.cloning.Cloner;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MctsTreeNode<StateT extends MctsDomainState<ActionT, AgentT>, ActionT, AgentT extends MctsDomainAgent> {

    private final MctsTreeNode<StateT, ActionT, AgentT> parentNode;
    private final ActionT incomingAction;
    private final StateT representedState;
    private int visitCount;
    private double totalReward;
    private List<MctsTreeNode<StateT, ActionT, AgentT>> childNodes;
    private final Cloner cloner;

    protected MctsTreeNode(StateT representedState, Cloner cloner) {
        this(null, null, representedState, cloner);
    }

    private MctsTreeNode(MctsTreeNode<StateT, ActionT, AgentT> parentNode, ActionT incomingAction,
                         StateT representedState, Cloner cloner) {
        this.parentNode = parentNode;
        this.incomingAction = incomingAction;
        this.representedState = representedState;
        this.visitCount = 0;
        this.totalReward = 0.0;
        this.childNodes = new ArrayList<>();
        this.cloner = cloner;
    }

    protected MctsTreeNode<StateT, ActionT, AgentT> getParentNode() {
        return parentNode;
    }

    protected ActionT getIncomingAction() {
        return incomingAction;
    }

    protected int getVisitCount() {
        return visitCount;
    }

    protected int getParentsVisitCount() {
        return parentNode.getVisitCount();
    }

    protected List<MctsTreeNode<StateT, ActionT, AgentT>> getChildNodes() {
        return childNodes;
    }

    protected boolean hasChildNodes() {
        return childNodes.size() > 0;
    }

    protected boolean representsTerminalState() {
        return representedState.isTerminal();
    }

    protected AgentT getRepresentedStatesPreviousAgent() {
        return representedState.getPreviousAgent();
    }

    protected boolean representedStatesCurrentAgentHasAvailableActions() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() > 0;
    }

    protected boolean isFullyExpanded() {
        return representedState.getNumberOfAvailableActionsForCurrentAgent() == childNodes.size();
    }

    protected boolean hasUnvisitedChild () {
        return childNodes.stream()
                .anyMatch(MctsTreeNode::isUnvisited);
    }

    private boolean isUnvisited() {
        return visitCount == 0;
    }

    //Adds a new child node without an action and skips the child's current agent
    protected MctsTreeNode<StateT, ActionT, AgentT> addNewChildWithoutAction() {
        StateT childNodeState = getDeepCloneOfRepresentedState();
        childNodeState.skipCurrentAgent();
        return appendNewChildInstance(childNodeState, null);
    }

    //Adds a new child node with an action performed on it
    protected MctsTreeNode<StateT, ActionT, AgentT> addNewChildFromAction(ActionT action) {
        if (!isUntriedAction(action))
            throw new IllegalArgumentException("Error: invalid action passed as function parameter");
        else
            return addNewChildFromUntriedAction(action);
    }

    private boolean isUntriedAction(ActionT action) {
        return getUntriedActionsForCurrentAgent().contains(action);
    }

    //Returns a list of untried actions for the agent currently playing
    protected List<ActionT> getUntriedActionsForCurrentAgent() {
        List<ActionT> availableActions = representedState.getAvailableActionsForCurrentAgent();
        List<ActionT> untriedActions = new ArrayList<>(availableActions);
        List<ActionT> triedActions = getTriedActionsForCurrentAgent();
        untriedActions.removeAll(triedActions);
        return untriedActions;
    }

    //Returns a list of actions the agent has already tried
    private List<ActionT> getTriedActionsForCurrentAgent() {
        return childNodes.stream()
                .map(MctsTreeNode::getIncomingAction)
                .collect(Collectors.toList());
    }

    //Adds a new child node with an untried action
    private MctsTreeNode<StateT, ActionT, AgentT> addNewChildFromUntriedAction(ActionT incomingAction) {
        StateT childNodeState = getNewStateFromAction(incomingAction);
        return appendNewChildInstance(childNodeState, incomingAction);
    }

    //Clones the existing state and performs an action on it then returns it
    private StateT getNewStateFromAction(ActionT action) {
        StateT representedStateClone = getDeepCloneOfRepresentedState();
        representedStateClone.performActionForCurrentAgent(action);
        return representedStateClone;
    }

    //Deep clones the representedState object and returns it
    protected StateT getDeepCloneOfRepresentedState() {
        return cloner.deepClone(representedState);
    }

    //Create a new child node with incoming action.
    private MctsTreeNode<StateT, ActionT, AgentT> appendNewChildInstance(
            StateT representedState, ActionT incomingAction) {
        MctsTreeNode<StateT, ActionT, AgentT> childNode = new MctsTreeNode<>(
                this, incomingAction, representedState, cloner);
        childNodes.add(childNode);
        return childNode;
    }

    //Increment node's visit count and update its reward
    protected void updateDomainTheoreticValue(double rewardAddend) {
        visitCount += 1;
        totalReward += rewardAddend;
    }

    protected double getDomainTheoreticValue() {
        return totalReward / visitCount;
    }
}
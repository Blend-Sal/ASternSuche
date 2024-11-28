/**
 * Das Node Objekt soll einen Knoten im Graphen definieren.
 * Ein Knoten gehört immer zu einer Stadt und hat ein Charge (Restreichweite).
 * Somit ist der Zustand im Knoten definiert.
 */

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Node {
    private Node parent;
    private List<Node> children = new LinkedList<>();
    private int cost = 0;
    private City city;
    private int charge;

    public Node(City name, int cost) {
        this.city = name;
        this.cost = cost;
    }


    public int getCharge() {
        return this.charge;
    }

    public int setCharge(int charge) {

        return this.charge = charge;
    }

    public Node createChild(Node child) {
        this.children.add(child);
        child.parent = this;
        return child;
    }

    public int getCost() {
        return this.cost;
    }

    public City getCity() {
        return this.city;
    }


    public Node getChild(int childIndex) {
        return childIndex < children.size() ? children.get(childIndex) : null;
    }

    public int getHeuristicValue() {
        return (int) getCity().getHeuristicValue();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Node && this.city.equals(((Node) other).city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.city);
    }

    /**
     *
     * @return der FScore, der für die Priorisierung im Frontier eingesetzt wird.
     */
    public int getFScore() {
        return this.getCost() + this.getHeuristicValue() * getCharge();
    }


    public String toString() {
        return String.format("(%s|%d)", getCity().getName(), getCharge());
    }

    public Node getParent() {
        return this.parent;
    }

    public Node setParent(Node parent) {
        return this.parent = parent;
    }

}

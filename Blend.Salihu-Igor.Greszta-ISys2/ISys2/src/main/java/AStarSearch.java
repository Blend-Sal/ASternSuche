/**
 * Die AStarSearch Klasse ist dafür zuständig, den A* Algorithmus anzuwenden.
 * Sie beinhaltet die Hauptsuchmethode search(Node), genauso wie die reconstructPath(Node) Methode.
 */
import java.util.*;

public class AStarSearch {
    /**
     * @param connections eine Map, die alle Verbindungen zwischen Städten beinhaltet.
     *                    Hierbei ist der Key die Stadt und eine Liste von Connections,
     *                    in welchen sich die Stadt befindet, das Value.
     * @param maxRange die eingegebene maximale Reichweite (z.B. 200 bei der Teilaufgabe 3).
     * @param reached eine Map, die alle Knoten, die bereits besucht worden sind. Hierbei ist die Stadt der Key,
     *                und das Value ist der Knoten, zu welchen die Stadt gehört.
     *                @see Node
     * @param maxFrontierSize und
     * @param expandedNodes sind beides Zähler, die für die Teilaufgabe 3 benötigt werden.
     */
    private final Map<City, List<Connection>> connections;
    private final int maxRange;
    private PriorityQueue<Node> frontier;
    private Map<City, Node> reached;
    int maxFrontierSize = 0;
    int expandedNodes = 0;

    /**
     * Hier wird das Frontier nach der Heuristik sortiert.
     * @see Node
     */
    public AStarSearch(Map<City, List<Connection>> connections, int maxRange) {
        this.connections = connections;
        this.maxRange = maxRange;
        this.frontier = new PriorityQueue<>(Comparator.comparingInt(Node::getFScore));
        this.reached = new HashMap<>();
    }

    /**
     *
     * @param start der Startknoten
     * @param goal der Endknoten
     * @return Der optimale Pfad mithilfe von der reconstructPath() Methode als Liste ausgegeben.
     */
    public List<Node> search(City start, City goal) {
        start.setCost(0);
        Node starterNode = new Node(start, 0);
        frontier.add(starterNode);
        reached.put(start, starterNode);
        this.maxFrontierSize = frontier.size();


        while (!frontier.isEmpty()) {
            Node currentNode = frontier.poll();
            this.maxFrontierSize = Math.max(this.maxFrontierSize, frontier.size());
            if (currentNode.getParent() == null) { // Check für den Initial - Node
                currentNode.setCharge(maxRange);
            }
            City currentCity = currentNode.getCity();

            if (currentCity.equals(goal)) {
                return reconstructPath(currentNode);
            }

            if (currentNode.getCity().hasChargingStation() && currentNode.getCharge() < maxRange) {
                Node rechargeNode = currentNode.createChild(new Node(currentNode.getCity(), currentNode.getCost()));
                rechargeNode.setCharge(maxRange);
                if (!frontier.contains(rechargeNode)) {
                    frontier.add(rechargeNode);
                }
            }

            if (connections.containsKey(currentCity)) {
                /**
                 * Iteration über jede Connection, zu welcher die momentane Stadt gehört
                 * @throws IllegalStateException falls der Agent sich bewegt, ohne sich die Bewegung leisten zu können.
                 */
                for (Connection connection : connections.get(currentCity)) {
                    City neighbor = connection.getOtherCity(currentCity);
                    int tentativeGScore = currentNode.getCost() + connection.getDistance();

                    if (currentNode.getCharge() < 0 && !currentCity.hasChargingStation()) {
                        throw new IllegalStateException("No negative charge allowed");
                    }

                    if (currentNode.getCharge() >= 0 || currentCity.hasChargingStation()) {
                        Node childNode = currentNode.createChild(new Node(neighbor, tentativeGScore));
                        childNode.setCharge(currentNode.getCharge() - connection.getDistance());
                        Node reachedNode = reached.get(neighbor);

                        if (reachedNode == null || childNode.getCost() < reachedNode.getCost()) {
                            if (reachedNode == null && (!currentNode.getCity().hasChargingStation())) {
                                childNode.setCharge(currentNode.getCharge() - connection.getDistance());
                            } else {
                                frontier.remove(reachedNode);
                            }

                        }
                            if (childNode.getCharge() >= 0) {
                                reached.put(neighbor, childNode);
                                frontier.add(childNode);
                                expandedNodes++;
                            }
                    }
                }
            }
        }

        return null;
    }



    private List<Node> reconstructPath(Node goal) {
        List<Node> path = new ArrayList<>();
        Node currentNode = goal;

        while (currentNode != null) {
            path.add(currentNode);
            currentNode = currentNode.getParent();
        }

        Collections.reverse(path);
        System.out.println(path.stream().map(Node::getCity).map(City::getName).toList() + "\n");
        return path;
    }

}
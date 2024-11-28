import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class City implements Comparable<City> {
    private String name;
    private boolean hasChargingStation;
    private double heuristicValue;
    private List<Connection> connections = new ArrayList<>();
    private int cost = Integer.MAX_VALUE;
    private double longitude;
    private double latitude;

    public City(String name, double heuristicValue, boolean hasChargingStation) {
        this.name = name;
        this.heuristicValue = heuristicValue;
        this.hasChargingStation = hasChargingStation;
        this.latitude = 0;
        this.longitude = 0;
    }

    public City(String name, double latitude, double longitude, boolean hasChargingStation) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.hasChargingStation = hasChargingStation;
    }

    public String getName() {
        return name;
    }

    public boolean hasChargingStation() {
        return hasChargingStation;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    public void setHeuristicValue(double heuristicValue) {
        this.heuristicValue = heuristicValue;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void addConnection(Connection connection) {
        connections.add(connection);
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof City && ((City) other).name.equalsIgnoreCase(this.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public String toString() {
        return this.getName();
    }


    @Override
    public int compareTo(City o) {
        return Double.compare(cost + this.heuristicValue, o.cost + o.heuristicValue);
    }

    public boolean setHeuristicWithHaversine(City goal) {
        this.setHeuristicValue(Helper.haversine_distance(this.getLatitude(), this.getLongitude(), goal.getLatitude(), goal.getLongitude()));
        return true;
    }

    public boolean setHeuristicWithManhattan(City goal) {
        this.setHeuristicValue(Helper.manhattanDist(this.getLatitude(), this.getLongitude(), goal.getLatitude(), goal.getLongitude()));
        return true;
    }


}





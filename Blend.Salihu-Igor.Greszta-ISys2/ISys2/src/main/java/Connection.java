import java.util.Objects;

public class Connection {
    private City city1;
    private City city2;
    private int distance;

    public Connection(City city1, City city2, int distance) {
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
        city1.addConnection(this);
        city2.addConnection(this);
    }
    public City getCity() {
        return city1;
    }

    public City getOtherCity(City city) {
        if (city.equals(city1)) {
            return city2;
        } else if (city.equals(city2)) {
            return city1;
        } else {
            throw new IllegalArgumentException("No city found in this connection");
        }
    }
    @Override
    public boolean equals(Object other) {
        return other instanceof Connection && ((Connection) other).city1.equals(this.city1)
                && ((Connection) other).city2.equals(this.city2) && ((Connection) other).distance == this.distance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.city1, this.city2, this.distance);
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }
}

/**
 * Diese Klasse beinhaltet viele Methoden, die uns bei der Bearbeitung der Aufgaben helfen.
 */

import org.javatuples.Pair;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Helper extends java.io.FileReader {

    public Helper(String filename) throws IOException {
        super(filename);
    }

    public static List<City> readCities(String fileName, Map<City, List<Connection>> connectionsMap) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new Helper(fileName))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                int heuristicValue = Integer.parseInt(parts[1]);
                boolean hasChargingStation = Boolean.parseBoolean(parts[2].trim().toLowerCase());
                City city = new City(name, heuristicValue, hasChargingStation);
                cities.add(city);
                connectionsMap.put(city, new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }


    public static Map<City, List<Connection>> readConnections(String fileName, List<City> cities) {
        Map<City, List<Connection>> connectionsMap = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String city1Name = parts[0].trim();
                String city2Name = parts[1].trim();
                int distance = Integer.parseInt(parts[2]);
                City city1 = findCityByName(cities, city1Name);
                City city2 = findCityByName(cities, city2Name);
                Connection connection = new Connection(city1, city2, distance);

                // Ensure each city is a key in the map
                connectionsMap.putIfAbsent(city1, new ArrayList<>());
                connectionsMap.putIfAbsent(city2, new ArrayList<>());

                connectionsMap.get(city1).add(connection);
                connectionsMap.get(city2).add(connection);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connectionsMap;
    }

    public static List<City> readCitiesA3(String fileName, Map<City, List<Connection>> connectionsMap) {
        List<City> cities = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            br.readLine(); // skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String name = parts[0];
                double latitude = Double.parseDouble(parts[1]);
                double longitude = Double.parseDouble(parts[2]);
                boolean hasChargingStation = Boolean.parseBoolean(parts[3].trim().toLowerCase());
                City city = new City(name, latitude, longitude, hasChargingStation);
                cities.add(city);
                connectionsMap.put(city, new ArrayList<>());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cities;
    }

    public static City findCityByName(List<City> cities, String name) {
        return cities.stream()
                .filter(city -> city.getName().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No city found with name " + name));
    }

    public static int calculateCost(List<Node> path) {
        int cost = 0;
        if (path == null) {
            System.out.println("No path found for this testcase");
            return -1;
        }
        for (int i = 0; i < path.size() - 1; i++) {
            City city1 = path.get(i).getCity();
            City city2 = path.get(i + 1).getCity();
            cost += calculateDistance(city1, city2);

            if (city1.equals(city2)) { // Diese Situation findet nur bei dem Ladevorgang statt.
                cost += 10;
            }
        }
        return cost;
    }

    public static int calculateDistance(City city1, City city2) {
        Connection connection = findConnection(city1, city2);
        return connection.getDistance();
    }

    public static Connection findConnection(City city1, City city2) {
        if (city1.equals(city2)) {
            return new Connection(city1, city2, 0);
        }
        for (Connection connection : city1.getConnections()) {
            if (connection.getOtherCity(city1).equals(city2)) {
                return connection;
            }
        }
        throw new IllegalArgumentException("No connection found between " + city1.getName() + " and " + city2.getName());
    }


    /**
     * @param lat1 Latitude of location 1
     * @param lon1 Longitude of location 1
     * @param lat2 Latitude of location 2
     * @param lon2 Longitude of location 2
     * @return Haversine distance between the two locations
     */
    public static double haversine_distance(double lat1, double lon1, double lat2, double lon2) {
        final int d = 12742;
        double sinHalfDeltaLat = Math.sin(Math.toRadians(lat2 - lat1) / 2);
        double sinHalfDeltaLon = Math.sin(Math.toRadians(lon2 - lon1) / 2);
        double latARadians = Math.toRadians(lat1);
        double latBRadians = Math.toRadians(lat2);
        double a = sinHalfDeltaLat * sinHalfDeltaLat
                + Math.cos(latARadians) * Math.cos(latBRadians) * sinHalfDeltaLon * sinHalfDeltaLon;
        return d * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }


    static double manhattanDist(double lat1, double lon1, double lat2, double lon2) {
        double dist = Math.abs(lat1 - lat2) + Math.abs(lon1 - lon2);
        return dist;
    }

    public static List<Pair<City, City>> readTestCase(String fileName, List<City> cities) {
        List<Pair<City, City>> testCases = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                String city1Name = parts[0].trim();
                String city2Name = parts[1].trim();
                City city1 = findCityByName(cities, city1Name);
                City city2 = findCityByName(cities, city2Name);
                city1.setHeuristicWithHaversine(city2);
                city2.setHeuristicWithHaversine(city1);
                testCases.add(new Pair<>(city1, city2));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return testCases;
    }


    public static void task2(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (args.length != 0) {
            return;
        }

        System.out.println("Please provide 3 arguments:");
        System.out.print("1. Max range (e.g., 410): ");
        int maxRange = scanner.nextInt();

        System.out.print("2. Relative path to the file with cities (e.g., ./src/main/resources/testcases_Teilaufgabe_2/t1_cities.txt): ");
        String citiesFilePath = scanner.next();

        System.out.print("3. Relative path to the file with connections (e.g., ./src/main/resources/testcases_Teilaufgabe_2/t1_connections.txt): ");
        String connectionsFilePath = scanner.next();


        Map<City, List<Connection>> connectionsMap = new HashMap<>();
        City goal = new City("B", 0, false);

        List<City> cities = Helper.readCities(citiesFilePath, connectionsMap);
        connectionsMap = Helper.readConnections(connectionsFilePath, cities);

        AStarSearch search = new AStarSearch(connectionsMap, maxRange);
        City start = Helper.findCityByName(cities, "A");

        List<Node> path = search.search(start, goal);
        int cost = Helper.calculateCost(path);

        System.out.println("Optimal path cost: " + cost);

        if (path == null) {
            System.out.println("No path found for this testcase");
            return;
        }

        for (Node node : path) {
            System.out.println("(" + node.getCity().getName() + "|" + node.getCharge() + ")");
        }
    }

    /**
     * @param useManhattanDistance Auswahl der Heuristik. Wenn true, wird für den Algorithmus die Manhattan - Distanz genutzt.
     *                             Dies ist für Aufgabe 4 nötig. Wenn false, benutzt man die Haversive - Distanz.
     */
    public static void task3And4(boolean useManhattanDistance) {
        int maxRange = 200;
        String citiesFilePath = "./src/main/resources/testcases_Teilaufgabe_3/bigGraph_cities.txt";
        String connectionsFilePath = "./src/main/resources/testcases_Teilaufgabe_3/bigGraph_connections.txt";
        String testCasesFilePath = "./src/main/resources/testcases_Teilaufgabe_3/testcases_bigGraph.txt";


        Map<City, List<Connection>> connectionsMap = new HashMap<>();

        List<City> cities = readCitiesA3(citiesFilePath, connectionsMap);
        connectionsMap = readConnections(connectionsFilePath, cities);
        List<Pair<City, City>> testCases = readTestCase(testCasesFilePath, cities);
        AStarSearch search = new AStarSearch(connectionsMap, maxRange);

        int i = 1;
        List<Integer> maxFrontiers = new LinkedList<>();
        List<Integer> expandedNodesCounts = new LinkedList<>();
        List<Integer> costList = new LinkedList<>();

        for (Pair<City, City> testCase : testCases) {
            search.expandedNodes = 0;
            search.maxFrontierSize = 0;
            for (City city : cities) {
                if (useManhattanDistance) {
                    city.setHeuristicWithManhattan(testCase.getValue1());
                } else {
                    city.setHeuristicWithHaversine(testCase.getValue1());
                }
            }
            System.out.printf("\nCalculating testCase %d from %d%n", i, testCases.size());
            List<Node> path = search.search(testCase.getValue0(), testCase.getValue1());
            System.out.printf("Cost for this path: %d\n", calculateCost(path));
            System.out.printf("Maximum size of Frontier for testcase %d: %d%n", i, search.maxFrontierSize);
            System.out.printf("Number of expanded Nodes for testcase %d: %d%n", i, search.expandedNodes);

            i++;
            maxFrontiers.add(search.maxFrontierSize);
            expandedNodesCounts.add(search.expandedNodes);
            costList.add(calculateCost(path));
        }

        System.out.printf("%nAverage maximum Frontier size for all testcases: %.2f\n", maxFrontiers.stream().mapToDouble(a -> a).average().getAsDouble());
        System.out.printf("Average number of expanded Nodes for all testcases: %.2f\n", expandedNodesCounts.stream().mapToDouble(a -> a).average().getAsDouble());
        System.out.printf("Average travel cost: %.2f", costList.stream().mapToDouble(a -> a).average().getAsDouble());

    }

}





// Sanika Sisodia 
// 300283847
// websites used to inspire implementations: (code from labs too)
//https://www.baeldung.com/java-dijkstra
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class ParisMetro {
    private final Graph metroGraph;
    private static final int WALK = 90;

    private ParisMetro(Graph metroGraph) {
        this.metroGraph = metroGraph;
    }

    public static ParisMetro readFile(String fileName) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File(fileName));
        Graph graph = new Graph();

        int numberOfStations = scanner.nextInt();
        int numberOfConnections = scanner.nextInt();

        for (int i = 0; i < numberOfStations; i++) {
            graph.addVertex(new Vertex(scanner.nextInt(), scanner.next()));
            scanner.nextLine();
        }
        scanner.nextLine();

        while (scanner.hasNext()) {
            // reading the file
            int orgStation = scanner.nextInt();
            int desStation = scanner.nextInt();
            int travelTime = scanner.nextInt();
            // getting vertexes from the file using the vertex method 
            Vertex orgVertex = graph.getVertex(orgStation);
            Vertex desVertex = graph.getVertex(desStation);
            // using edge method to get edges to complete the graph
            graph.addEdge(orgVertex, desVertex, travelTime);
        }

        return new ParisMetro(graph);
    }



    private void depthFirstSearch(int stationNumber, List<Integer> sameLineStations, Hashtable<Integer, Boolean> visited) {
        if (!visited.containsKey(stationNumber)) {
            visited.put(stationNumber, true);
            sameLineStations.add(stationNumber);   // explain in report
            for (Vertex neighbor : metroGraph.getNeighbors(metroGraph.getVertex(stationNumber))) {
                int neighborStation = neighbor.getStationNumber();
                if (!visited.containsKey(neighborStation) && metroGraph.getWeight(metroGraph.getVertex(stationNumber), neighbor) != -1) {
                    depthFirstSearch(neighborStation, sameLineStations, visited);
                }
            }
        }
    }


    public List<Integer> getSameLineStations(int stationNumber) {
        List<Integer> sameLineStations = new ArrayList<>();
        Hashtable<Integer, Boolean> visited = new Hashtable<>();
        depthFirstSearch(stationNumber, sameLineStations, visited);
        return sameLineStations;
    }


    public List<Integer> findShortestPath(Vertex org, Vertex des) {
        // array to store distances from source to each station
        int[] distances = new int[376];
        // array to store predecessors for reconstructing the path
        Vertex[] predecessors = new Vertex[376];
        // initialize distances to infinity and predecessors to null for all stations
        Arrays.fill(distances, Integer.MAX_VALUE);
        // Distance from source to itself is 0
        distances[org.getStationNumber()] = 0;
         // priority queue to store vertices with their current distances
        PriorityQueue<NodeDistance<Vertex>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));
        priorityQueue.add(new NodeDistance<>(org, 0));
         // dijkstra's algorithm (explained in report)
        while (!priorityQueue.isEmpty()) {
            NodeDistance<Vertex> current = priorityQueue.poll();
            Vertex currentVertex = current.v;
            int currentDistance = current.distance;
            // calculate the total distance to the neighbor
            for (Vertex neighbor : metroGraph.getNeighbors(currentVertex)) {
                int weight = metroGraph.getWeight(currentVertex, neighbor);
                if (weight == -1) {
                    weight = WALK;
                }
                int neighbourDis = currentDistance + weight;
                // if the new distance is shorter update distances and predecessors
                if (neighbourDis < distances[neighbor.getStationNumber()]) {
                    distances[neighbor.getStationNumber()] = neighbourDis;
                    predecessors[neighbor.getStationNumber()] = currentVertex;
                    priorityQueue.add(new NodeDistance<>(neighbor, neighbourDis));
                }
            }
        }

        return buildPath(des, predecessors);
    }


    private List<Integer> buildPath(Vertex des, Vertex[] pred) { //method made so that path can be built from destination to source using preceessoros (reverse at end so its source to destination)
        List<Integer> path = new ArrayList<>();
        Vertex vert = des;

        while (vert != null) {
            path.add(vert.getStationNumber());
            vert = pred[vert.getStationNumber()];
        }

        Collections.reverse(path);
        return path;
    }

    public List<Integer> findShortestPathWithBrokenLine(Vertex org, Vertex des, Vertex brokenLineEndpoint) {
        // same setup as previous shortest path
        int[] distances = new int[376];
        Vertex[] predecessors = new Vertex[376];

        for (Vertex v : metroGraph.getVertices()) {
            distances[v.getStationNumber()] = Integer.MAX_VALUE;
            predecessors[v.getStationNumber()] = null;
        }

        distances[org.getStationNumber()] = 0;

        PriorityQueue<NodeDistance<Vertex>> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(nd -> nd.distance));
        priorityQueue.add(new NodeDistance<>(org, 0));
        
        while (!priorityQueue.isEmpty()) {
            NodeDistance<Vertex> current = priorityQueue.poll();
            Vertex currentVertex = current.v;
            int currentDistance = current.distance;
            // if line is broken, uses algorith to fix that 
            if (currentVertex.equals(brokenLineEndpoint)) {
                // would impliment broken line algorithm
            } else {
                // calculate the total distance to the neighbor
                for (Vertex neighbor : metroGraph.getNeighbors(currentVertex)) {
                    int weight = metroGraph.getWeight(currentVertex, neighbor);
                    if (weight == -1) {
                        weight = WALK;
                    }
                    int neighbourDis = currentDistance + weight;
                    // if the new distance is shorter update distances and predecessors
                    if (neighbourDis < distances[neighbor.getStationNumber()]) {
                        distances[neighbor.getStationNumber()] = neighbourDis;
                        predecessors[neighbor.getStationNumber()] = currentVertex;
                        priorityQueue.add(new NodeDistance<>(neighbor, neighbourDis));
                    }
                }
            }
        }

        return buildPath(des, predecessors);
    }

 

    private static class NodeDistance<V> {
        Vertex v;
        int distance;

        NodeDistance(Vertex v, int distance) {
            this.v = v;
            this.distance = distance;
        }
    }

    public static void main(String[] args) {
        ParisMetro parisMetro = null;
        long startTime;
        long endTime;

        System.out.println(args.length);
        try {
            parisMetro = readFile("metro.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (args.length == 1) {
            int stationNumber = Integer.parseInt(args[0]);
            List<Integer> sameLineStations = parisMetro.getSameLineStations(stationNumber);
            System.out.println("Stations on the same line as " + stationNumber + " are as follows : " + sameLineStations);

        } else if (args.length == 2) {
            Vertex org = parisMetro.metroGraph.getVertex(Integer.parseInt(args[0]));
            Vertex des = parisMetro.metroGraph.getVertex(Integer.parseInt(args[1]));

            startTime = System.currentTimeMillis();
            List<Integer> shortestPath = parisMetro.findShortestPath(org, des);
            endTime = System.currentTimeMillis();

            System.out.println("Time:" + (endTime - startTime));
            System.out.println("Shortest path from " + org + " to " + des + " is as follows: " + shortestPath);
        } else if (args.length == 3) {
            Vertex org = parisMetro.metroGraph.getVertex(Integer.parseInt(args[0]));
            Vertex des = parisMetro.metroGraph.getVertex(Integer.parseInt(args[1]));
            Vertex brokenLine = parisMetro.metroGraph.getVertex(Integer.parseInt(args[2]));

            startTime = System.currentTimeMillis();
            List<Integer> shortestPath = parisMetro.findShortestPathWithBrokenLine(org, des, brokenLine);
            endTime = System.currentTimeMillis();

            System.out.println("Time:" + (endTime - startTime));
            System.out.println("Shortest path from " + org + " to " + des + " with broken line at " + brokenLine + " is as follows: " + shortestPath);
        } else {

        }
    }
}

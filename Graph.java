import java.util.*;
public class Graph{
    private Map<Vertex, List<Edge>> al; // adjancency list used as data structure

    public Graph(){
        this.al = new HashMap<>();
    }

    public void addVertex(Vertex vert){
        al.put(vert, new LinkedList<>());
    }

    public void addEdge(Vertex u, Vertex v, int weight) {
        Edge edge = new Edge(u, v, weight);
        al.get(u).add(edge);
        al.get(v).add(edge);
    }

    public Vertex getVertex(int stationNumber){
        for (Vertex vertex : al.keySet()) {
            if (vertex.getStationNumber() == stationNumber) {
                return vertex;
            }
        }
        return null;

    }

    public Set<Vertex> getNeighbors(Vertex vert){
        Set<Vertex> neighbors = new HashSet<>();
        for (Edge edge : al.get(vert)) {
            neighbors.add(edge.getOtherVertex(vert));
        }
        return neighbors;

    }


    public Set<Vertex> getVertices(){
        return al.keySet();
    }

    public int getWeight(Vertex u, Vertex v) {
        for (Edge edge : al.get(u)) {
            if (edge.containsVertices(u, v)) {
                return edge.getWeight();
            }
        }
        return Integer.MAX_VALUE; 
    }  





    
}
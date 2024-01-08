    import java.util.*;
    public class Edge{
        Vertex des;
        Vertex orgin;
        int weight;
        public Edge(Vertex orgin, Vertex des, int weight){
            this.des = des;
            this.orgin = orgin;
            this.weight = weight;
        }

        public Vertex getOtherVertex(Vertex vertex) {

            if(vertex.equals(orgin)){
                return des;
            }
            else{
                return orgin;
            }
          
        }

        public boolean containsVertices(Vertex vertex1, Vertex vertex2) {
            return (orgin.equals(vertex1) && des.equals(vertex2)) || (orgin.equals(vertex2) && des.equals(vertex1));
        }

        public int getWeight() {
            return weight;
        }

    }
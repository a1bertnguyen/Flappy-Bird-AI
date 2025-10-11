package flappy.AI;

public class Connection {
    public Node fromNode;
    public Node toNode;
    public float weight;

    public Connection(Node fromNode, Node toNode, float weight) {
        this.fromNode = fromNode;
        this.toNode = toNode;
        this.weight = weight;
    }
}


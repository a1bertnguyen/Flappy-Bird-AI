package flappy.AI;

import java.util.ArrayList;
import java.util.List;

public class Node {
    public int id;
    public int layer;
    public float inputValue = 0;
    public float outputValue = 0;
    public boolean activated = false;

    public List<Connection> connections = new ArrayList<>();

    public Node(int id) {
        this.id = id;
        this.layer = 0; // default: input layer
    }

    public void activate() {
        if (activated) return; // tránh kích hoạt nhiều lần

        if (layer == 1) {
            outputValue = sigmoid(inputValue);  // input node
        } else {
            outputValue = inputValue; // hidden/output
        }

        for (Connection conn : connections) {
            conn.toNode.inputValue += outputValue * conn.weight;
        }

        activated = true;
    }

    public void addConnection(Node to, float weight) {
        connections.add(new Connection(this, to, weight));
    }

    public void reset() {
        inputValue = 0;
        outputValue = 0;
        activated = false;
    }

    private float sigmoid(float x) {
        return (float)(1.0 / (1.0 + Math.exp(-x)));
    }
}

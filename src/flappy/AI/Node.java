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
        this.layer = 0;
    }

    public void activate() {
        if (activated) return;

        if (layer > 0) {
            outputValue = sigmoid(inputValue);
        } else {
            outputValue = inputValue;
        }

        for (Connection conn : connections) {
            conn.to.inputValue += outputValue * conn.weight;
        }

        activated = true;
    }

    public Node cloneNode() {
        Node clone = new Node(this.id);
        clone.layer = this.layer;
        return clone;
    }

    private float sigmoid(float x) {
        return (float)(1.0 / (1.0 + Math.exp(-x)));
    }
}

package flappy.AI;

import java.util.*;

public class Brain {

    public List<Node> nodes = new ArrayList<>();
    public List<Connection> connections = new ArrayList<>();
    public List<Node> net = new ArrayList<>();
    public int inputs;
    public int layers = 2;

    public Brain(int inputs, boolean clone) {
        this.inputs = inputs;

        if (!clone) {
            // Tạo các node input
            for (int i = 0; i < inputs; i++) {
                Node n = new Node(i);
                n.layer = 0;
                nodes.add(n);
            }

            // Node bias
            Node bias = new Node(inputs);
            bias.layer = 0;
            nodes.add(bias);

            // Node output
            Node output = new Node(inputs + 1);
            output.layer = 1;
            nodes.add(output);

            // Kết nối input -> output
            Random rand = new Random();
            for (int i = 0; i <= inputs; i++) {
                connections.add(new Connection(nodes.get(i), output, rand.nextFloat() * 2 - 1));
            }
        }
    }

    public void connectNodes() {
        for (Node node : nodes) {
            node.connections.clear();
        }
        for (Connection c : connections) {
            c.from.connections.add(c);
        }
    }

    public void generateNet() {
        connectNodes();
        net.clear();
        for (int l = 0; l < layers; l++) {
            for (Node n : nodes) {
                if (n.layer == l) {
                    net.add(n);
                }
            }
        }
    }

    public float feedForward(float[] vision) {
        // 1. Gán input cho các node đầu vào
        for (int i = 0; i < inputs; i++) {
            // vision[i] là giá trị cảm biến đã chuẩn hoá
            nodes.get(i).inputValue = vision[i];
            nodes.get(i).outputValue = vision[i];  // không bắt buộc, nhưng giúp rõ ràng
        }

        // 2. Gan bias = 1 cho node bias
        nodes.get(inputs).inputValue = 1f;
        nodes.get(inputs).outputValue = 1f;

        // 3. Kích hoạt lần lượt các node trong mạng
        for (Node n : net) {
            n.activate();
        }

        // 4. Lay output tu node output
        float result = nodes.get(inputs + 1).outputValue;

        // 5. Reset inputValue (và có thể cả cờ activated nếu bạn dùng nhiều lần)
        for (Node n : nodes) {
            n.inputValue = 0;
            n.activated = false;  // nếu trong Node có biến này public / có setter
            // nếu không, bạn có thể thêm hàm reset() trong Node và gọi ở đây
        }

        return result;
    }

    public Brain cloneBrain() {
        Brain clone = new Brain(inputs, true);

        for (Node n : nodes) {
            clone.nodes.add(n.cloneNode());
        }

        for (Connection c : connections) {
            clone.connections.add(c.cloneConnection(clone.getNode(c.from.id), clone.getNode(c.to.id)));
        }

        clone.layers = this.layers;
        clone.connectNodes();

        return clone;
    }

    public Node getNode(int id) {
        for (Node n : nodes) {
            if (n.id == id) {
                return n;
            }
        }
        return null;
    }

    public void mutate() {
        if (Math.random() < 0.95) {
            for (Connection c : connections) {
                c.mutateWeight();
            }
        }

        if (Math.random() < 0.15) {
            addHiddenNode();
        }

        generateNet();
    }

    private void addHiddenNode() {
        if (nodes.size() >= 10) {
            return;
        }

        int newId = nodes.size();
        Node hidden = new Node(newId);
        hidden.layer = 1;
        nodes.add(hidden);

        Node output = getNode(inputs + 1);
        if (output == null) {
            return;
        }
        output.layer = 2;

        Random rand = new Random();
        for (int i = 0; i <= inputs; i++) {
            connections.add(new Connection(nodes.get(i), hidden, rand.nextFloat() * 2 - 1));
        }
        connections.add(new Connection(hidden, output, rand.nextFloat() * 2 - 1));

        layers = 3;
    }
}
